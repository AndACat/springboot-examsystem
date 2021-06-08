package com.wangzhen.models;

import com.wangzhen.models.users.Student;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 用来保存学生考试之后的试卷答案,用于后期自动阅卷使用
 * @CreateDate 2020/3/23 18:22
 */
@Getter
@Setter
public class StudentPaperAnswer {
    private String uuid;
    private String paper_uuid;//考试的试卷的uuid
    private String student_uuid;//考试的学生的uuid
    private Paper paper;
    private Student student;

    private Float singleChoiceScore = 0f;
    private Float multipleChoiceScore = 0f;
    private Float fillScore = 0f;
    private Float shortScore = 0f;
    private Float judgeScore = 0f;
    private Float programScore = 0f;//↑各题型的总分
    private Float totalScore;//学生的考试总分
    private Float paperTotalScore;//试卷的满分
    private List<String> zbList;//作弊图片地址的集合
    private LinkedHashMap<Integer,Object> answers;//学生的答案集合 key:totalProblemIdx(1开始) value: answer
    private Boolean pass;
    private Boolean complete;
    private Timestamp sort;

    public Boolean isPass(){
        return pass;
    }

    public Float getTotalScore(){
        return totalScore;
//                singleChoiceScore == 0f ? 0f : singleChoiceScore
//                +
//                multipleChoiceScore == 0f ? 0f : multipleChoiceScore
//                +
//                fillScore == 0f ? 0f : fillScore
//                +
//                shortScore == 0f ? 0f : shortScore
//                +
//                judgeScore == 0f ? 0f : judgeScore
//                +
//                programScore == 0f ? 0f : programScore;
    }
}
