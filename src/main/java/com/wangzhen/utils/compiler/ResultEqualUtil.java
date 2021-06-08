package com.wangzhen.utils.compiler;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/3 11:45
 */
public class ResultEqualUtil {
    public static boolean equals(String target,String source){
        String t = target.trim().replace("\n", "").replace("\t", "");
        String s = source.trim().replace("\n", "").replace("\t", "");
        return t.equals(s);
    }
}
