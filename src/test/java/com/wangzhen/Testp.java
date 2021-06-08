package com.wangzhen;

import com.wangzhen.models.Paper;
import com.wangzhen.models.StudentPaperAnswer;
import com.wangzhen.services.student.StudentPaperAnswerService;
import com.wangzhen.services.teacher.PaperService;
import com.wangzhen.utils.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/19 16:35
 */
@SpringBootTest
public class Testp {
    @Autowired
    PaperService paperService;
    @Autowired
    StudentPaperAnswerService studentPaperAnswerService;
    @Test
    public void m(){
        ArrayList<String> strings = new ArrayList<>();
        strings.add("1dsddsad3a3ghjmld");
        strings.add("952db1671be14c339727808e44a1b645");
        strings.add("222222222");
        List<Paper> papers = paperService.selectSimplePaperForStudentToDisplay(strings);
        System.out.println(papers);

    }
    @Test
    public void m1(){
        List<Paper> papers = paperService.selectAllPaper();
        System.out.println(papers);

    }
    @Test
    public void  没(){
        List<StudentPaperAnswer> studentPaperAnswers = studentPaperAnswerService.selectAllStudentPaperAnswerOfSinglePaper("123");
        System.out.println(studentPaperAnswers.size());
    }
    @Test
    public void  没1(){
        StudentPaperAnswer studentPaperAnswer = new StudentPaperAnswer();
        studentPaperAnswer.setUuid(Utils.randomUuid());
        studentPaperAnswerService.insertStudentPaperAnswer(studentPaperAnswer);
    }
}
