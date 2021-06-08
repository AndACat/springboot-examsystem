package com.wangzhen.configuration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 18:38
 */
public class PublicSession {
    private static Map<String,Object> map = new Hashtable<>();
    public static void add(String key,Object val){
        map.put(key,val);
    }
    public static Object getVal(String key){
        return map.get(key);
    }
    public static boolean containsKey(String key){
        return map.containsKey(key);
    }
    public static boolean containsVal(Object val){
        return map.containsValue(val);
    }
}
