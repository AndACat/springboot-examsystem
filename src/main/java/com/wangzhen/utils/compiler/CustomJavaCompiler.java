package com.wangzhen.utils.compiler;
import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


/**
 * @Author wangzhen
 * @Description 自定义java编译器
 * @CreateDate 2020/3/15 22:44
 */
public class CustomJavaCompiler {
    //类全名
    private static final String fullClassName = "Main";
    private String sourceCode;
    private CustomJavaFileObject customJavaFileObject = null;
    //获取java的编译器
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    //存放编译过程中输出的信息
    private DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
    private long compilerTakeTime = 0;
    private CustomJavaFileManage customJavaFileManage;


    public CustomJavaCompiler(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * 编译字符串源代码,编译失败在 diagnosticsCollector 中获取提示信息
     *
     * @return true:编译成功 false:编译失败
     */
    public boolean compiler() {
        long startTime = System.currentTimeMillis();
        //标准的内容管理器,更换成自己的实现，覆盖部分方法
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
        //构造源代码对象
        CustomJavaFileObject customJavaFileObject = new CustomJavaFileObject(fullClassName, sourceCode);
        customJavaFileManage = new CustomJavaFileManage(standardFileManager);
        //获取一个编译任务
        JavaCompiler.CompilationTask task = compiler.getTask(null, customJavaFileManage, diagnosticsCollector, null, null, Arrays.asList(customJavaFileObject));
        //设置编译耗时
        compilerTakeTime = System.currentTimeMillis() - startTime;
        return task.call();
    }

    /**
     * 执行main方法，重定向System.out.print
     */
    public String runMainMethod(String params)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        PrintStream out = System.out;
        InputStream in = System.in;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
//            PrintStream printStream = new PrintStream("tem.text"); //输出到文件
            ByteArrayInputStream inputStream = new ByteArrayInputStream(params.getBytes());
            //设置输入
            System.setIn(inputStream);
            //设置输出
            System.setOut(printStream);
            CustomClassLoader scl = new CustomClassLoader(customJavaFileManage.get());
            Class<?> aClass = scl.findClass(fullClassName);
            Method main = aClass.getMethod("main", String[].class);
            if(main == null){
                return "";
            }
            Object[] pars = new Object[]{1};
            pars[0] = new String[]{};
            //调用main方法E
            main.invoke(null, pars);
            //设置打印输出的内容
            return new String(outputStream.toByteArray(), "utf-8");
        }catch (Exception e){
        }finally {
            //还原默认打印的对象
            System.setOut(out);
            System.setIn(in);
        }
        return "";
    }

    /**
     * @return 编译信息(错误 警告)
     */
    public String getCompilerMessage() {
        StringBuilder sb = new StringBuilder();
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector.getDiagnostics();
        for (Diagnostic diagnostic : diagnostics) {
            sb.append(diagnostic.toString()).append("\r\n");
        }
        return sb.toString();
    }




    public long getCompilerTakeTime() {
        return compilerTakeTime;
    }


    /**
     * 获取类的全名称
     *
     * @param sourceCode 源码
     * @return 类的全名称
     */
//    public static String getFullClassName(String sourceCode) {
//        String className = "";
//        Pattern pattern = Pattern.compile("package\\s+\\S+\\s*;");
//        Matcher matcher = pattern.matcher(sourceCode);
//        if (matcher.find()) {
//            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
//        }
//
//        pattern = Pattern.compile("class\\s+\\S+\\s*+\\{");
//        matcher = pattern.matcher(sourceCode);
//        if (matcher.find()) {
//            className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
//        }
//        return className;
//    }
}
