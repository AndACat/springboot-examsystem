package com.wangzhen.utils.ga;

import com.wangzhen.configuration.MyWebAppConfig;
import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.services.teacher.PaperStrategyService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/31 16:51
 */
public class CustomCallable implements Callable<PaperPackage> {
    private static PaperStrategyService paperStrategyService = MyWebAppConfig.context.getBean(PaperStrategyService.class);

    public CustomCallable(String paperStrategy_uuid, String teacher_account, Float minDifficultyVal, Float maxDifficultyVal, Float min_adaptationDegree, Integer maxEvolutionNum, Integer populationInitMaxRunCount, Integer populationSize, Float mutationRate, Boolean elitism, Integer tournamentSize, Boolean copulation, List<String> singleChoiceKnowledgeList, List<String> multipleChoiceKnowledgeList, List<String> programKnowledgeList, List<String> fillKnowledgeList, List<String> shortKnowledgeList, List<String> judgeKnowledgeList, HttpSession session) {
        this.paperStrategy_uuid = paperStrategy_uuid;
        this.teacher_account = teacher_account;
        this.minDifficultyVal = minDifficultyVal;
        this.maxDifficultyVal = maxDifficultyVal;
        this.min_adaptationDegree = min_adaptationDegree;
        this.maxEvolutionNum = maxEvolutionNum;
        this.populationInitMaxRunCount = populationInitMaxRunCount;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.elitism = elitism;
        this.tournamentSize = tournamentSize;
        this.copulation = copulation;
        this.singleChoiceKnowledgeList = singleChoiceKnowledgeList;
        this.multipleChoiceKnowledgeList = multipleChoiceKnowledgeList;
        this.programKnowledgeList = programKnowledgeList;
        this.fillKnowledgeList = fillKnowledgeList;
        this.shortKnowledgeList = shortKnowledgeList;
        this.judgeKnowledgeList = judgeKnowledgeList;
        this.session = session;
    }

    String paperStrategy_uuid;
    String teacher_account;
    Float minDifficultyVal;//0-10
    Float maxDifficultyVal;//0-10
    Float min_adaptationDegree;//0%-100%
    Integer maxEvolutionNum;//0%-100%
    Integer populationInitMaxRunCount;
    Integer populationSize;
    Float mutationRate;
    Boolean elitism;
    Integer tournamentSize;
    Boolean copulation;
    List<String> singleChoiceKnowledgeList;
    List<String> multipleChoiceKnowledgeList;
    List<String> programKnowledgeList;
    List<String> fillKnowledgeList;
    List<String> shortKnowledgeList;
    List<String> judgeKnowledgeList;
    HttpSession session;
    PaperPackage paperPackage = null;
    ProblemException problemException = null;
    @Override
    public PaperPackage call() {
        CustomCallable customCallable = this;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    paperPackage = customCallable.getPaper(
                            paperStrategy_uuid,
                            teacher_account,
                            minDifficultyVal,
                            maxDifficultyVal,
                            min_adaptationDegree,
                            maxEvolutionNum,
                            populationInitMaxRunCount,
                            populationSize,
                            mutationRate,
                            elitism,
                            tournamentSize,
                            copulation,
                            singleChoiceKnowledgeList,
                            multipleChoiceKnowledgeList,
                            programKnowledgeList,
                            fillKnowledgeList,
                            shortKnowledgeList,
                            judgeKnowledgeList,
                            session);
                } catch (ProblemException e) {
                    paperPackage = new PaperPackage();
                    problemException = e;
                }
            };
        });
        t1.start();
        try {
            t1.join(20000);
        } catch (InterruptedException e) {
            paperPackage = new PaperPackage();
            paperPackage.e = new ProblemException("运行超时");
        }
        if(problemException != null){
            paperPackage = new PaperPackage();
            paperPackage.e = new ProblemException(problemException.getMessage());
            t1.stop();
            return paperPackage;
        }
        if(paperPackage == null){
            paperPackage = new PaperPackage();
            paperPackage.e = new ProblemException("运行超时");
            t1.stop();
            return paperPackage;
        }
        t1.stop();
        return paperPackage;
    }
    public PaperPackage getPaper(String paperStrategy_uuid,
                                 String teacher_account,
                                 Float minDifficultyVal,//0-10
                                 Float maxDifficultyVal,//0-10
                                 Float min_adaptationDegree,//0%-100%
                                 Integer maxEvolutionNum,//0%-100%
                                 Integer populationInitMaxRunCount,
                                 Integer populationSize,
                                 Float mutationRate,
                                 Boolean elitism,
                                 Integer tournamentSize,
                                 Boolean copulation,
                                 List<String> singleChoiceKnowledgeList,
                                 List<String> multipleChoiceKnowledgeList,
                                 List<String> programKnowledgeList,
                                 List<String> fillKnowledgeList,
                                 List<String> shortKnowledgeList,
                                 List<String> judgeKnowledgeList,
                                 HttpSession session) throws ProblemException {
        Population population = null;
        int count = 0;// 迭代计数器
        int runCount = maxEvolutionNum;//最大进化次数
        try {
            PaperStrategy paperStrategy = paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paperStrategy_uuid);
            PaperRule paperRule = new PaperRule(
                    paperStrategy,
                    teacher_account, populationSize,minDifficultyVal, maxDifficultyVal, populationInitMaxRunCount,
                    mutationRate, elitism, tournamentSize, copulation, singleChoiceKnowledgeList,
                    multipleChoiceKnowledgeList,programKnowledgeList,fillKnowledgeList,shortKnowledgeList,judgeKnowledgeList
            );
            Paper paper = new Paper();
            paper.setPaperStrategy(paperStrategy);
//        PaperPackage paperPackage = new PaperPackage(paper, paperRule);
            PaperPackage fitness = null;
            population = new Population(populationSize,true,paperRule);
            fitness = population.getFitness();
            Float adaptationDegree = population.getFitness().getAdaptationDegree();
            System.out.println("初始化适应度"+fitness.getAdaptationDegree() + adaptationDegree+",难度值为"+fitness.getDifficultyVal()+",paper:"+fitness.getPaper().getUuid());
            System.out.println("min_adaptationDegree"+min_adaptationDegree);
            while(adaptationDegree < min_adaptationDegree && runCount-->0){
                count++;
                population = InheritanceAlgorithm.evolvePopulation(population,paperRule);
                adaptationDegree = population.getFitness().getAdaptationDegree();
                fitness = population.getFitness();
                System.out.println("第 " + count + " 次进化，适应度为： " + adaptationDegree+",难度值为"+fitness.getDifficultyVal()+",paper:"+fitness.getPaper().getUuid());
            }
            System.out.println("共进化 " + count + " 次，最终适应度为： " + adaptationDegree);

            session.setAttribute("evolutionNum",count);
        }catch (Exception e){

        }
        if(runCount <= 0){
            throw new ProblemException("进化失败,共进化"+maxEvolutionNum+"次");
        }
        return population.getFitness();
    }
}
