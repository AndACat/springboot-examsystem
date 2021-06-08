package com.wangzhen.models.problem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.*;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 编程题
 * @CreateDate 2020/1/8 18:00
 */
public class Program {
    private static final String tag = "PROGRAM";
    private String uuid;                //主键
    private String problem;             //问题内容
    private List<Option> ioList;        //参考答案 一般10个
    private String videoPath;               //解析视频地址
    private String analysis;
    private Integer useNum;             //参与成功组卷的次数
    private Integer allNum;             //总答题次数
    private Integer correctNum;         //答题正确次数
    private Float correctRate;          //答题正确率
    private Float difficultyVal;        //老师给出的难度值 [0.0-10.0]
    private List<String> knowledgeList;        //知识点
    private String answer;

    private String getStringKnowledgeList(){
        return JSON.toJSONString(knowledgeList);
    }
    private String getStringIoList(){
        return JSON.toJSONString(ioList);
    }

    public static String getTag() {
        return tag;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public List<Option> getIoList() {
        return ioList;
    }

    public void setIoList(List<Option> ioList) {
        this.ioList = ioList;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(Integer correctNum) {
        this.correctNum = correctNum;
    }

    public Float getCorrectRate() {
        return correctRate;
    }

    public void setCorrectRate(Float correctRate) {
        this.correctRate = correctRate;
    }

    public Float getDifficultyVal() {
        return difficultyVal;
    }

    public void setDifficultyVal(Float difficultyVal) {
        this.difficultyVal = difficultyVal;
    }

    public List<String> getKnowledgeList() {
        return knowledgeList;
    }

    public void setKnowledgeList(List<String> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Program fill = (Program) o;
        return uuid.equals(fill.getUuid());
    }
}
