package com.wangzhen.controllers.teacher;

import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.models.problem.SingleChoice;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.SingleChoiceService;
import com.wangzhen.utils.ExcelReadUtil;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/10 23:10
 */
@SuppressWarnings("all")
@RestController
@RequestMapping({"/teacher"})
@Slf4j
public class SingleChoiceController {
    @Autowired
    private SingleChoiceService singleChoiceService;
    @Autowired
    private Converter<String[],List<String>> stringListConverter;


    @RequestMapping("/selectMyAllSingleChoice")
    public List<SingleChoice> selectMyAllSingleChoice(HttpSession session){
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        return singleChoiceService.selectMyAllSingleChoice(teacher.getAccount());
    }

    @RequestMapping("/selectMyCollegeAllSingleChoice")
    public List<SingleChoice> selectMyCollegeAllSingleChoice(HttpSession session){
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        return singleChoiceService.selectMyCollegeAllSingleChoice(teacher.getCollege());
    }

    @RequestMapping("/insertSingleChoice")
    public ResponseMessage insertSingleChoice(@RequestParam("singlechoice")SingleChoice singlechoice,@RequestParam(value = "video",required = false)MultipartFile video,HttpSession session){
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        String videoPath = null;
        if(video != null){
            String saveName = Utils.randomUuid() + video.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsouluteSavePath = singleChoiceService.saveVideoToDisk(video, saveName);//返回保存的绝对地址
            if(videoAbsouluteSavePath.equals("")){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            videoPath = "/video/singlechoice/"+saveName;
            singlechoice.setVideoPath(videoPath);
        }
        String uuid = Utils.randomUuid();
        singlechoice.setUuid(uuid);
        boolean tag = singleChoiceService.insertSingleChoice(singlechoice, teacher.getAccount());
        return ResponseMessage
                .getInstance()
                .setCode(tag ? 200 : 403)
                .setMsg(tag ? "插入成功" : "插入失败")
                .setData(stringListConverter.convert(new String[]{uuid,videoPath}));

    }

    @RequestMapping("/insertSingleChoiceByExcel")
    public ResponseMessage insertSingleChoiceByExcel(@RequestParam(value="excelFile",required=true) MultipartFile excelFile, HttpSession session){
        StringBuffer errMsg = new StringBuffer();
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        List<SingleChoice> singleChoiceList = ExcelReadUtil.getSingleChoiceList(excelFile,errMsg);//返回Excel中的数据集合
        if(singleChoiceList != null || singleChoiceList.size() == 0){
            singleChoiceService.insertSingleChoiceWithExcel(singleChoiceList,teacher.getAccount());//插入题库
            return new ResponseMessage().setCode(200).setData(errMsg.toString()).setMsg("插入成功");
        }else{
            return new ResponseMessage().setCode(200).setMsg("excel数据空");
        }
    }

    @PostMapping("/deleteSingleChoiceByUuid")
    public ResponseMessage deleteSingleChoiceByUuid(String uuid,HttpSession session){
        //Teacher user = Utils.getUser(session, Teacher.class);
        //boolean tag = singleChoiceService.deleteSinglechoiceByUuid(user.getAccount(), uuid);
        //return new ResponseMessage().setCode(tag?200:403);
        return new ResponseMessage().setCode(200);
    }

    @PostMapping("/updateSingleChoiceInfo")
    public ResponseMessage updateProblemInfo(@RequestParam("problem")SingleChoice problem,@RequestParam(value="file",required=false) MultipartFile file, HttpSession session){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        if(file == null){
            //无需修改视频文件,直接修改题目其他信息...
            boolean tag = singleChoiceService.updateSingleChoiceInfo(problem,teacher.getAccount());
            return new ResponseMessage().setCode(tag ? 200 : 403);
        }else{
            //指定保存视频文件的新名字
            String saveName = Utils.randomUuid() + file.getOriginalFilename();
            //返回保存的绝对地址
            String videoAbsoluteSavePath = singleChoiceService.saveVideoToDisk(file, saveName);
            if(videoAbsoluteSavePath == ""){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            singleChoiceService.deleteOldVideo(problem.getVideoPath());
            String videoPath = "/video/singlechoice/"+saveName;
            //视频文件保存成功,接下来插入到数据库
            boolean tag = singleChoiceService.updateSingleChoiceInfo(problem, videoPath, teacher.getAccount());
            return new ResponseMessage().setCode(tag ? 200 : 403).setData(videoPath);
        }
    }


}
