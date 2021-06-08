package com.wangzhen.models;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/29 0:55
 */
@Setter@Getter
public class ProblemStrategy {
    public ProblemStrategy() {}

    public ProblemStrategy(String problemType, int problemNum, float problemScore) {
        this.problemType = problemType;
        this.problemNum = problemNum;
        this.problemScore = problemScore;
        this.problemAllScore = problemNum * problemScore;
    }

    private String problemType;// = ProblemType.FILL.name();
    private int problemNum;    //题目数量
    private float problemScore;//每道题分数
    private float problemAllScore;//这道大题总分
}
