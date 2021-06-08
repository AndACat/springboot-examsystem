package com.wangzhen.models.problem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author wangzhen
 * @Description 填空题
 * @CreateDate 2020/1/8 18:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Fill implements Problem {
    private static final String tag = "FILL";
    private String uuid;                //主键
    private String problem;             //问题内容//这是一个#{人},她很#{美}
    private List<String> answer;              //参考答案
    private String videoPath;               //解析视频地址
    private Integer useNum;             //参与成功组卷的次数
    private Integer allNum;             //总答题次数
    private Integer correctNum;         //答题正确次数
    private String analysis;
    private Float correctRate;          //答题正确率
    private Float difficultyVal;        //老师给出的难度值 [0.0-10.0]
    private List<String> knowledgeList;        //知识点

    private String getStringKnowledgeList(){
        return JSON.toJSONString(knowledgeList);
    }
    private String getStringAnswer(){
        String s = JSON.toJSONString(answer);
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fill fill = (Fill) o;
        return uuid.equals(fill.getUuid());
    }
}
