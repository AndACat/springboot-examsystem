package com.wangzhen.utils;

import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/22 14:34
 */
public class ProblemNodeUtil {
    public ProblemNode firstProblemNode = null;
    private ProblemNode lastProblemNode = null;
    private ProblemNode currentProblemNode = null;
    private ProblemNode nextProblemNode = null;
    private Integer totalProblemIdx = 1;
    private List<ProblemNode> problemNodeList = new ArrayList<>();
//    public void init(){
//        if(firstProblemNode == null){
//            firstProblemNode = new ProblemNode();
//            nextProblemNode = firstProblemNode;
//        }
//    }
    public void addNewNode(String problemType, Object problemData,Integer problemIdx){
        currentProblemNode = new ProblemNode(problemType,problemData,totalProblemIdx++,problemIdx);
        problemNodeList.add(currentProblemNode);
        if(firstProblemNode == null){//保存根节点
            firstProblemNode = currentProblemNode;
        }
        if(lastProblemNode != null){
            currentProblemNode.lastProblemNode = lastProblemNode;
            lastProblemNode.nextProblemNode = currentProblemNode;
        }
        lastProblemNode = currentProblemNode;
        currentProblemNode = null;
    }
    public ProblemNode getProblemNode(Integer problemIdx){
        if(problemNodeList.size() >= problemIdx){
            return problemNodeList.get(problemIdx-1);
        }
        return null;
    }

    public boolean canOverExam(){
        boolean canOverExam = true;
        boolean breakCurrentWhile = false;
        ProblemNode next = firstProblemNode;
        while(next != null){
            switch (next.problemType){
                case "单选题":{
                    SingleChoice singleChoice = (SingleChoice) next.problemData;
                    if(singleChoice.getAnswer() == null || singleChoice.getAnswer().trim().equals("")){
                        canOverExam = false;
                        breakCurrentWhile = true;
                    }
                    break;
                }
                case "多选题":{
                    MultipleChoice multipleChoice = (MultipleChoice) next.problemData;
                    if(multipleChoice.getAnswer() == null || multipleChoice.getAnswer().size() == 0){
                        canOverExam = false;
                        breakCurrentWhile = true;
                    }
                    break;
                }
                case "填空题":{
                    Fill fill = (Fill) next.problemData;
                    if(fill.getAnswer() == null || fill.getAnswer().size() == 0){
                        canOverExam = false;
                        breakCurrentWhile = true;
                    }
                    break;
                }
                case "简答题":{
                    Short aShort = (Short) next.problemData;
                    if(aShort.getAnswer() == null || aShort.getAnswer().trim().equals("")){
                        canOverExam = false;
                        breakCurrentWhile = true;
                    }
                    break;
                }
                case "判断题":{
                    Judge judge = (Judge)next.problemData;
                    if(judge.getAnswer() == null){
                        canOverExam = false;
                        breakCurrentWhile = true;
                    }
                    break;
                }
                case "编程题":{
                    Program program = (Program)next.problemData;
                    if(program.getAnswer() == null || program.getAnswer().trim().equals("")){
                        canOverExam = false;
                        breakCurrentWhile = true;
                    }
//                    if(program.get)
//                List<Judge> judgeList = (List<Judge>) paperInfo.get(problemType);
//                judgeList.get(problemIdx).setAnswer((Boolean) answer);
                    break;
                }
            }
            next = next.nextProblemNode;
            if(breakCurrentWhile) break;
        }


        return canOverExam;
    }
}
