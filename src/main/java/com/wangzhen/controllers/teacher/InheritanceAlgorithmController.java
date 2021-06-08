package com.wangzhen.controllers.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.InheritanceAlgorithmService;
import com.wangzhen.services.teacher.RandomPaperBuilderService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import com.wangzhen.utils.ga.PaperPackage;
import com.wangzhen.utils.ga.ProblemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/30 20:05
 */
@RestController
@RequestMapping("/teacher")
public class InheritanceAlgorithmController {
    private static final String INHERITANCEALGORITHM_PAPERINFO = "INHERITANCEALGORITHM_PAPERINFO";
    @Autowired
    private InheritanceAlgorithmService inheritanceAlgorithmService;

    @RequestMapping("/inheritanceAlgorithmPaper")
    public ResponseMessage inheritanceAlgorithmPaper(@RequestParam("paperStrategy_uuid")String paperStrategy_uuid,//策略uuid
                                                     @RequestParam("knowledgeMap") Map<String, List<String>> knowledgeMap,
                                                     @RequestParam("minDifficultyVal")Float minDifficultyVal,
                                                     @RequestParam("populationSize")Integer populationSize,
                                                     @RequestParam("maxDifficultyVal")Float maxDifficultyVal,
                                                     @RequestParam("min_adaptationDegree")Float min_adaptationDegree,//0%-100%
                                                     @RequestParam("populationInitMaxRunCount")Integer populationInitMaxRunCount,
                                                     @RequestParam("mutationRate")Float mutationRate,
                                                     @RequestParam("elitism")Boolean elitism,
                                                     @RequestParam("tournamentSize")Integer tournamentSize,
                                                     @RequestParam("copulation")Boolean copulation,
                                                     @RequestParam("maxEvolutionNum")Integer maxEvolutionNum,
                                                     HttpSession session) throws Exception {
        List<String> singleChoiceKnowledgeList = knowledgeMap.get("单选题");
        List<String> multipleChoiceKnowledgeList = knowledgeMap.get("多选题");
        List<String> fillKnowledgeList = knowledgeMap.get("填空题");
        List<String> judgeKnowledgeList = knowledgeMap.get("判断题");
        List<String> shortKnowledgeList = knowledgeMap.get("简答题");
        List<String> programKnowledgeList = knowledgeMap.get("编程题");
        Teacher teacher = Utils.getUser(session, Teacher.class);
        PaperPackage paperPackage = null;
        Long startTime = System.currentTimeMillis();
        try {
            paperPackage = inheritanceAlgorithmService.getPaper(
                    paperStrategy_uuid,
                    teacher.getAccount(),
                    minDifficultyVal,
                    maxDifficultyVal,
                    min_adaptationDegree,//0%-100%
                    maxEvolutionNum,//0%-100%
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
        } catch (ExecutionException | InterruptedException | ProblemException e) {
            return new ResponseMessage().setCode(403).setMsg(e.getMessage());
        }
        if(paperPackage.e != null){
            return new ResponseMessage().setCode(403).setMsg(paperPackage.e.getMessage());
        }
        Long endTime = System.currentTimeMillis();
        float second = (float) (endTime-startTime);
        int evolutionNum =  (int)session.getAttribute("evolutionNum");
        System.out.println(evolutionNum);
        session.setAttribute(INHERITANCEALGORITHM_PAPERINFO,paperPackage.getPaper().getPaperInfo());
        return new ResponseMessage()
                .setCode(200)
                .setMsg("组卷成功.\n试卷难度系数为"+paperPackage.getDifficultyVal()+".\n本轮共进化"+(evolutionNum+1)+"次.\n耗时"+second/1000+"秒.")
                .setData(paperPackage.getPaper().getPaperInfo());

    }

    @RequestMapping("/saveInheritanceAlgorithmPaper")
    public ResponseMessage saveInheritanceAlgorithmPaper(@RequestParam("course_uuid") String course_uuid,
                                                         @RequestParam("paperName")String paperName,
                                                         @RequestParam("during")Integer during,
                                                         @RequestParam("sort")boolean sort,
                                                         @RequestParam("openFaceIdentity")boolean openFaceIdentity,
                                                         @RequestParam("begin_time")long begin_time,
                                                         @RequestParam("end_time")long end_time,
                                                         @RequestParam("visible") boolean visible,
                                                         HttpSession session){
        boolean tag = inheritanceAlgorithmService.savePaper(paperName, course_uuid, during, sort, openFaceIdentity, new Timestamp(begin_time), new Timestamp(end_time),visible ,session);
        return tag ? ResponseMessage.OK : ResponseMessage.FAIL;
    }
}
