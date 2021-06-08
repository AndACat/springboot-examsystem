package com.wangzhen.utils.compiler;

/**
 * @Author wangzhen
 * @Description 自定义类加载器, 用来加载动态的字节码
 * @CreateDate 2020/3/16 16:08
 */
public class CustomClassLoader extends ClassLoader{
    private ByteJavaFileObject fileObject;
    CustomClassLoader(ByteJavaFileObject fileObject){
        this.fileObject = fileObject;
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (fileObject != null) {
            byte[] bytes = fileObject.getCompiledBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        try {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            return super.findClass(name);
        }
    }
}
