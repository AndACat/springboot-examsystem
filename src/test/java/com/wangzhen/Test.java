package com.wangzhen;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/5 23:33
 */
public class Test {
    public static void main(String[] args) {
        IntStream.range(1,5).boxed().map(i -> {
            if(i==4){
                Arrays.asList("i,o,u".split(","))
                        .stream()
                        .sorted((t, t1) -> -1)
                        .collect(Collectors.toList())
                        .forEach(System.out :: print);
                return "";
            }
            else return "i,o,u";
        }).forEach(System.out :: println);
    }
}

//public class Main {
//    public static void main(String[] args) {
//        System.out.println("1 2 2 4 4 6");
//    }
//}
