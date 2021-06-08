package com.wangzhen.models;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/8 18:00
 */
@Builder
@AllArgsConstructor
public class Paper implements Cloneable{
    public Paper(){}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    private String uuid;                                //主键
    private String paperName;                           //试卷名称
    private Boolean sort;   //是否乱序显示题目。true正序 false乱序
    private Boolean openFaceIdentity;
    private String teacher_uuid;
    private Teacher teacher;

    private String paperStrategy_uuid;
    private PaperStrategy paperStrategy;
    private String course_uuid;//课程的uuid
    private Course course;                              //所属课程  属于该课程的学生都参加考试
    private Cla cla;//考试班级

    private Timestamp begin;                                 //开考时间
    private Timestamp end;                                   //结考时间
    private Integer during;                             //考试时间 单位：分钟
    private Long begin_time;
    private Long end_time;
    private Boolean visible;

    private Float totalScore;                         //试卷总分


    private LinkedHashMap<String,Object> paperInfo = new LinkedHashMap<>(); //有序的paperInfo

    public String getStringPaperInfo(){
        return JSON.toJSONString(paperInfo);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public Boolean getSort() {
        return sort;
    }

    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    public Boolean getOpenFaceIdentity() {
        return openFaceIdentity;
    }

    public void setOpenFaceIdentity(Boolean openFaceIdentity) {
        this.openFaceIdentity = openFaceIdentity;
    }

    public String getTeacher_uuid() {
        return teacher_uuid;
    }

    public void setTeacher_uuid(String teacher_uuid) {
        this.teacher_uuid = teacher_uuid;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getPaperStrategy_uuid() {
        return paperStrategy_uuid;
    }

    public void setPaperStrategy_uuid(String paperStrategy_uuid) {
        this.paperStrategy_uuid = paperStrategy_uuid;
    }

    public PaperStrategy getPaperStrategy() {
        return paperStrategy;
    }

    public void setPaperStrategy(PaperStrategy paperStrategy) {
        this.paperStrategy = paperStrategy;
    }

    public String getCourse_uuid() {
        return course_uuid;
    }

    public void setCourse_uuid(String course_uuid) {
        this.course_uuid = course_uuid;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Cla getCla() {
        return cla;
    }

    public void setCla(Cla cla) {
        this.cla = cla;
    }

    public Timestamp getBegin() {
        return begin;
    }

    public void setBegin(Timestamp begin) {
        this.begin = begin;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public Integer getDuring() {
        return during;
    }

    public void setDuring(Integer during) {
        this.during = during;
    }

    public Long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Long begin_time) {
        this.begin_time = begin_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    public LinkedHashMap<String, Object> getPaperInfo() {
        return paperInfo;
    }

    public void setPaperInfo(LinkedHashMap<String, Object> paperInfo) {
        this.paperInfo = paperInfo;
    }
}
