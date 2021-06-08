package com.wangzhen.services.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.Utils;
import com.wangzhen.utils.ga.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/30 20:00
 */
@Service
public class InheritanceAlgorithmService {
    private static ExecutorService pool = Executors.newFixedThreadPool(10);
    private static final String SESSION_INHERITANCEALGORITHM_PAPERSTRATEGY = "SESSION_INHERITANCEALGORITHM_PAPERSTRATEGY";
    private static final String SESSION_INHERITANCEALGORITHM_PAPERINFO = "SESSION_INHERITANCEALGORITHM_PAPERINFO";
    @Autowired
    private PaperStrategyService paperStrategyService;
    @Autowired
    private SingleChoiceService singleChoiceService;
    @Autowired
    private MultipleChoiceService multipleChoiceService;
    @Autowired
    private FillService fillService;
    @Autowired
    private ShortService shortService;
    @Autowired
    private JudgeService judgeService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private ProgramService programService;
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
                                 HttpSession session) throws Exception {
//        CustomCallable customCallable = new CustomCallable(paperStrategy_uuid, teacher_account, minDifficultyVal, maxDifficultyVal, min_adaptationDegree, maxEvolutionNum, populationInitMaxRunCount, populationSize, mutationRate, elitism, tournamentSize, copulation, singleChoiceKnowledgeList, multipleChoiceKnowledgeList, programKnowledgeList, fillKnowledgeList, shortKnowledgeList, judgeKnowledgeList, session);
//        Future<PaperPackage> submit = pool.submit(customCallable);
//        return submit.get();
//    }


        PaperStrategy paperStrategy = paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paperStrategy_uuid);
        session.setAttribute(SESSION_INHERITANCEALGORITHM_PAPERSTRATEGY,paperStrategy);
        PaperRule paperRule = new PaperRule(
                paperStrategy,
                teacher_account, populationSize,minDifficultyVal, maxDifficultyVal, populationInitMaxRunCount,
                mutationRate, elitism, tournamentSize, copulation, singleChoiceKnowledgeList,
                multipleChoiceKnowledgeList,programKnowledgeList,fillKnowledgeList,shortKnowledgeList,judgeKnowledgeList
                );
        Paper paper = new Paper();
        paper.setPaperStrategy(paperStrategy);
//        PaperPackage paperPackage = new PaperPackage(paper, paperRule);
        // 迭代计数器
        int count = 0;
        int runCount = maxEvolutionNum;//最大进化次数
        Population population = new Population(populationSize,true,paperRule);
//        System.out.println("初始化适应度"+population.getFitness().getAdaptationDegree());
        Float adaptationDegree = population.getFitness().getAdaptationDegree();
        System.out.println("min_adaptationDegree"+min_adaptationDegree);
        while(adaptationDegree < min_adaptationDegree && runCount-->0){
            count++;
            population = InheritanceAlgorithm.evolvePopulation(population,paperRule);
            adaptationDegree = population.getFitness().getAdaptationDegree();
            System.out.println("第 " + count + " 次进化，适应度为： " + adaptationDegree);
        }
        System.out.println("共进化 " + count + " 次，最终适应度为： " + adaptationDegree);
        if(runCount <= 0){
            throw new ProblemException("进化失败,共进化"+maxEvolutionNum+"次");
        }
        PaperPackage fitness = population.getFitness();
        session.setAttribute("evolutionNum",count);
        session.setAttribute(SESSION_INHERITANCEALGORITHM_PAPERINFO,fitness.getPaper().getPaperInfo());
        return fitness;
    }

    public boolean savePaper(String paperName,
                             String course_uuid,
                             Integer during,
                             boolean sort,
                             boolean openFaceIdentity,
                             Timestamp begin,
                             Timestamp end,
                             boolean visible,
                             HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        LinkedHashMap<String,Object> paperInfo = (LinkedHashMap<String, Object>) session.getAttribute(SESSION_INHERITANCEALGORITHM_PAPERINFO);
        PaperStrategy paperStrategy = (PaperStrategy) session.getAttribute(SESSION_INHERITANCEALGORITHM_PAPERSTRATEGY);
        if(paperInfo == null){
            return false;
        }
        if(paperService.selectPaperByPaperName(paperName,teacher.getUuid()) != null){
            return false;
        }
        for (ProblemStrategy problemStrategy : paperStrategy.problemStrategyList()) {
            switch (problemStrategy.getProblemType()){
                case "单选题":{
                    List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get("singleChoiceList");
                    for (SingleChoice singleChoice : singleChoiceList) {
                        singleChoiceService.addUseNum(teacher.getAccount(),singleChoice.getUuid());
                    }
                    break;
                }
                case "多选题":{
                    List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get("multipleChoiceList");
                    for (MultipleChoice multipleChoice : multipleChoiceList) {
                        multipleChoiceService.addUseNum(teacher.getAccount(),multipleChoice.getUuid());
                    }
                    break;
                }
                case "判断题":{
                    List<Judge> judgeList = (List<Judge>) paperInfo.get("judgeList");
                    for (Judge judge : judgeList) {
                        judgeService.addUseNum(teacher.getAccount(),judge.getUuid());
                    }
                    break;
                }
                case "填空题":{
                    List<Fill> fillList = (List<Fill>) paperInfo.get("fillList");
                    for (Fill fill : fillList) {
                        fillService.addUseNum(teacher.getAccount(),fill.getUuid());
                    }
                    break;
                }
                case "简答题":{
                    List<com.wangzhen.models.problem.Short> shortList = (List<com.wangzhen.models.problem.Short>) paperInfo.get("shortList");
                    for (Short sho : shortList) {
                        shortService.addUseNum(teacher.getAccount(),sho.getUuid());

                    }
                    break;
                }
                case "编程题":{
                    List<Program> programList = (List<Program>) paperInfo.get("programList");
                    for (Program program : programList) {
                        programService.addUseNum(teacher.getAccount(),program.getUuid());
                    }
                    break;
                }
            }
        }
        Paper paper = Paper.builder()
                .uuid(Utils.randomUuid())
                .totalScore(paperStrategy.getAllScore())
                .paperName(paperName)
                .course_uuid(course_uuid)
                .sort(sort)
                .openFaceIdentity(openFaceIdentity)
                .begin(begin)
                .during(during)
                .end(end)
                .visible(visible)
                .teacher_uuid(teacher.getUuid())
                .paperInfo(paperInfo)
                .paperStrategy_uuid(paperStrategy.getUuid())
                .build();
        paperService.insertPaper(paper);
        return true;
    }
}
