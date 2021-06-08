package com.wangzhen.utils.ga;
import com.wangzhen.configuration.MyWebAppConfig;
import com.wangzhen.models.Paper;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.services.teacher.problemservice.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.SingletonMap;

import java.util.*;

/**
 * @Author wangzhen
 * @Description 试卷的包装类
 * @CreateDate 2020/3/30 13:44
 */
@SuppressWarnings("all")
public class PaperPackage {
    private SingleChoiceService singleChoiceService = MyWebAppConfig.context.getBean(SingleChoiceService.class);
    private MultipleChoiceService multipleChoiceService = MyWebAppConfig.context.getBean(MultipleChoiceService.class);
    private FillService fillService = MyWebAppConfig.context.getBean(FillService.class);
    private ShortService shortService = MyWebAppConfig.context.getBean(ShortService.class);
    private JudgeService judgeService = MyWebAppConfig.context.getBean(JudgeService.class);
    private ProgramService programService = MyWebAppConfig.context.getBean(ProgramService.class);
    public ProblemException e = null;
    public PaperPackage(){}
    public PaperPackage(Paper paper,boolean init, PaperRule paperRule) throws ProblemException {
        List<ProblemStrategy> problemStrategyList = paper.getPaperStrategy().problemStrategyList();
        for (ProblemStrategy problemStrategy : paperRule.getPaperStrategy().problemStrategyList()) {
            Integer problemNum = problemStrategy.getProblemNum();
            switch (problemStrategy.getProblemType()){
                case "单选题":{
                    if(init){
                        List<SingleChoice> singleChoiceList = singleChoiceService.selectMyAllSingleChoiceWithKnowledgeMap(
                                paperRule.getTeacher_account(), paperRule.getSingleChoiceKnowledgeList());
                        if(singleChoiceList.size()  < problemNum){
                            throw new ProblemException("单选题数量不够,要求"+problemNum+"道,但仅有"+singleChoiceList.size()+"道;\n");
                        }
                        paper.getPaperInfo().put("singleChoiceList",getPaperProblemList(singleChoiceList,problemNum));
                    }else{
                        paper.getPaperInfo().put("singleChoiceList",new ArrayList<SingleChoice>(problemNum));
                    }
                    break;
                }
                case "多选题":{
                    if(init){
                        List<MultipleChoice> multipleChoiceList = multipleChoiceService.selectMyAllMultipleChoiceWithKnowledgeMap(
                                paperRule.getTeacher_account(),paperRule.getMultipleChoiceKnowledgeList());
                        if(multipleChoiceList.size()  < problemNum){
                            throw new ProblemException("多选题数量不够,要求"+problemNum+"道,但仅有"+multipleChoiceList.size()+"道;\n");
                        }
                        paper.getPaperInfo().put("multipleChoiceList",getPaperProblemList(multipleChoiceList,problemNum));
                    }else{
                        paper.getPaperInfo().put("multipleChoiceList",new ArrayList<MultipleChoice>(problemNum));
                    }
                    break;
                }
                case "填空题":{
                    if(init){
                        List<Fill> fillList = fillService.selectMyAllFillWithKnowledgeMap(
                                paperRule.getTeacher_account(),paperRule.getFillKnowledgeList());
                        if(fillList.size()  < problemNum){
                            throw new ProblemException("填空题数量不够,要求"+problemNum+"道,但仅有"+fillList.size()+"道;\n");
                        }
                        paper.getPaperInfo().put("fillList",getPaperProblemList(fillList,problemNum));
                    }else{
                        paper.getPaperInfo().put("fillList",new ArrayList<Fill>());
                    }
                    break;
                }
                case "简答题":{
                    if(init){
                        List<Short> shortList = shortService.selectMyAllShortWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getShortKnowledgeList());
                        if(shortList.size()  < problemNum){
                            throw new ProblemException("简答题数量不够,要求"+problemNum+"道,但仅有"+shortList.size()+"道;\n");
                        }
                        paper.getPaperInfo().put("shortList",getPaperProblemList(shortList,problemNum));
                    }else {
                        paper.getPaperInfo().put("shortList",new ArrayList<Short>(problemNum));
                    }
                    break;
                }
                case "判断题":{
                    if(init){
                        List<Judge> judgeList = judgeService.selectMyAllJudgeWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getJudgeKnowledgeList());
                        if(judgeList.size()  < problemNum){
                            throw new ProblemException("判断题数量不够,要求"+problemNum+"道,但仅有"+judgeList.size()+"道;\n");
                        }
                        paper.getPaperInfo().put("judgeList",getPaperProblemList(judgeList,problemNum));
                    }else{
                        paper.getPaperInfo().put("judgeList",new ArrayList<Judge>());
                    }
                    break;
                }
                case "编程题":{
                    if(init){
                        List<Program> programList = programService.selectMyAllShortWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getProgramKnowledgeList());
                        if(programList.size()  < problemNum){
                            throw new ProblemException("编程题数量不够,要求"+problemNum+"道,但仅有"+programList.size()+"道;\n");
                        }
                        paper.getPaperInfo().put("programList",getPaperProblemList(programList,problemNum));
                    }else{
                        paper.getPaperInfo().put("programList",new ArrayList<Program>());
                    }
                    break;
                }
            }
        }
        this.paper = paper;
        if(init){
            this.setDifficulty(paperRule);
        }
    }

    public void setDifficulty(PaperRule paperRule) {
        List<ProblemStrategy> problemStrategyList = paperRule.getPaperStrategy().problemStrategyList();
        Float _difficulty = 0f;
        int problemAllSize = 0;
        float totalScore = 0;
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();
        for (String problemType : paperInfo.keySet()) {
            List<Object> objectList = (List<Object>) paperInfo.get(problemType);
            for (Object o : objectList) {
                if(o instanceof SingleChoice){
                    float problemScore = 0;
                    for (ProblemStrategy problemStrategy : problemStrategyList) {
                        if(problemStrategy.getProblemType().equals("单选题")){
                            problemScore = problemStrategy.getProblemScore();
                        }
                    }
                    totalScore+=problemScore;
                    _difficulty += ((SingleChoice) o).getDifficultyVal() * problemScore;problemAllSize++;
                }
                else if(o instanceof MultipleChoice){
                    float problemScore = 0;
                    for (ProblemStrategy problemStrategy : problemStrategyList) {
                        if(problemStrategy.getProblemType().equals("多选题")){
                            problemScore = problemStrategy.getProblemScore();
                        }
                    }
                    totalScore+=problemScore;
                    _difficulty += ((MultipleChoice) o).getDifficultyVal() * problemScore;problemAllSize++;
                }
                else if(o instanceof Fill){
                    float problemScore = 0;
                    for (ProblemStrategy problemStrategy : problemStrategyList) {
                        if(problemStrategy.getProblemType().equals("填空题")){
                            problemScore = problemStrategy.getProblemScore();
                        }
                    }
                    totalScore+=problemScore;
                    _difficulty += ((Fill) o).getDifficultyVal() * problemScore;problemAllSize++;
                }
                else if(o instanceof Short){
                    float problemScore = 0;
                    for (ProblemStrategy problemStrategy : problemStrategyList) {
                        if(problemStrategy.getProblemType().equals("简答题")){
                            problemScore = problemStrategy.getProblemScore();
                        }
                    }
                    totalScore+=problemScore;
                    _difficulty += ((Short) o).getDifficultyVal() * problemScore;problemAllSize++;
                }
                else if(o instanceof Judge){
                    float problemScore = 0;
                    for (ProblemStrategy problemStrategy : problemStrategyList) {
                        if(problemStrategy.getProblemType().equals("判断题")){
                            problemScore = problemStrategy.getProblemScore();
                        }
                    }
                    totalScore+=problemScore;
                    _difficulty += ((Judge) o).getDifficultyVal() * problemScore;problemAllSize++;
                }
                else if(o instanceof Program){
                    float problemScore = 0;
                    for (ProblemStrategy problemStrategy : problemStrategyList) {
                        if(problemStrategy.getProblemType().equals("编程题")){
                            problemScore = problemStrategy.getProblemScore();
                        }
                    }
                    totalScore+=problemScore;
                    _difficulty += ((Program) o).getDifficultyVal() * problemScore;problemAllSize++;
                }
            }
        }

        this.difficulty = _difficulty / totalScore;
    }

    /**
     * 包装的试卷
     */
    private Paper paper = null;
    /**
     * 适应度
     */
    private Float adaptationDegree = 0f;
    /**
     * 知识点覆盖率
     */
    private Float knowledgeCoverage = 0f;
    /**
     * 试卷难度系数
     */
    private Float difficulty = 0f;

    /**
     * 计算试卷总分
     */
    public Float getTotalScore() {
        if(paper.getTotalScore() == 0){
            Float totalScore = paper.getPaperStrategy() == null ? 0 : paper.getPaperStrategy().getAllScore();
            paper.setTotalScore(totalScore);
        }
        return paper.getTotalScore();
    }
    /**
     * 计算试卷个体难度系数 计算公式： 每题难度*分数求和除总分
     */
    public Float getDifficultyVal() {
        if (difficulty == 0) {
            Float _difficulty = 0f;
            int problemAllSize = 0;
            LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();
            if(paperInfo == null) return 0f;
            for (String problemType : paperInfo.keySet()) {
                List<Object> objectList = (List<Object>) paperInfo.get(problemType);
                for (Object o : objectList) {
                    if(o instanceof SingleChoice){
                        _difficulty += ((SingleChoice) o).getDifficultyVal();problemAllSize++;
                    }
                    else if(o instanceof MultipleChoice){
                        _difficulty += ((MultipleChoice) o).getDifficultyVal();problemAllSize++;
                    }
                    else if(o instanceof Fill){
                        _difficulty += ((Fill) o).getDifficultyVal();problemAllSize++;
                    }
                    else if(o instanceof Short){
                        _difficulty += ((Short) o).getDifficultyVal();problemAllSize++;
                    }
                    else if(o instanceof Judge){
                        _difficulty += ((Judge) o).getDifficultyVal();problemAllSize++;
                    }
                    else if(o instanceof Program){
                        _difficulty += ((Program) o).getDifficultyVal();problemAllSize++;
                    }
                }
            }
//            Float totalScore = getTotalScore();
            difficulty = _difficulty / problemAllSize;//(totalScore == 0f ? 1f : totalScore);
        }
        return difficulty;
    }
    /**
     * 获取试题数量
     *
     * @return
     */
    public int getProblemSize() {
        int problemSize = 0;
        for (Map.Entry<String, Object> stringObjectEntry : paper.getPaperInfo().entrySet()) {
            List<Object> value = (List<Object>) stringObjectEntry.getValue();
            problemSize += value.size();
        }
        return problemSize;
    }

    /**
     * 计算个体适应度 公式为：f(max,min,difficultyVal) = 1-|((max+min)-2*difficultyVal)/(max-min)|
     * 其中 max:试卷最大难度值 前端赋值
     * 其中 min:试卷最小难度值 前端赋值
     * 其中 difficultyVal:试卷平均难度值 自动生成
     */
    public void setAdaptationDegree(PaperRule rule) {
        float max = rule.getMaxDifficultyVal();
        float min = rule.getMinDifficultyVal();
//        adaptationDegree = 1 - Math.abs( (max+min) - 2 * getDifficultyVal() / (max-min));
//        adaptationDegree = 1 - Math.abs( ((max-min) - 2 * getDifficultyVal()) / (max-min));
        float di = getDifficultyVal();
        float adaptationDegree = 0f;
        if(di <= min){
            adaptationDegree = (di-min)/(max -min);
        }
        if(di >= max){
            adaptationDegree = (max-di)/(max-min);
        }
        float avg = (max+min)/2;
        if(avg < di && di < max){
            adaptationDegree = (max-di)/(max-avg);
        }
        if(min < di && di < avg){
            adaptationDegree = (di-min)/(max-avg);
        }
        if(di == avg){
            adaptationDegree = 1;
        }
        this.adaptationDegree = adaptationDegree;
    }
    /**
     * 返回试卷的适应度
     */
    public Float getAdaptationDegree() {
        return adaptationDegree;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public void setAdaptationDegree(Float adaptationDegree) {
        this.adaptationDegree = adaptationDegree;
    }

    public Float getKnowledgeCoverage() {
        return knowledgeCoverage;
    }

    public void setKnowledgeCoverage(Float knowledgeCoverage) {
        this.knowledgeCoverage = knowledgeCoverage;
    }

    public Float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
    }

    public void setProblem(Object object,PaperRule paperRule) {//从1开始
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();
        if(object instanceof SingleChoice){
            List<SingleChoice> singleChoiceList = (List<SingleChoice>)paperInfo.get("singleChoiceList");
            singleChoiceList.add((SingleChoice) object);
        }
        else if(object instanceof MultipleChoice){
            List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>)paperInfo.get("multipleChoiceList");
            multipleChoiceList.add((MultipleChoice) object);
        }
        else if(object instanceof Fill){
            List<Fill> fillList = (List<Fill>)paperInfo.get("fillList");
            fillList.add((Fill) object);
        }
        else if(object instanceof Judge){
            List<Judge> judgeList = (List<Judge>)paperInfo.get("judgeList");
            judgeList.add((Judge) object);
        }
        else if(object instanceof Short){
            List<Short> shortList = (List<Short>)paperInfo.get("shortList");
            shortList.add((Short) object);
        }
        else if(object instanceof Program){
            List<Program> programList = (List<Program>)paperInfo.get("programList");
            programList.add((Program) object);
        }
    }
    public Object getRandomProblem(PaperRule paperRule) throws ProblemException {
        List<ProblemStrategy> problemStrategyList = paper.getPaperStrategy().problemStrategyList();
        int size = problemStrategyList.size();
        Random random = paperRule.getRandom();
        int randomInt = random.nextInt(size);
        ProblemStrategy problemStrategy = problemStrategyList.get(randomInt);
        int randomInt2 = random.nextInt(problemStrategy.getProblemNum());
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();
        try {
            switch (problemStrategy.getProblemType()){
                case "单选题":{
                    List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get("singleChoiceList");
                    return singleChoiceList.get(randomInt2);
                }
                case "多选题":{
                    List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get("multipleChoiceList");
                    return multipleChoiceList.get(randomInt2);
                }
                case "简答题":{
                    List<Short> shortList = (List<Short>) paperInfo.get("shortList");
                    return shortList.get(randomInt2);
                }
                case "填空题":{
                    List<Fill> fillList = (List<Fill>) paperInfo.get("fillList");
                    return fillList.get(randomInt2);
                }
                case "判断题":{
                    List<Judge> judgeList = (List<Judge>) paperInfo.get("judgeList");
                    return judgeList.get(randomInt2);
                }
                case "编程题":{
                    List<Program> programList = (List<Program>) paperInfo.get("programList");
                    return programList.get(randomInt2);
                }
            }
        }catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
        }
        throw new ProblemException("未知题型");
    }
    public Object getProblem(int idx){//从1开始
        int count = idx;
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();//6  3.3.3
        Set<String> strings = paperInfo.keySet();
        Object problem = null;
        for (String problemType : strings) {
            List<Object> list = ((List<Object>) paperInfo.get(problemType));
            int size = list.size();
            if(count > size){
                count -= size;
                continue;
            }
            problem = list.get(count-1);
        }
        return problem;
    }
    public boolean containsProblem(Object problem){
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();
        if(problem instanceof SingleChoice){
            List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get("singleChoiceList");
            if(singleChoiceList == null || singleChoiceList.isEmpty()){
                return false;
            }
            for (SingleChoice singleChoice : singleChoiceList) {
                if(singleChoice != null && singleChoice.equals((SingleChoice)problem)){
                    return true;
                }
            }
        }
        else if(problem instanceof MultipleChoice){
            List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get("multipleChoiceList");
            if(multipleChoiceList == null || multipleChoiceList.isEmpty()){
                return false;
            }
            for (MultipleChoice multipleChoice : multipleChoiceList) {
                if(multipleChoice != null && multipleChoice.equals((MultipleChoice)problem)){
                    return true;
                }
            }
        }
        else if(problem instanceof Fill){
            List<Fill> fillList = (List<Fill>) paperInfo.get("fillList");
            if(fillList == null || fillList.isEmpty()){
                return false;
            }
            for (Fill fill : fillList) {
                if(fill != null && fill.equals((Fill)problem)){
                    return true;
                }
            }
        }
        else if(problem instanceof Short){
            List<Short> shortList = (List<Short>) paperInfo.get("shortList");
            if(shortList == null || shortList.isEmpty()){
                return false;
            }
            for (Short sho : shortList) {
                if(sho != null && sho.equals((Short)problem)){
                    return true;
                }
            }
        }
        else if(problem instanceof Judge){
            List<Judge> judgeList = (List<Judge>) paperInfo.get("judgeList");
            if(judgeList == null || judgeList.isEmpty()){
                return false;
            }
            for (Judge judge : judgeList) {
                if(judge != null && judge.equals((Judge)problem)){
                    return true;
                }
            }
        }
        else if(problem instanceof Program){
            List<Program> programList = (List<Program>) paperInfo.get("programList");
            if(programList == null || programList.isEmpty()){
                return false;
            }
            for (Program program : programList) {
                if(program != null && program.equals((Program)problem)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 计算知识点覆盖率 公式为：个体包含的知识点/期望包含的知识点
     *
     */
//    public void setKpCoverage(RuleBean rule) {
//        if (knowledgeCoverage == 0) {
//            Set<String> singleChoiceKnowledgeSet = new HashSet<String>();
//            singleChoiceKnowledgeSet.addAll(rule.getSingleChoiceKnowledgeList());
//
//            Set<String> singleChoiceKnowledgeSet_another = new HashSet<>();
//            ((List<SingleChoice>)paper.getPaperInfo().get("singleChoiceList")).stream().forEach(singleChoice -> singleChoiceKnowledgeSet_another.addAll(singleChoice.getKnowledgeList()));
//
//            Set<String> multipleChoiceKnowledgeSet_another = new HashSet<>();
//            ((List<SingleChoice>)paper.getPaperInfo().get("multipleChoiceList")).stream().forEach(multipleChoice -> multipleChoiceKnowledgeSet_another.addAll(multipleChoice.getKnowledgeList()));
//
//            Set<String> fillKnowledgeSet_another = new HashSet<>();
//            ((List<SingleChoice>)paper.getPaperInfo().get("fillList")).stream().forEach(singleChoice -> fillKnowledgeSet_another.addAll(singleChoice.getKnowledgeList()));
//
//            Set<String> shortKnowledgeSet_another = new HashSet<>();
//            ((List<SingleChoice>)paper.getPaperInfo().get("shortList")).stream().forEach(singleChoice -> shortKnowledgeSet_another.addAll(singleChoice.getKnowledgeList()));
//
//            Set<String> judgeKnowledgeSet_another = new HashSet<>();
//            ((List<SingleChoice>)paper.getPaperInfo().get("judgeList")).stream().forEach(singleChoice -> judgeKnowledgeSet_another.addAll(singleChoice.getKnowledgeList()));
//
//            Set<String> programKnowledgeSet_another = new HashSet<>();
//            ((List<SingleChoice>)paper.getPaperInfo().get("programList")).stream().forEach(singleChoice -> programKnowledgeSet_another.addAll(singleChoice.getKnowledgeList()));
//            // 交集操作
//            result.retainAll(another);
//            kPCoverage = result.size() / rule.getPointIds().size();
//        }
//    }

    /**
     * //            int r = random.nextInt(singleChoiceList.size());//随机挑选出的单选题序号
     * //            SingleChoice singleChoice = singleChoiceList.get(r);//根据序号得到单选题
     * //            paper_singleChoiceList.add(singleChoice);//选中单选题,添加进选中集合
     * //            singleChoiceList.remove(r);//从待选集合移除旧的单选题
     *
     * @Description  从待挑选的单选题集合挑选指定数量的单选题集合
     * @date 2020/3/9 15:53
     * @param problemList 待挑选的题集合
     * @param problemNum 挑选的数量
     * @return java.util.List<T>
     */
    private <T> List<T> getPaperProblemList(List<T> problemList, int problemNum) {
        Random random = new Random(System.currentTimeMillis());
        List<T> paper_problemList = new ArrayList<>(problemNum);
        List<Integer> randomIdxList = getRandomIdxList(problemList.size(),problemNum);
        for (int i = 0; i < problemNum; i++) paper_problemList.add(problemList.get(randomIdxList.get(i)));
        return paper_problemList;
    }
    /**
     * @Description 返回随机的数组集合 [0-problemSize] 中的problemNum个
     * @date 2020/3/14 17:51
     * @param problemSize 总数量
     * @param problemNum 要求数量
     * @return java.util.List<java.lang.Integer>
     */
    private List<Integer> getRandomIdxList(int problemSize,int problemNum){
        List<Integer> problemSizeList = new ArrayList<>(problemSize);
        for (int i = 0; i < problemSize; i++) problemSizeList.add(i);//初始化数组

        Random random = new Random(System.currentTimeMillis() % 1000);

        List<Integer> returnRandomIdxList = new ArrayList<>();
        for (int i = 0; i < problemNum; i++) {
            Integer randomIdx = problemSizeList.get(random.nextInt(problemSizeList.size()));
            problemSizeList.remove(randomIdx);
            returnRandomIdxList.add(randomIdx);
        }
        return returnRandomIdxList;

    }
}
