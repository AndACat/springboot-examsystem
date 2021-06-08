package com.wangzhen.models.problem;

import java.util.List;

/**
 * @Author wangzhen
 * @Description 题库
 * @CreateDate 2020/1/8 18:00
 */
public class QuestionBank {
    private String uuid;                            //主键
    private String name;                            //题库名称

    private List<SingleChoice> singleChoiceList;    //单选题集合
    private List<MultipleChoice> multipleChoiceList;//多选题集合
    private List<Judge> judgeList;                  //判断题集合
    private List<Short> shortProblemList;    //简答题集合
    private List<Fill> fillList;                    //填空题集合
    private List<Program> programList;              //编程题集合


    @Override
    public String toString() {
        return "QuestionBank{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", singleChoiceList=" + singleChoiceList +
                ", multipleChoiceList=" + multipleChoiceList +
                ", judgeList=" + judgeList +
                ", shortProblemList=" + shortProblemList +
                ", fillList=" + fillList +
                ", programList=" + programList +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SingleChoice> getSingleChoiceList() {
        return singleChoiceList;
    }

    public void setSingleChoiceList(List<SingleChoice> singleChoiceList) {
        this.singleChoiceList = singleChoiceList;
    }

    public List<MultipleChoice> getMultipleChoiceList() {
        return multipleChoiceList;
    }

    public void setMultipleChoiceList(List<MultipleChoice> multipleChoiceList) {
        this.multipleChoiceList = multipleChoiceList;
    }

    public List<Judge> getJudgeList() {
        return judgeList;
    }

    public void setJudgeList(List<Judge> judgeList) {
        this.judgeList = judgeList;
    }

    public List<Short> getShortProblemList() {
        return shortProblemList;
    }

    public void setShortProblemList(List<Short> shortProblemList) {
        this.shortProblemList = shortProblemList;
    }

    public List<Fill> getFillList() {
        return fillList;
    }

    public void setFillList(List<Fill> fillList) {
        this.fillList = fillList;
    }

    public List<Program> getProgramList() {
        return programList;
    }

    public void setProgramList(List<Program> programList) {
        this.programList = programList;
    }
}
