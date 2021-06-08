package com.wangzhen.services.teacher;

import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.teacher.PaperDao;
import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/1 19:05
 */
@Service
public class FixationPaperService {
    @Autowired
    private PaperService paperService;
    @Autowired
    private SingleChoiceService singleChoiceService;
    @Autowired
    private MultipleChoiceService multipleChoiceService;
    @Autowired
    private FillService fillService;
    @Autowired
    private JudgeService judgeService;
    @Autowired
    private ShortService shortService;
    @Autowired
    private ProgramService programService;
    @Autowired
    private PaperStrategyService paperStrategyService;
    public boolean savePaper(String course_uuid,
                             String paperName,
                             Integer during,
                             boolean sort,
                             boolean openFaceIdentity,
                             Timestamp begin,
                             Timestamp end,
                             boolean visible,
                             List<String> singleChoice_uuid_list,
                             List<String> multipleChoice_uuid_list,
                             List<String> fill_uuid_list,
                             List<String> judge_uuid_list,
                             List<String> short_uuid_list,
                             List<String> program_uuid_list,
                             float singleChoiceScore,
                             float multipleChoiceScore,
                             float fillScore,
                             float judgeScore,
                             float shortScore,
                             float programScore,
                             HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        String account = teacher.getAccount();
        if(paperService.selectPaperByPaperName(paperName,teacher.getUuid()) != null){
            return false;
        }
        LinkedHashMap<String,Object> paperInfo = new LinkedHashMap<>();
        List<SingleChoice> singleChoiceList = null;
        List<MultipleChoice> multipleChoiceList = null;
        List<Fill> fillList = null;
        List<Short> shortList = null;
        List<Judge> judgeList = null;
        List<Program> programList = null;
        PaperStrategy paperStrategy = new PaperStrategy();
        paperStrategy.setUuid(Utils.randomUuid());
        List<ProblemStrategy> problemStrategyList = new ArrayList<>();
        if(singleChoice_uuid_list.size()!=0){
            problemStrategyList.add(new ProblemStrategy("单选题",singleChoice_uuid_list.size(),singleChoiceScore));
            singleChoiceList = new ArrayList<>();
            for (String uuid : singleChoice_uuid_list) {
                singleChoiceList.add(singleChoiceService.selectProblemByUuid(account,uuid));
            }
            paperInfo.put("singleChoiceList",singleChoiceList);
        }
        if(multipleChoice_uuid_list.size() != 0){
            problemStrategyList.add(new ProblemStrategy("多选题",multipleChoice_uuid_list.size(),multipleChoiceScore));
            multipleChoiceList = new ArrayList<>();
            for (String uuid : multipleChoice_uuid_list) {
                multipleChoiceList.add(multipleChoiceService.selectProblemByUuid(account,uuid));
            }
            paperInfo.put("multipleChoiceList",multipleChoiceList);
        }
        if(fill_uuid_list.size() != 0){
            problemStrategyList.add(new ProblemStrategy("填空题",fill_uuid_list.size(),fillScore));
            fillList = new ArrayList<>();
            for (String uuid : fill_uuid_list) {
                fillList.add(fillService.selectProblemByUuid(account,uuid));
            }
            paperInfo.put("fillList",fillList);
        }
        if(judge_uuid_list.size() != 0){
            problemStrategyList.add(new ProblemStrategy("判断题",judge_uuid_list.size(),judgeScore));
            judgeList = new ArrayList<>();
            for (String uuid : judge_uuid_list) {
                judgeList.add(judgeService.selectProblemByUuid(account,uuid));
            }
            paperInfo.put("judgeList",judgeList);
        }
        if(short_uuid_list.size() != 0){
            problemStrategyList.add(new ProblemStrategy("简答题",short_uuid_list.size(),shortScore));
            shortList = new ArrayList<>();
            for (String uuid : short_uuid_list) {
                shortList.add(shortService.selectProblemByUuid(account,uuid));
            }
            paperInfo.put("shortList",shortList);
        }
        if(program_uuid_list.size() != 0){
            problemStrategyList.add(new ProblemStrategy("编程题",short_uuid_list.size(),programScore));
            programList = new ArrayList<>();
            for (String uuid : program_uuid_list) {
                programList.add(programService.selectProblemByUuid(account,uuid));
            }
            paperInfo.put("programList",programList);
        }
        float allSore = 0;
        for (ProblemStrategy problemStrategy : problemStrategyList) {
            allSore += problemStrategy.getProblemAllScore();
        }
        paperStrategy.setProblemStrategyList(JSON.toJSONString(problemStrategyList));
        paperStrategy.setAllScore(allSore);
        paperStrategy.setPaperStrategyName("试卷:"+paperName+"的默认策略(自动生成)");
        Paper paper = Paper.builder()
                .uuid(Utils.randomUuid())
                .paperStrategy_uuid(paperStrategy.getUuid())
                .totalScore(allSore)
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
                .build();

        paperStrategyService.insertPaperStrategy(paperStrategy,account);
        paperService.insertPaper(paper);
        return true;
    }
}
