package com.wangzhen.utils.compiler;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/16 19:15
 */
public class CustomJavaFileManage extends ForwardingJavaFileManager {
    private ByteJavaFileObject javaFileObject;
    CustomJavaFileManage(JavaFileManager fileManager) {
        super(fileManager);
    }

    //获取输出的文件对象，它表示给定位置处指定类型的指定类。
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        javaFileObject = new ByteJavaFileObject(className, kind);
        return javaFileObject;
    }

    public ByteJavaFileObject get(){
        return javaFileObject;
    }
}
