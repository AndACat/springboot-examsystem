package com.wangzhen.controllers.teacher;

import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.ShortService;
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
public class ShortController {
    @Autowired
    private ShortService shortService;
    @RequestMapping("/selectMyAllShort")
    public List<Short> selectMyAllShort(HttpSession session){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        return shortService.selectMyAllShort(teacher.getAccount());
    }
    @RequestMapping("/insertShort")
    public ResponseMessage insertShort(HttpSession session, @RequestParam("short") Short sho){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String uuid = Utils.randomUuid();
        sho.setUuid(uuid);
        shortService.insertShort(teacher.getAccount(), sho);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("插入成功");
    }
    @RequestMapping("/deleteShortByUuid")
    public ResponseMessage deleteShortByUuid(HttpSession session, String uuid){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        shortService.deleteShortByUuid(teacher.getAccount(),uuid);
        return new ResponseMessage()
                .setCode(200)
                .setData(uuid)
                .setMsg("删除成功");
    }
    @RequestMapping("/updateShortInfo")
    public ResponseMessage updateShortInfo(HttpSession session, @RequestParam("short") Short sho, @RequestParam(value = "excelFile",required = false) MultipartFile excelFile){
        Teacher teacher = Utils.getUser(session,Teacher.class);
        String videoPath = null;
        if(excelFile != null){
            String saveName = Utils.randomUuid() + excelFile.getOriginalFilename();//指定保存视频文件的新名字
            String videoAbsoluteSavePath = shortService.saveVideoToDisk(excelFile, saveName);//返回保存的绝对地址
            if(videoAbsoluteSavePath == ""){//上传失败
                return new ResponseMessage().setCode(500).setMsg("视频文件保存失败,请联系技术人员");
            }
            shortService.deleteOldVideo(sho.getVideoPath());
            videoPath = "/video/short/"+saveName;
            sho.setVideoPath(videoPath);
        }
        shortService.updateShortInfo(teacher.getAccount(), sho);
        return new ResponseMessage()
                .setCode(200)
                .setMsg("更新成功")
                .setData(videoPath);
    }

    @RequestMapping("/insertShortByExcel")
    public ResponseMessage insertShortByExcel(@RequestParam("excelFile")MultipartFile excelFile,HttpSession session){
        StringBuffer errMsg = new StringBuffer();
        Teacher teacher = (Teacher) session.getAttribute(UserIdentity.TEACHER);
        List<Short> shortList = ExcelReadUtil.getShortList(excelFile,errMsg);//返回Excel中的数据集合
        if(shortList != null || shortList.size() == 0){
            shortService.insertSingleChoiceWithExcel(shortList,teacher.getAccount());//插入题库
            return new ResponseMessage().setCode(200).setData(errMsg.toString()).setMsg("插入成功");
        }else{
            return new ResponseMessage().setCode(200).setMsg("excel数据空");
        }
    }
}
