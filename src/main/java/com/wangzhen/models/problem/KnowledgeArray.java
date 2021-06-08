package com.wangzhen.models.problem;

import com.alibaba.fastjson.JSONArray;
import lombok.*;

import java.util.List;

/**
 * @Author wangzhen
 * @Description 知识点的集合
 * @CreateDate 2020/2/10 21:39
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KnowledgeArray {
    private String uuid;
    private String name;//知识点主题
    private List<String> knowledgeList;//知识点主题里面的一堆小点
    public void setKnowledgeList(String knowledgeList) {
        this.knowledgeList = JSONArray.parseArray(knowledgeList,String.class);
    }
}
