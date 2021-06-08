package com.wangzhen.models;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
/**
 * @Author wangzhen
 * @Description 试卷策略
 * @CreateDate 2020/2/29 0:35
 */
@Setter
@Getter
public class PaperStrategy {
    private String uuid;
    private String paperStrategyName;//策略名
    private float allScore;//试卷总分
    private List<ProblemStrategy> problemStrategyList;

    public void setProblemStrategyList(String problemStrategyList) {
        this.problemStrategyList = JSON.parseArray(problemStrategyList,ProblemStrategy.class);
    }

    public String getProblemStrategyList() {
        return JSON.toJSONString(problemStrategyList);
    }
    public List<ProblemStrategy> problemStrategyList(){
        return problemStrategyList;
    }
}
