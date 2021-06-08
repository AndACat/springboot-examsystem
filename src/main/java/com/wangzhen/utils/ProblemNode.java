package com.wangzhen.utils;

import com.wangzhen.controllers.student.StudentHomeController;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/22 14:33
 */
public class ProblemNode {
    public ProblemNode() {
    }
    public ProblemNode(String problemType, Object problemData, Integer totalProblemIdx,Integer problemIdx) {
        this.problemType = problemType;
        this.problemData = problemData;
        this.totalProblemIdx = totalProblemIdx;
        this.problemIdx = problemIdx;
    }

    public String problemType = null;
    public Object problemData = null;
    public Integer problemIdx = null;
    public Integer totalProblemIdx = null;
    public ProblemNode lastProblemNode = null;
    public ProblemNode nextProblemNode = null;
}
