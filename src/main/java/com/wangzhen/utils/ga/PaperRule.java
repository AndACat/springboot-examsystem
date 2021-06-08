package com.wangzhen.utils.ga;

import com.wangzhen.models.PaperStrategy;
import com.wangzhen.utils.Utils;

import java.sql.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/30 14:19
 */
public class PaperRule {
    public PaperRule(PaperStrategy paperStrategy, String teacher_account,Integer populationSize,Float minDifficultyVal,
                     Float maxDifficultyVal,Integer populationInitMaxRunCount, Float mutationRate,Boolean elitism,Integer tournamentSize,Boolean copulation,
                     List<String> singleChoiceKnowledgeList, List<String> multipleChoiceKnowledgeList,
                     List<String> programKnowledgeList, List<String> fillKnowledgeList,
                     List<String> shortKnowledgeList, List<String> judgeKnowledgeList) {
        this.paperStrategy = paperStrategy;
        this.teacher_account = teacher_account;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.elitism = elitism;
        this.copulation = copulation;
        this.tournamentSize = tournamentSize;
        this.singleChoiceKnowledgeList = singleChoiceKnowledgeList;
        this.multipleChoiceKnowledgeList = multipleChoiceKnowledgeList;
        this.programKnowledgeList = programKnowledgeList;
        this.fillKnowledgeList = fillKnowledgeList;
        this.shortKnowledgeList = shortKnowledgeList;
        this.judgeKnowledgeList = judgeKnowledgeList;
        this.minDifficultyVal = minDifficultyVal;
        this.maxDifficultyVal = maxDifficultyVal;
        this.populationInitMaxRunCount = populationInitMaxRunCount;
        this.createTime = new Date(System.currentTimeMillis());
        this.paperUuid = Utils.randomUuid();
        random = new Random(System.currentTimeMillis());
    }
    /**
     * 最低难度值
     */
    private Float minDifficultyVal;
    /**
     * 最高难度值
     */
    private Float maxDifficultyVal;
    /**
     * 组成策略
     */
    private PaperStrategy paperStrategy;
    /**
     * 教师账户
     */
    private String teacher_account;
    private Integer populationSize;
    /**
     * 种群初始化时最大迭代数量
     */
    private Integer populationInitMaxRunCount;
    /**
     * 规则对应的考试id
     */
    private String paperUuid;
    /**
     * 变异概率
     */
    private Float mutationRate = 0.085f;
    /**
     * 精英主义
     */
    private Boolean elitism = true;
    /**
     * 淘汰数组大小
     */
    private Integer tournamentSize = 5;
    /**
     * 是否种群交配
     */
    private Boolean copulation = null;
    private Random random = null;
    /**
     * 试卷包含的知识点id
     */
    private List<String> singleChoiceKnowledgeList = null;
    private List<String> multipleChoiceKnowledgeList = null;
    private List<String> programKnowledgeList = null;
    private List<String> fillKnowledgeList = null;
    private List<String> shortKnowledgeList = null;
    private List<String> judgeKnowledgeList = null;
    /**
     * 规则创建时间
     */
    private Date createTime;

    public Float getMinDifficultyVal() {
        return minDifficultyVal;
    }

    public void setMinDifficultyVal(Float minDifficultyVal) {
        this.minDifficultyVal = minDifficultyVal;
    }

    public Float getMaxDifficultyVal() {
        return maxDifficultyVal;
    }

    public void setMaxDifficultyVal(Float maxDifficultyVal) {
        this.maxDifficultyVal = maxDifficultyVal;
    }

    public PaperStrategy getPaperStrategy() {
        return paperStrategy;
    }

    public void setPaperStrategy(PaperStrategy paperStrategy) {
        this.paperStrategy = paperStrategy;
    }

    public String getTeacher_account() {
        return teacher_account;
    }

    public void setTeacher_account(String teacher_account) {
        this.teacher_account = teacher_account;
    }

    public Integer getPopulationInitMaxRunCount() {
        return populationInitMaxRunCount;
    }

    public void setPopulationInitMaxRunCount(Integer populationInitMaxRunCount) {
        this.populationInitMaxRunCount = populationInitMaxRunCount;
    }

    public String getPaperUuid() {
        return paperUuid;
    }

    public void setPaperUuid(String paperUuid) {
        this.paperUuid = paperUuid;
    }

    public Float getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(Float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Boolean getElitism() {
        return elitism;
    }

    public void setElitism(Boolean elitism) {
        this.elitism = elitism;
    }

    public Integer getTournamentSize() {
        return tournamentSize;
    }

    public void setTournamentSize(Integer tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public List<String> getSingleChoiceKnowledgeList() {
        return singleChoiceKnowledgeList;
    }

    public void setSingleChoiceKnowledgeList(List<String> singleChoiceKnowledgeList) {
        this.singleChoiceKnowledgeList = singleChoiceKnowledgeList;
    }

    public List<String> getMultipleChoiceKnowledgeList() {
        return multipleChoiceKnowledgeList;
    }

    public void setMultipleChoiceKnowledgeList(List<String> multipleChoiceKnowledgeList) {
        this.multipleChoiceKnowledgeList = multipleChoiceKnowledgeList;
    }

    public List<String> getProgramKnowledgeList() {
        return programKnowledgeList;
    }

    public void setProgramKnowledgeList(List<String> programKnowledgeList) {
        this.programKnowledgeList = programKnowledgeList;
    }

    public List<String> getFillKnowledgeList() {
        return fillKnowledgeList;
    }

    public void setFillKnowledgeList(List<String> fillKnowledgeList) {
        this.fillKnowledgeList = fillKnowledgeList;
    }

    public List<String> getShortKnowledgeList() {
        return shortKnowledgeList;
    }

    public void setShortKnowledgeList(List<String> shortKnowledgeList) {
        this.shortKnowledgeList = shortKnowledgeList;
    }

    public List<String> getJudgeKnowledgeList() {
        return judgeKnowledgeList;
    }

    public void setJudgeKnowledgeList(List<String> judgeKnowledgeList) {
        this.judgeKnowledgeList = judgeKnowledgeList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Boolean getCopulation() {
        return copulation;
    }

    public void setCopulation(Boolean copulation) {
        this.copulation = copulation;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }
}
