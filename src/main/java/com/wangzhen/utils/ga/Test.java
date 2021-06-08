//package com.wangzhen.utils.ga;
//import com.wangzhen.models.Paper;
//import com.wangzhen.models.PaperStrategy;
//import java.util.List;
///**
// * @Author wangzhen
// * @Description
// * @CreateDate 2020/3/30 13:42
// */
//public class Test {
//    static PaperStrategy paperStrategy = new PaperStrategy();
//    static String teacher_account;
//    static Float minDifficultyVal;
//    static Float maxDifficultyVal;
//    static Integer populationInitMaxRunCount;
//    static Float mutationRate;
//    static Boolean elitism;
//    static Integer tournamentSize;
//    static Boolean copulation;
//    static List<String> singleChoiceKnowledgeList;
//    static List<String> multipleChoiceKnowledgeList;
//    static List<String> programKnowledgeList;
//    static List<String> fillKnowledgeList;
//    static List<String> shortKnowledgeList;
//    static List<String> judgeKnowledgeList;
//    public static void main(String[] args) {
//        Paper resultPaper = null;
//        PaperRule paperRule = new PaperRule(paperStrategy,teacher_account,minDifficultyVal,maxDifficultyVal,populationInitMaxRunCount,mutationRate,elitism,tournamentSize,copulation,singleChoiceKnowledgeList,multipleChoiceKnowledgeList,programKnowledgeList,fillKnowledgeList,shortKnowledgeList,judgeKnowledgeList);
//        PaperPackage paperPackage = new PaperPackage(new Paper(), paperRule);
//        // 迭代计数器
//        int count = 0;
//        int runCount = 100;
//        // 适应度期望值
//        float expand = 0.98f;
//        float min_adaptationDegree = 0.5f;
//        float max_adaptationDegree = 0.5f;
//        StringBuilder errMsg = new StringBuilder();
//        try{
//            Population population = new Population(20,true,paperRule);
//            System.out.println("舒适化适应度"+population.getFitness().getAdaptationDegree());
//            Float adaptationDegree = population.getFitness().getAdaptationDegree();
//            while(adaptationDegree > max_adaptationDegree || adaptationDegree < min_adaptationDegree){
//                count++;
//                population = InheritanceAlgorithm.evolvePopulation(population,paperRule);
//                adaptationDegree = population.getFitness().getAdaptationDegree();
//                System.out.println("第 " + count + " 次进化，适应度为： " + adaptationDegree);
//            }
//            System.out.println("共进化 " + count + " 次，最终适应度为： " + adaptationDegree);
//            resultPaper = population.getFitness().getPaper();
//            System.out.println(resultPaper);
//        }catch (ProblemException e){
//
//        }
//
//
//    }
//}
