package com.wangzhen.utils.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/16 15:19
 */
public class RunInfo {

    RunInfo(){}
    RunInfo(Boolean timeOut){
        this.timeOut = timeOut;
        this.compilerMessage = "编译超时";
    }

    //true:代表超时
    private Boolean timeOut;

    private Long compilerTakeTime;
    private String compilerMessage;
    private Boolean compilerSuccess;

    private List<RunResultObj> runResultObjList = new ArrayList<>();

    public void addRunResult(Long runTakeTime,String runMessage,Boolean runSuccess,String runParam,String runResult){
        runResultObjList.add(new RunResultObj(runTakeTime,runMessage,runSuccess,runParam,runResult));
    }
    public void addRunResult(RunResultObj runResultObj){
        runResultObjList.add(runResultObj);
    }


    public Boolean getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Boolean timeOut) {
        this.timeOut = timeOut;
    }

    public Long getCompilerTakeTime() {
        return compilerTakeTime;
    }

    public void setCompilerTakeTime(Long compilerTakeTime) {
        this.compilerTakeTime = compilerTakeTime;
    }

    public String getCompilerMessage() {
        return compilerMessage;
    }

    public void setCompilerMessage(String compilerMessage) {
        this.compilerMessage = compilerMessage;
    }

    public Boolean getCompilerSuccess() {
        return compilerSuccess;
    }

    public void setCompilerSuccess(Boolean compilerSuccess) {
        this.compilerSuccess = compilerSuccess;
    }

    public List<RunResultObj> getRunResultObjList(){
        return runResultObjList;
    }


}


