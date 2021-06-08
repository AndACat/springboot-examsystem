package com.wangzhen.utils.ga;

import com.wangzhen.configuration.MyWebAppConfig;
import com.wangzhen.models.Paper;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/30 17:19
 */
@SuppressWarnings("unchecked")
public class InheritanceAlgorithm {
    private static SingleChoiceService singleChoiceService = MyWebAppConfig.context.getBean(SingleChoiceService.class);
    private static MultipleChoiceService multipleChoiceService = MyWebAppConfig.context.getBean(MultipleChoiceService.class);
    private static FillService fillService = MyWebAppConfig.context.getBean(FillService.class);
    private static JudgeService judgeService = MyWebAppConfig.context.getBean(JudgeService.class);
    private static ShortService shortService = MyWebAppConfig.context.getBean(ShortService.class);
    private static ProgramService programService = MyWebAppConfig.context.getBean(ProgramService.class);

    /**
     * 种群进化操作
     */
    public static Population evolvePopulation(Population population,PaperRule paperRule) throws Exception {
        Population newPopulation = new Population(population.getLength());
        int elitismOffset = paperRule.getElitism() ? 1 : 0;
        //精英主义
        if (paperRule.getElitism()){
            // 保留上一代最优秀个体
            PaperPackage fitness = population.getFitness();
            newPopulation.setPaperPackage(0, fitness);
        }
        //种群交配
        if(paperRule.getCopulation()){
            //种群交配操作，从当前的种群population来创建下一代种群newPopulation
            for (int i = elitismOffset; i < newPopulation.getLength(); i++) {
                // 较优选择parent
                PaperPackage parent1 = select(population,paperRule);
                PaperPackage parent2 = select(population,paperRule);
                while (parent1.getPaper().getUuid() == parent2.getPaper().getUuid()) {
                    parent2 = select(population,paperRule);
                }
                // 交叉
                PaperPackage child = copulation(parent1, parent2, paperRule);
                Float adaptationDegree = child.getAdaptationDegree();
                newPopulation.setPaperPackage(i, child);
            }
        }
        return newPopulation;
    }

    /**
     * 种群交配
     */
    private static PaperPackage copulation(PaperPackage parent1, PaperPackage parent2, PaperRule paperRule) throws Exception {
        Paper paper = new Paper();
        paper.setPaperStrategy(paperRule.getPaperStrategy());
        paper.setUuid(Utils.randomUuid());
        PaperPackage child = new PaperPackage(paper,false,paperRule);
        child.getPaper().setTotalScore(paperRule.getPaperStrategy().getAllScore());
        Random random = paperRule.getRandom();
        LinkedHashMap<String, Object> childPaperInfo = child.getPaper().getPaperInfo();
        LinkedHashMap<String, Object> parent1PaperInfo = parent1.getPaper().getPaperInfo();
        LinkedHashMap<String, Object> parent2PaperInfo = parent2.getPaper().getPaperInfo();
        List<ProblemStrategy> problemStrategyList = paperRule.getPaperStrategy().problemStrategyList();
        for (ProblemStrategy problemStrategy : problemStrategyList) {
            switch (problemStrategy.getProblemType()){
                case "单选题":{
                    String type = "singleChoiceList";
                    List<Object> list1 = (List<Object>) parent1PaperInfo.get(type);
                    List<Object> list2 = (List<Object>) parent2PaperInfo.get(type);
                    List<Object> randomProblemList = getRandomProblemList(list1, list2, problemStrategy.getProblemNum());
                    childPaperInfo.put(type,randomProblemList);
                    break;
                }
                case "多选题":{
                    String type = "multipleChoiceList";
                    List<Object> list1 = (List<Object>) parent1PaperInfo.get(type);
                    List<Object> list2 = (List<Object>) parent2PaperInfo.get(type);
                    List<Object> randomProblemList = getRandomProblemList(list1, list2, problemStrategy.getProblemNum());
                    childPaperInfo.put(type,randomProblemList);
                    break;
                }
                case "填空题":{
                    String type = "fillList";
                    List<Object> list1 = (List<Object>) parent1PaperInfo.get(type);
                    List<Object> list2 = (List<Object>) parent2PaperInfo.get(type);
                    List<Object> randomProblemList = getRandomProblemList(list1, list2, problemStrategy.getProblemNum());
                    childPaperInfo.put(type,randomProblemList);
                    break;
                }
                case "简答题":{
                    String type = "shortList";
                    List<Object> list1 = (List<Object>) parent1PaperInfo.get(type);
                    List<Object> list2 = (List<Object>) parent2PaperInfo.get(type);
                    List<Object> randomProblemList = getRandomProblemList(list1, list2, problemStrategy.getProblemNum());
                    childPaperInfo.put(type,randomProblemList);
                    break;
                }
                case "判断题":{
                    String type = "judgeList";
                    List<Object> list1 = (List<Object>) parent1PaperInfo.get(type);
                    List<Object> list2 = (List<Object>) parent2PaperInfo.get(type);
                    List<Object> randomProblemList = getRandomProblemList(list1, list2, problemStrategy.getProblemNum());
                    childPaperInfo.put(type,randomProblemList);
                    break;
                }
                case "编程题":{
                    String type = "programList";
                    List<Object> list1 = (List<Object>) parent1PaperInfo.get(type);
                    List<Object> list2 = (List<Object>) parent2PaperInfo.get(type);
                    List<Object> randomProblemList = getRandomProblemList(list1, list2, problemStrategy.getProblemNum());
                    childPaperInfo.put(type,randomProblemList);
                    break;
                }
                default:
                    throw new Exception("未知异常");
            }
        }
        child.setDifficulty(paperRule);
        child.setAdaptationDegree(paperRule);
        return child;
    }

    private static List<Object> getRandomProblemList(List<Object> list1, List<Object> list2, int problemNum) {
        List<Object> list = new ArrayList<>();
        Random random = new Random(System.currentTimeMillis());
        int n = problemNum;
        while(n-- > 0){
            if(random.nextBoolean()){
                list.add(list1.get(random.nextInt(list1.size())));
            }else{
                list.add(list2.get(random.nextInt(list2.size())));
            }
        }
        return list;
    }

    public static PaperPackage select(Population population,PaperRule paperRule){
        Population pop = new Population(paperRule.getTournamentSize());
        for (int i = 0; i < paperRule.getTournamentSize(); i++) {
            int i1 = 0;
            do{
                i1 = paperRule.getRandom().nextInt(paperRule.getPopulationSize());
            }while (i1 == 0 && pop.containPaperPackage(population.getPaperPackage(i1)));
            PaperPackage paperPackage = population.getPaperPackage(i1);
            paperPackage.setDifficulty(paperRule);
            paperPackage.setAdaptationDegree(paperRule);
            pop.setPaperPackage(i,paperPackage);
        }
        return pop.getFitness();
    }
    public static Object getNonRepeatProblemList(String teacher_account, Object o, LinkedHashMap<String, Object> paperInfo)
            throws ProblemException {
        List<String> uuidList = new ArrayList<>();
        if(o instanceof SingleChoice){
            List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get("singleChoiceList");
            singleChoiceList.stream().forEach(singleChoice -> uuidList.add(singleChoice.getUuid()));
            return getNonRepeatProblemList(teacher_account,o.getClass(),uuidList);
        }
        if(o instanceof MultipleChoice){
            List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get("multipleChoiceList");
            multipleChoiceList.stream().forEach(multipleChoice -> uuidList.add(multipleChoice.getUuid()));
            return getNonRepeatProblemList(teacher_account,o.getClass(),uuidList);
        }
        if(o instanceof Fill){
            List<Fill> fillList = (List<Fill>) paperInfo.get("fillList");
            fillList.stream().forEach(fill -> uuidList.add(fill.getUuid()));
            return getNonRepeatProblemList(teacher_account,o.getClass(),uuidList);
        }
        if(o instanceof Short){
            List<Short> shortList = (List<Short>) paperInfo.get("shortList");
            shortList.stream().forEach(sho -> uuidList.add(sho.getUuid()));
            return getNonRepeatProblemList(teacher_account,o.getClass(),uuidList);
        }
        if(o instanceof Judge){
            List<Judge> judgeList = (List<Judge>) paperInfo.get("judgeList");
            judgeList.stream().forEach(judge -> uuidList.add(judge.getUuid()));
            return getNonRepeatProblemList(teacher_account,o.getClass(),uuidList);
        }
        if(o instanceof Program){
            List<Program> programList = (List<Program>) paperInfo.get("programList");
            programList.stream().forEach(program -> uuidList.add(program.getUuid()));
            return getNonRepeatProblemList(teacher_account,o.getClass(),uuidList);
        }
        throw new ProblemException("未知题型");
    }
    public static <T> T getNonRepeatProblemList(String teacher_account,Class<T> cla,List<String> uuidList) throws ProblemException {
        if(cla == SingleChoice.class){
            return (T) singleChoiceService.selectSingleChoiceWithoutUuidList(teacher_account,uuidList);
        }
        if(cla == MultipleChoice.class){
            return (T) multipleChoiceService.selectMultipleChoiceWithoutUuidList(teacher_account,uuidList);
        }
        if(cla == Fill.class){
            return (T)fillService.selectFillWithoutUuidList(teacher_account,uuidList);
        }
        if(cla == Short.class){
            return (T)shortService.selectShortWithoutUuidList(teacher_account,uuidList);
        }
        if(cla == Judge.class) {
            return (T)judgeService.selectJudgeWithoutUuidList(teacher_account,uuidList);
        }
        if(cla == Program.class){
            return (T)programService.selectProgramWithoutUuidList(teacher_account,uuidList);
        }
        throw new ProblemException("未知题型");
    }
}
