package com.wangzhen.models.problem;

import com.alibaba.fastjson.JSONArray;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author wangzhen
 * @Description  单选题
 * @CreateDate 2020/1/8 18:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SingleChoice implements Problem{
    private String uuid;                //主键
    private String problem;             //问题内容
    private String choice_a;            //选项—a 的内容
    private String choice_b;
    private String choice_c;
    private String choice_d;
    private String choice_e;
    private String choice_f;
    private String choice_g;
    private String choice_h;
    private String answer;              //答案选项 [a-h | A-H]
    private String analysis;             //中文解析
    private String videoPath;           //解析视频地址
    private Integer useNum;             //参与成功组卷的次数
    private Integer allNum;             //总答题次数
    private Integer correctNum;         //答题正确次数
    private Float correctRate;          //答题正确率
    private Float difficultyVal;        //老师给出的难度值 [0.0-10.0]
    private List<String> knowledgeList;        //知识点

    @Override
    public boolean equals(Object o) {
        return ((SingleChoice)o).getUuid().equals(this.getUuid());
    }
}