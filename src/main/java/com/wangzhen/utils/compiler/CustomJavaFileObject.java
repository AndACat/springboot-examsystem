package com.wangzhen.utils.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @Author wangzhen
 * @Description 自定义一个字符串的源码对象
 * @CreateDate 2020/3/16 16:12
 */
public class CustomJavaFileObject extends SimpleJavaFileObject {
    //等待编译的源码字段
    private String source;
    //存放编译后的字节码
    private ByteArrayOutputStream outPutStream  = new ByteArrayOutputStream();
    //java源代码 => StringJavaFileObject对象 的时候使用
    public CustomJavaFileObject(String className, String source) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    //字符串源码会调用该方法
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return source;
    }
}
