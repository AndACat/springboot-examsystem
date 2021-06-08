package com.wangzhen.controllers.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.models.StudentPaperAnswer;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.manager.StudentService;
import com.wangzhen.services.student.StudentPaperAnswerService;
import com.wangzhen.services.teacher.PaperService;
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
 * @CreateDate 2020/3/29 21:00
 */
@RestController
@RequestMapping("/teacher")
public class ScoreController {
    @Autowired
    private PaperService paperService;
    @Autowired
    private StudentPaperAnswerService studentPaperAnswerService;
    @Autowired
    private StudentService studentService;
    /**
     * @Description
     * @date 2020/3/29 21:02
     * @return  key:paper_uuid value:paperName
     * @return java.util.LinkedHashMap<java.lang.String,java.lang.String>
     */
    @RequestMapping("/getScoreOfMyAllPaper")
    public LinkedHashMap<String,String> getScoreOfMyAllPaper(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return paperService.getScoreOfMyAllPaper(teacher.getUuid());

    }

    @RequestMapping("/getStudentScores")
    public List<StudentPaperAnswer> getStudentScores(@RequestParam("paper_uuid") String paper_uuid){
        List<StudentPaperAnswer> studentPaperAnswerList = studentPaperAnswerService.selectAllStudentPaperAnswerOfSinglePaper(paper_uuid);
        for (StudentPaperAnswer studentPaperAnswer : studentPaperAnswerList) {
            studentPaperAnswer.setStudent(studentService.selectStudentByUuid(studentPaperAnswer.getStudent_uuid()));
            String paper_uuid1 = studentPaperAnswer.getPaper_uuid();
            Paper paper = paperService.selectPaperByPaper_uuid(paper_uuid);
            studentPaperAnswer.setPaper(paper);
        }
        return studentPaperAnswerList;
    }

}
