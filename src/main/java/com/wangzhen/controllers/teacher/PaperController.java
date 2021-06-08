package com.wangzhen.controllers.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.PaperService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/17 21:41
 */
@RestController
@RequestMapping("/teacher")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @RequestMapping("/updatePaperInfo")
    public ResponseMessage updatePaperInfo(@RequestParam("sort")boolean sort,
                                           @RequestParam("openFaceIdentity")boolean openFaceIdentity,
                                           @RequestParam("begin_time")long begin_time,
                                           @RequestParam("visible")boolean visible,
                                           @RequestParam("end_time")long end_time,
                                           @RequestParam("during")Integer during,
                                           @RequestParam("uuid")String uuid){
        paperService.updatePaperInfo(sort,openFaceIdentity,visible,new Timestamp(begin_time),new Timestamp(end_time),during,uuid);
        return ResponseMessage.OK;
    }

    @RequestMapping("/deletePaperByUuid")
    public ResponseMessage deletePaperByUuid(@RequestParam("uuid")String uuid){
        paperService.deletePaperByUuid(uuid);
        return ResponseMessage.OK;
    }

    @RequestMapping("/selectMyAllPaper")
    public List<Paper> selectMyAllPaper(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return paperService.selectMyAllPaper(teacher.getUuid());
    }
    @RequestMapping("/selectPaperByPaper_uuid")
    public Paper selectPaperByPaper_uuid(@RequestParam("paper_uuid") String paper_uuid){
        return paperService.selectPaperByPaper_uuid(paper_uuid);
    }
}
