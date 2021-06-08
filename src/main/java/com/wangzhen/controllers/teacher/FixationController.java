package com.wangzhen.controllers.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.services.teacher.FixationPaperService;
import com.wangzhen.utils.ResponseMessage;
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
 * @CreateDate 2020/4/1 18:59
 */
@RestController
@RequestMapping("/teacher")
public class FixationController {
    @Autowired
    private FixationPaperService fixationPaperService;
    @RequestMapping("/saveFixationPaper")
    public ResponseMessage saveFixationPaper(@RequestParam("course_uuid") String course_uuid,
                                             @RequestParam("paperName")String paperName,
                                             @RequestParam("during")Integer during,
                                             @RequestParam("sort")boolean sort,
                                             @RequestParam("openFaceIdentity")boolean openFaceIdentity,
                                             @RequestParam("begin_time")long begin_time,
                                             @RequestParam("end_time")long end_time,
                                             @RequestParam("visible") boolean visible,
                                             @RequestParam("singleChoice_uuid_list") List<String> singleChoice_uuid_list,
                                             @RequestParam("multipleChoice_uuid_list") List<String> multipleChoice_uuid_list,
                                             @RequestParam("fill_uuid_list") List<String> fill_uuid_list,
                                             @RequestParam("judge_uuid_list") List<String> judge_uuid_list,
                                             @RequestParam("short_uuid_list") List<String> short_uuid_list,
                                             @RequestParam("program_uuid_list") List<String> program_uuid_list,
                                             @RequestParam("singleChoiceScore")float singleChoiceScore,
                                             @RequestParam("multipleChoiceScore")float multipleChoiceScore,
                                             @RequestParam("fillScore")float fillScore,
                                             @RequestParam("judgeScore")float judgeScore,
                                             @RequestParam("shortScore")float shortScore,
                                             @RequestParam("programScore")float programScore,
                                             HttpSession session){

        boolean tag = fixationPaperService.savePaper(course_uuid, paperName, during, sort, openFaceIdentity, new Timestamp(begin_time), new Timestamp(end_time), visible, singleChoice_uuid_list, multipleChoice_uuid_list, fill_uuid_list, judge_uuid_list, short_uuid_list, program_uuid_list, singleChoiceScore, multipleChoiceScore, fillScore, judgeScore, shortScore, programScore, session);
        return tag ? ResponseMessage.OK :ResponseMessage.FAIL;
    }
}
