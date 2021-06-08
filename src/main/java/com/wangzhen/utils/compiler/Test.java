package com.wangzhen.utils.compiler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/15 23:26
 */
public class Test {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException, IllegalAccessException, InvocationTargetException {
        String code = "public class Abc{" +
                "           public static void main(String[] args) {" +
                "               System.out.println(\"在线编译运行。。。。。。\");" +
                "try {\n" +
                "            Thread.sleep(3000);\n" +
                "        } catch (InterruptedException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }" +
                "           }" +
                "      }";

//        CustomJavaCompiler customJavaCompiler = new CustomJavaCompiler(code);
//        customJavaCompiler.compiler();
//        customJavaCompiler.runMainMethod("dddd");
//        System.out.println(customJavaCompiler.getRunResult());
    }
}



