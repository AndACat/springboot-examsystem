package com.wangzhen.utils.compiler;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/16 18:07
 */
public class RunResultObj {
    RunResultObj(){}
    RunResultObj(Long runTakeTime, String runMessage, Boolean runSuccess, String runParam, String runResult){
        this.runTakeTime = runTakeTime;
        this.runMessage = runMessage;
        this.runSuccess = runSuccess;
        this.runParam = runParam;
        this.runResult = runResult;
    }
    private Long runTakeTime;
    private String runMessage;
    private Boolean runSuccess;

    private String runParam;
    private String runResult;

    public Long getRunTakeTime() {
        return runTakeTime;
    }

    public void setRunTakeTime(Long runTakeTime) {
        this.runTakeTime = runTakeTime;
    }

    public String getRunMessage() {
        return runMessage;
    }

    public void setRunMessage(String runMessage) {
        this.runMessage = runMessage;
    }

    public Boolean getRunSuccess() {
        return runSuccess;
    }

    public void setRunSuccess(Boolean runSuccess) {
        this.runSuccess = runSuccess;
    }

    public String getRunParam() {
        return runParam;
    }

    public void setRunParam(String runParam) {
        this.runParam = runParam;
    }

    public String getRunResult() {
        return runResult;
    }

    public void setRunResult(String runResult) {
        this.runResult = runResult;
    }
}
