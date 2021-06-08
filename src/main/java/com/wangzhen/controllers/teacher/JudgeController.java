package com.wangzhen.controllers.teacher;

import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.models.problem.Judge;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.JudgeService;
import com.wangzhen.utils.ExcelReadUtil;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/27 15:11
 */
@RestController
@RequestMapping("/teacher")
public class JudgeController {
    @Autowired
    private JudgeService judgeService;
    @RequestMapping("/selectMyAllJudge")
    public List<Judge> selectMyAllJudge(HttpSession session){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        return judgeService.selectMyAllJudge(teacher.getAccount());
    }
    @RequestMapping("/insertJudge")
    public ResponseMessage insertJudge(HttpSession session, @RequestParam("judge") Judge judge){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String uuid = Utils.randomUuid();
        judge.setUuid(uuid);
        judgeService.insertJudge(teacher.getAccount(), judge);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("插入成功");
    }
    @RequestMapping("/deleteJudgeByUuid")
    public ResponseMessage deleteJudgeByUuid(HttpSession session, String uuid){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        judgeService.deleteJudgeByUuid(teacher.getAccount(),uuid);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("删除成功");
    }
    @RequestMapping("/updateJudgeInfo")
    public ResponseMessage updateJudgeInfo(HttpSession session, @RequestParam("judge") Judge judge, @RequestParam(value = "excelFile",required = false)MultipartFile excelFile){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String videoPath = null;
        if(excelFile != null){
            String saveName = Utils.randomUuid() + excelFile.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsoluteSavePath = judgeService.saveVideoToDisk(excelFile, saveName);//返回保存的绝对地址
            if(videoAbsoluteSavePath == ""){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            judgeService.deleteOldVideo(judge.getVideoPath());
            videoPath = "/video/judge/"+saveName;
            judge.setVideoPath(videoPath);
        }
        judgeService.updateJudgeInfo(teacher.getAccount(), judge);
        return new ResponseMessage()
                .setCode(200)
                .setMsg("更新成功")
                .setData(videoPath);
    }

    @RequestMapping("/insertJudgeByExcel")
    public ResponseMessage insertJudgeByExcel(@RequestParam("excelFile")MultipartFile excelFile,HttpSession session){
        StringBuffer errMsg = new StringBuffer();
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        List<Judge> judgeList = ExcelReadUtil.getJudgeList(excelFile,errMsg);//返回Excel中的数据集合
        if(judgeList != null || judgeList.size() == 0){
            judgeService.insertSingleChoiceWithExcel(judgeList,teacher.getAccount());//插入题库
            return new ResponseMessage().setCode(200).setData(errMsg.toString()).setMsg("插入成功");
        }else{
            return new ResponseMessage().setCode(200).setMsg("excel数据空");
        }
    }

}
