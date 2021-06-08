package com.wangzhen.controllers.teacher;

import com.wangzhen.models.StudentPaperAnswer;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.student.StudentPaperAnswerService;
import com.wangzhen.services.teacher.MarkingService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/27 16:28
 */
@RestController
@RequestMapping("/teacher")
public class MarkingController {
    @Autowired
    private MarkingService markingService;
    @Autowired
    private StudentPaperAnswerService studentPaperAnswerService;
    @RequestMapping("/getMarkingPaper")
    public LinkedHashMap<String, String> getMarkingPaper(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        LinkedHashMap<String, String> markingPaper = markingService.getMarkingPaper(teacher.getUuid());
        return markingPaper;
    }
    @RequestMapping("/selectOneRequiredMarkingPaper")
    public List<StudentPaperAnswer> selectOneRequiredMarkingPaper(@RequestParam("paper_uuid") String paper_uuid){
        return markingService.selectOneRequiredMarkingPaper(paper_uuid);
    }
    @RequestMapping("/submitScoreOfMark")
    public ResponseMessage submitScoreOfMark(@RequestParam(value = "fillTotalScore",required = false) Float fillTotalScore,
                                             @RequestParam(value = "shortTotalScore",required = false) Float shortTotalScore,
//                                             @RequestParam(value = "programTotalScore",required = false) Float programTotalScore,
                                             @RequestParam(value = "paper_uuid") String paper_uuid,
                                             @RequestParam(value = "student_uuid") String student_uuid){
        studentPaperAnswerService.submitScoreOfMark(paper_uuid,student_uuid,fillTotalScore,shortTotalScore);
        return ResponseMessage.OK;
    }
}
