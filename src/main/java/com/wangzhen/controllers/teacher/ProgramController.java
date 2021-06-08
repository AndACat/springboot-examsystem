package com.wangzhen.controllers.teacher;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.problem.Option;
import com.wangzhen.models.problem.Program;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.ProgramService;
import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.utils.ExcelReadUtil;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/28 1:18
 */
@RestController
@RequestMapping("/teacher")
public class ProgramController {
    @Autowired
    private ProgramService programService;
    @RequestMapping("/selectMyAllProgram")
    public List<Program> selectMyAllProgram(HttpSession session){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        return programService.selectMyAllProgram(teacher.getAccount());
    }
    @RequestMapping("/insertProgram")
    public ResponseMessage insertProgram(HttpSession session, @RequestParam("program") Program program){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String uuid = Utils.randomUuid();
        program.setUuid(uuid);
        programService.insertProgram(teacher.getAccount(), program);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("插入成功");
    }
    @RequestMapping("/deleteProgramByUuid")
    public ResponseMessage deleteProgramByUuid(HttpSession session, String uuid){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        programService.deleteProgramByUuid(teacher.getAccount(),uuid);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("删除成功");
    }
    @RequestMapping("/updateProgramInfo")
    public ResponseMessage updateProgramInfo(HttpSession session, @RequestParam("program") Program program, @RequestParam(value = "file",required = false) MultipartFile file){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String videoPath = null;
        if(file != null){
            String saveName = Utils.randomUuid() + file.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsoluteSavePath = programService.saveVideoToDisk(file, saveName);//返回保存的绝对地址
            if(videoAbsoluteSavePath == ""){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            programService.deleteOldVideo(program.getVideoPath());
            videoPath = "/video/program/"+saveName;
            program.setVideoPath(videoPath);
        }
        programService.updateProgramInfo(teacher.getAccount(), program);
        return new ResponseMessage()
                .setCode(200)
                .setMsg("更新成功")
                .setData(videoPath);
    }

//    @RequestMapping("/insertProgramByExcel")
//    public ResponseMessage insertProgramByExcel(@RequestParam("excelFile")MultipartFile excelFile,HttpSession session){
//        StringBuffer errMsg = new StringBuffer();
//        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
//        List<Program> programList = ExcelReadUtil.getProgramList(excelFile,errMsg);//返回Excel中的数据集合
//        if(programList != null || programList.size() == 0){
//            programService.insertProgramWithExcel(programList,teacher.getAccount());//插入题库
//            return new ResponseMessage().setCode(200).setData(errMsg.toString()).setMsg("插入成功");
//        }else{
//            return new ResponseMessage().setCode(200).setMsg("excel数据空");
//        }
//    }
}
