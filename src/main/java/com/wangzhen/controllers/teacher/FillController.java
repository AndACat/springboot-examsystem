package com.wangzhen.controllers.teacher;
import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.models.problem.Fill;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.FillService;
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
 * @CreateDate 2020/2/28 1:18
 */
@RestController
@RequestMapping("/teacher")
public class FillController {
    @Autowired
    private FillService fillService;
    @RequestMapping("/selectMyAllFill")
    public List<Fill> selectMyAllFill(HttpSession session){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        return fillService.selectMyAllFill(teacher.getAccount());
    }
    @RequestMapping("/insertFill")
    public ResponseMessage insertFill(HttpSession session, @RequestParam("fill") Fill fill){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String uuid = Utils.randomUuid();
        fill.setUuid(uuid);
        fillService.insertFill(teacher.getAccount(), fill);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("插入成功");
    }
    @RequestMapping("/deleteFillByUuid")
    public ResponseMessage deleteFillByUuid(HttpSession session, String uuid){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        fillService.deleteFillByUuid(teacher.getAccount(),uuid);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("删除成功");
    }
    @RequestMapping("/updateFillInfo")
    public ResponseMessage updateFillInfo(HttpSession session, @RequestParam("fill") Fill fill, @RequestParam(value = "excelFile",required = false) MultipartFile excelFile){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String videoPath = null;
        if(excelFile != null){
            String saveName = Utils.randomUuid() + excelFile.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsoluteSavePath = fillService.saveVideoToDisk(excelFile, saveName);//返回保存的绝对地址
            if(videoAbsoluteSavePath == ""){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            fillService.deleteOldVideo(fill.getVideoPath());
            videoPath = "/video/fill/"+saveName;
            fill.setVideoPath(videoPath);
        }
        fillService.updateFillInfo(teacher.getAccount(), fill);
        return new ResponseMessage()
                .setCode(200)
                .setMsg("更新成功")
                .setData(videoPath);
    }

    @RequestMapping("/insertFillByExcel")
    public ResponseMessage insertFillByExcel(@RequestParam("excelFile")MultipartFile excelFile,HttpSession session){
        StringBuffer errMsg = new StringBuffer();
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        List<Fill> fillList = ExcelReadUtil.getFillList(excelFile,errMsg);//返回Excel中的数据集合
        if(fillList != null || fillList.size() == 0){
            fillService.insertFillWithExcel(fillList,teacher.getAccount());//插入题库
            return new ResponseMessage().setCode(200).setData(errMsg.toString()).setMsg("插入成功");
        }else{
            return new ResponseMessage().setCode(200).setMsg("excel数据空");
        }
    }
}
