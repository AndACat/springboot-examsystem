package com.wangzhen.utils.compiler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/16 15:19
 */
public class CompilerUtil {
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static final RunInfo TIMEOUT = new RunInfo(true);
    public static RunInfo runMainMethod(String javaSourceCode, String ...runParams) {
        RunInfo runInfo;
        CustomCallable compilerAndRun = new CustomCallable(javaSourceCode,runParams);
        Future<RunInfo> future = pool.submit(compilerAndRun);
        try {
            runInfo = future.get();
        } catch (Exception e) {
            e.printStackTrace();
            return TIMEOUT;
        }
        return runInfo;
    }
}
