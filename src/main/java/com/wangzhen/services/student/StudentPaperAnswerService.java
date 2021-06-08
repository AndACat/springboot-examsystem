package com.wangzhen.services.student;

import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.StudentPaperAnswerDao;
import com.wangzhen.models.StudentPaperAnswer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/24 14:15
 */
@Service
public class StudentPaperAnswerService {
    @Autowired
    private StudentPaperAnswerDao studentPaperAnswerDao;

    public List<StudentPaperAnswer> selectAllStudentPaperAnswerOfSinglePaper(@Param("paper_uuid") String paper_uuid){
        return studentPaperAnswerDao.selectAllStudentPaperAnswerOfSinglePaper(paper_uuid);
    }

    public StudentPaperAnswer selectStudentPaperAnswer(@Param("paper_uuid") String paper_uuid,@Param("student_uuid") String student_uuid){
        return studentPaperAnswerDao.selectStudentPaperAnswer(paper_uuid,student_uuid);
    }

    public Boolean insertStudentPaperAnswer(String uuid, String paper_uuid, String student_uuid, Float singleChoiceScore, Float multipleChoiceScore, Float fillScore, Float shortScore, Float judgeScore, Float programScore, Float totalScore, Float paperTotalScore, LinkedHashMap<Integer,Object> answers, Boolean pass,Boolean complete){
        return studentPaperAnswerDao.insertStudentPaperAnswer(
                uuid,
                paper_uuid,
                student_uuid,
                singleChoiceScore,
                multipleChoiceScore,
                fillScore,
                shortScore,
                judgeScore,
                programScore,
                totalScore,
                paperTotalScore,
                JSON.toJSONString(answers),
                pass,
                complete);
    }

    public void insertStudentPaperAnswer(StudentPaperAnswer studentPaperAnswer) {
        this.insertStudentPaperAnswer(studentPaperAnswer.getUuid(),
                studentPaperAnswer.getPaper_uuid(),
                studentPaperAnswer.getStudent_uuid(),
                studentPaperAnswer.getSingleChoiceScore(),
                studentPaperAnswer.getMultipleChoiceScore(),
                studentPaperAnswer.getFillScore(),
                studentPaperAnswer.getShortScore(),
                studentPaperAnswer.getJudgeScore(),
                studentPaperAnswer.getProgramScore(),
                studentPaperAnswer.getTotalScore(),
                studentPaperAnswer.getPaperTotalScore(),
                studentPaperAnswer.getAnswers(),
                studentPaperAnswer.getPass(),
                studentPaperAnswer.getComplete()
        );
    }

    public StudentPaperAnswer selectStudentPaperAnswerByPaper_uuidAndStudent_uuid(String paper_uuid, String student_uuid) {
        return studentPaperAnswerDao.selectStudentPaperAnswerByPaper_uuidAndStudent_uuid(paper_uuid,student_uuid);
    }

    public void addZbList(String student_uuid,String paper_uuid, List<String> zbList) {
        studentPaperAnswerDao.addZbList(student_uuid,paper_uuid,JSON.toJSONString(zbList));
    }

    public void submitScoreOfMark(String paper_uuid, String student_uuid, Float fillTotalScore, Float shortTotalScore) {
        studentPaperAnswerDao.submitScoreOfMark(paper_uuid,student_uuid,fillTotalScore,shortTotalScore);
        studentPaperAnswerDao.autoChangeTotalScore(paper_uuid,student_uuid);
        studentPaperAnswerDao.autoChangePass(paper_uuid,student_uuid);
    }

    public List<StudentPaperAnswer> getMyScore(String student_uuid){
        return studentPaperAnswerDao.getMyScore(student_uuid);
    }
}
