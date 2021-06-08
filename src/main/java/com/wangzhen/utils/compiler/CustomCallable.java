package com.wangzhen.utils.compiler;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/16 15:21
 */
public class CustomCallable implements Callable<RunInfo>{
    private static final String importStr = "import java.util.*;";
    private String sourceCode;
    private String[] runParams;

    public CustomCallable(String sourceCode,String ...runParams) {
        this.sourceCode = sourceCode;
        this.runParams = runParams;
    }

    //方案2
    @Override
    public RunInfo call() throws Exception {
        RunInfo runInfo = new RunInfo();
        Thread t1 = new Thread(() -> this.realCall(runInfo));
        t1.start();
        try {
            t1.join(20000); //等待20秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //不管有没有正常执行完成，强制停止t1
        t1.stop();
        return runInfo;
    }

    private void realCall(RunInfo runInfo) {
        CustomJavaCompiler compiler = new CustomJavaCompiler(importStr+sourceCode);
        RunResultObj runResultObj = null;
        if (compiler.compiler()) {
            runInfo.setCompilerSuccess(true);
            try {
                for (String runParam : runParams) {
                    runResultObj = new RunResultObj();
                    //开始运行时间
                    Long startRunTime = System.currentTimeMillis();
                    //运行代码
                    String runResult = compiler.runMainMethod(runParam);
                    //终止运行时间
                    Long stopRunTime = System.currentTimeMillis();
                    runResultObj.setRunParam(runParam);
                    runResultObj.setRunSuccess(true);
                    runResultObj.setRunTakeTime(stopRunTime - startRunTime);
                    runResultObj.setRunResult(runResult);
                    runInfo.addRunResult(runResultObj);
                }
            } catch (InvocationTargetException e) {
                //反射调用异常了,是因为超时的线程被强制stop了
                if ("java.lang.ThreadDeath".equalsIgnoreCase(e.getCause().toString())) {
                    runResultObj.setRunMessage("运行超时");
                    return;
                }
            } catch (Exception e) {
                runResultObj.setRunSuccess(false);
                runResultObj.setRunMessage(e.getMessage());
            }
        } else {
            //编译失败
            runInfo.setCompilerSuccess(false);
        }
        runInfo.setCompilerTakeTime(compiler.getCompilerTakeTime());
        runInfo.setCompilerMessage(compiler.getCompilerMessage());
        //走到这一步代表没有超时
        runInfo.setTimeOut(false);
    }
}
