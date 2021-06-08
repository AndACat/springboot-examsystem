package com.wangzhen.utils.ga;

import com.wangzhen.configuration.MyWebAppConfig;
import com.wangzhen.models.Paper;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author wangzhen
 * @Description 种群，即多套试卷
 * @CreateDate 2020/3/30 15:48
 */
public class Population {
    private SingleChoiceService singleChoiceService = MyWebAppConfig.context.getBean(SingleChoiceService.class);
    private MultipleChoiceService multipleChoiceService = MyWebAppConfig.context.getBean(MultipleChoiceService.class);
    private FillService fillService = MyWebAppConfig.context.getBean(FillService.class);
    private ShortService shortService = MyWebAppConfig.context.getBean(ShortService.class);
    private JudgeService judgeService = MyWebAppConfig.context.getBean(JudgeService.class);
    private ProgramService programService = MyWebAppConfig.context.getBean(ProgramService.class);

    public Population(int populationSize) {
        paperPackages = new PaperPackage[populationSize];
    }

    /**
     * 试卷集合
     */
    private PaperPackage[] paperPackages;
    public Population (int populationSize, boolean initFlag, PaperRule paperRule) throws ProblemException {
        paperPackages = new PaperPackage[populationSize];
        if(initFlag){
            Paper paper = null;
            PaperPackage paperPackage = null;
            for (int i = 0; i < populationSize; i++) {
                paper = new Paper();
                paper.setTotalScore(paperRule.getPaperStrategy().getAllScore());//设置总分
                paper.setUuid(Utils.randomUuid());//设置uuid
                paper.setPaperStrategy(paperRule.getPaperStrategy());
                paperPackage = new PaperPackage(paper,true, paperRule);
                paperPackage.getPaper().setTotalScore(paperRule.getPaperStrategy().getAllScore());
                paperPackage.setAdaptationDegree(paperRule);
                int populationInitMaxRunCount = paperRule.getPopulationInitMaxRunCount();
//                while((paperPackage.getDifficultyVal() == 0f) && (paperPackage.getDifficultyVal() < paperRule.getMinDifficultyVal() || paperPackage.getDifficultyVal() > paperRule.getMaxDifficultyVal()) && (populationInitMaxRunCount-- >0)){
//                    //当难度值超出范围且超过迭代次数(表明数据库中可能题目的难度系数值本就无法出题,例如数据库中难度值全是0,则无法出出难度值为9的试卷)
//                    for (ProblemStrategy problemStrategy : paperRule.getPaperStrategy().problemStrategyList()) {
//                        Integer problemNum = problemStrategy.getProblemNum();
//                        switch (problemStrategy.getProblemType()){
//                            case "单选题":{
//                                List<SingleChoice> singleChoiceList = singleChoiceService.selectMyAllSingleChoiceWithKnowledgeMap(paperRule.getTeacher_account(), paperRule.getSingleChoiceKnowledgeList());
//                                if(singleChoiceList.size()  < problemNum){
//                                    throw new ProblemException("单选题数量不够,要求"+problemNum+"道,但仅有"+singleChoiceList.size()+"道;\n");
//                                }
//                                paper.getPaperInfo().put("singleChoiceList",getPaperProblemList(singleChoiceList,problemNum));
//                                break;
//                            }
//                            case "多选题":{
//                                List<MultipleChoice> multipleChoiceList = multipleChoiceService.selectMyAllMultipleChoiceWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getMultipleChoiceKnowledgeList());
//                                if(multipleChoiceList.size()  < problemNum){
//                                    throw new ProblemException("多选题数量不够,要求"+problemNum+"道,但仅有"+multipleChoiceList.size()+"道;\n");
//                                }
//                                paper.getPaperInfo().put("multipleChoiceList",getPaperProblemList(multipleChoiceList,problemNum));
//                                break;
//                            }
//                            case "填空题":{
//                                List<Fill> fillList = fillService.selectMyAllFillWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getFillKnowledgeList());
//                                if(fillList.size()  < problemNum){
//                                    throw new ProblemException("填空题数量不够,要求"+problemNum+"道,但仅有"+fillList.size()+"道;\n");
//                                }
//                                paper.getPaperInfo().put("fillList",getPaperProblemList(fillList,problemNum));
//                                break;
//                            }
//                            case "简答题":{
//                                List<Short> shortList = shortService.selectMyAllShortWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getShortKnowledgeList());
//                                if(shortList.size()  < problemNum){
//                                    throw new ProblemException("简答题数量不够,要求"+problemNum+"道,但仅有"+shortList.size()+"道;\n");
//                                }
//                                paper.getPaperInfo().put("shortList",getPaperProblemList(shortList,problemNum));
//                                break;
//                            }
//                            case "判断题":{
//                                List<Fill> fillList = fillService.selectMyAllFillWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getJudgeKnowledgeList());
//                                if(fillList.size()  < problemNum){
//                                    throw new ProblemException("填空题数量不够,要求"+problemNum+"道,但仅有"+fillList.size()+"道;\n");
//                                }
//                                paper.getPaperInfo().put("fillList",getPaperProblemList(fillList,problemNum));
//                                break;
//                            }
//                            case "编程题":{
//                                List<Program> programList = programService.selectMyAllShortWithKnowledgeMap(paperRule.getTeacher_account(),paperRule.getProgramKnowledgeList());
//                                if(programList.size()  < problemNum){
//                                    throw new ProblemException("编程题数量不够,要求"+problemNum+"道,但仅有"+programList.size()+"道;\n");
//                                }
//                                paper.getPaperInfo().put("programList",getPaperProblemList(programList,problemNum));
//                                break;
//                            }
//                        }
//                    }
//                }
                if(populationInitMaxRunCount == 0){
                    throw new ProblemException("数据库中整体难度值不合法;");
                }
                paperPackage.setAdaptationDegree(paperRule);
                paperPackages[i] = paperPackage;
            }
        }
    }
    /**
     * 获取种群中最优秀个体
     *
     * @return
     */
    public PaperPackage getFitness() {
        PaperPackage paperPackage = paperPackages[0];
        System.out.println("适应度init\t");
        for (int i = 0; i < paperPackages.length; i++) {
            System.out.print("\t"+paperPackages[i].getAdaptationDegree());
            if (paperPackage.getAdaptationDegree() < paperPackages[i].getAdaptationDegree()) {
                paperPackage = paperPackages[i];
            }
        }
        System.out.print("\t选定适应度:"+paperPackage.getAdaptationDegree());
        System.out.print("\t适应度stop");
        return paperPackage;
    }
    public void setPaperPackage(int idx, PaperPackage paperPackage){
        paperPackages[idx] = paperPackage;
    }

    /**
     * 返回种群规模
     *
     * @return
     */
    public int getLength() {
        return paperPackages.length;
    }

    public PaperPackage getPaperPackage(int idx) {
        return paperPackages[idx];
    }
    public PaperPackage[] getPaperPackages() {
        return paperPackages;
    }

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

    public boolean containPaperPackage(PaperPackage paperPackage) {
        for (PaperPackage aPackage : paperPackages) {
            if(aPackage != null){
                if(aPackage == paperPackage || aPackage.getPaper() == paperPackage.getPaper()){
                    return true;
                }
            }
        }
        return false;
    }
}
