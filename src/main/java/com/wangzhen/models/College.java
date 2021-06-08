package com.wangzhen.models;

import com.alibaba.fastjson.JSONArray;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/7 21:04
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class College {
    private String uuid;
    private String name;
    private List<String> professionList;

    public void setProfessionList(String professionList) {
        this.professionList = JSONArray.parseArray(professionList,String.class);
    }

    public static List<String> buildProfessionList(String...professions){
        List<String> list = new ArrayList<String>();
        for(int i=0;i<professions.length;++i){
            list.add(professions[i]);
        }
        return list;
    }
}
