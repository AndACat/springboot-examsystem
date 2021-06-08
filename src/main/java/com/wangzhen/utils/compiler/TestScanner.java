package com.wangzhen.utils.compiler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/15 23:26
 */
public class TestScanner {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String code = "import java.util.Scanner;" +
                "public class Main{" +
                "    public static void main(String[] args) {" +
                "        Scanner scanner = new Scanner(System.in);" +
                "        int a = scanner.nextInt();" +
                "        System.out.println(a);" +
                "    }" +
                "}";
//        CustomJavaCompiler customJavaCompiler = new CustomJavaCompiler(code);
//        customJavaCompiler.compiler();
//        customJavaCompiler.runMainMethod("222 0009");
//        System.out.println(customJavaCompiler.getRunResult());

        RunInfo runInfo = CompilerUtil.runMainMethod(code, "9l", "8", "7");
        System.out.println(runInfo.getRunResultObjList().size());
    }
}


