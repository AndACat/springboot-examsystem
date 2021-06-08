package com.wangzhen.controllers.teacher;

import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.MultipleChoiceService;
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
 * @Description 多选题的控制层
 * @CreateDate 2020/2/26 17:11
 */
@SuppressWarnings("all")
@RestController
@RequestMapping({"/teacher"})
@Slf4j
public class MultipleChoiceController {
    @Autowired
    private MultipleChoiceService multipleChoiceService;
    @Autowired
    private Converter<String[],List<String>> stringToListConverter;


    @RequestMapping("/selectMyAllMultipleChoice")
    public List<MultipleChoice> selectMyAllMultipleChoice(HttpSession session){
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        return multipleChoiceService.selectMyAllMultipleChoice(teacher.getAccount());
    }

    @RequestMapping("/selectMyCollegeAllMultipleChoice")
    public List<MultipleChoice> selectMyCollegeAllMultipleChoice(HttpSession session){
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        return multipleChoiceService.selectMyCollegeAllMultipleChoice(teacher.getCollege());
    }

    @RequestMapping("/insertMultipleChoice")
    public ResponseMessage insertMultipleChoice(@RequestParam("multiplechoice")MultipleChoice multiplechoice,@RequestParam(value = "video",required = false)MultipartFile video,HttpSession session){
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        String videoPath = null;
        if(video != null){
            String saveName = Utils.randomUuid() + video.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsouluteSavePath = multipleChoiceService.saveVideoToDisk(video, saveName);//返回保存的绝对地址
            if(videoAbsouluteSavePath.equals("")){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            videoPath = "/video/multiplechoice/"+saveName;
            multiplechoice.setVideoPath(videoPath);
        }
        String uuid = Utils.randomUuid();
        multiplechoice.setUuid(uuid);
        boolean tag = multipleChoiceService.insertMultipleChoice(multiplechoice, teacher.getAccount());
        return ResponseMessage
                .getInstance()
                .setCode(tag ? 200 : 403)
                .setMsg(tag ? "插入成功" : "插入失败")
                .setData(stringToListConverter.convert(new String[]{uuid,videoPath}));

    }

    @RequestMapping("/insertMultipleChoiceByExcel")
    public ResponseMessage insertMultipleChoiceByExcel(@RequestParam(value="excelFile",required=true) MultipartFile excelFile, HttpSession session){
        StringBuffer errMsg = new StringBuffer();
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        multipleChoiceService.createMultipleChoiceDataTable(teacher.getAccount());//创建教师的题库表
        List<MultipleChoice> multipleChoiceList = ExcelReadUtil.getMultipleChoiceList(excelFile,errMsg);//返回Excel中的数据集合
        if(multipleChoiceList != null || multipleChoiceList.size() == 0){
            multipleChoiceService.insertMultipleChoiceWithExcel(multipleChoiceList,teacher.getAccount());//插入题库
            return new ResponseMessage().setCode(200).setData(errMsg.toString()).setMsg("插入成功");
        }else{
            return new ResponseMessage().setCode(200).setMsg("excel数据空");
        }
    }

    @PostMapping("/deleteMultipleChoiceByUuid")
    public ResponseMessage deleteMultipleChoiceByUuid(String uuid,HttpSession session){
        //Teacher user = Utils.getUser(session, Teacher.class);
        //boolean tag = multipleChoiceService.deleteSinglechoiceByUuid(user.getAccount(), uuid);
        //return new ResponseMessage().setCode(tag?200:403);
        return new ResponseMessage().setCode(200);
    }

    @PostMapping("/updateMultipleChoiceInfo")
    public ResponseMessage updateProblemInfo(@RequestParam("problem")MultipleChoice problem,@RequestParam(value="file",required=false) MultipartFile file, HttpSession session){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        if(file == null){
            //无需修改视频文件,直接修改题目其他信息...
            boolean tag = multipleChoiceService.updateMultipleChoiceInfo(problem,teacher.getAccount());
            return new ResponseMessage().setCode(tag ? 200 : 403);
        }else{
            String saveName = Utils.randomUuid() + file.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsouluteSavePath = multipleChoiceService.saveVideoToDisk(file, saveName);//返回保存的绝对地址
            if(videoAbsouluteSavePath == ""){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            multipleChoiceService.deleteOldVideo(problem.getVideoPath());
            String videoPath = "/video/multiplechoice/"+saveName;
            //视频文件保存成功,接下来插入到数据库
            boolean tag = multipleChoiceService.updateMultipleChoiceInfo(problem, videoPath, teacher.getAccount());
            return new ResponseMessage().setCode(tag ? 200 : 403).setData(videoPath);
        }
    }


}
