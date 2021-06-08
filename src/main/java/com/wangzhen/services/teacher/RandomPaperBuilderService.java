package com.wangzhen.services.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/9 0:03
 */
@Service
public class RandomPaperBuilderService {
    private static final String SESSION_PAPER_RANDOM = "SESSION_PAPER_RANDOM";
    private static final String SESSION_PAPERSTRATEGY_RANDOM = "SESSION_PAPERSTRATEGY_RANDOM";
    private static final String SESSION_PAPERSTRATEGY_UUID = "SESSION_PAPERSTRATEGY_UUID";
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


    /**
     * @Description 随机组卷算法
     * @date 2020/3/9 0:03

     * @param paperStrategy_uuid 策略uuid
     * @param knowledgeMap 要考察的知识点集合
     * @return void
     */
    public ResponseMessage randomPaper(String paperStrategy_uuid,
                                       Map<String,List<String>> knowledgeMap,
                                       HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        List<String> singleChoiceKnowledgeList = knowledgeMap.get("单选题");
        List<String> multipleChoiceKnowledgeList = knowledgeMap.get("多选题");
        List<String> fillKnowledgeList = knowledgeMap.get("填空题");
        List<String> judgeKnowledgeList = knowledgeMap.get("判断题");
        List<String> shortKnowledgeList = knowledgeMap.get("简答题");
        List<String> programKnowledgeList = knowledgeMap.get("编程题");
        List<SingleChoice> paper_singleChoiceList = null;
        List<MultipleChoice> paper_multipleChoiceList = null;
        List<Fill> paper_fillList = null;
        List<Short> paper_shortList = null;
        List<Judge> paper_judgeList = null;
        List<Program> paper_programList = null;
        Map<String,Object> map = new LinkedHashMap<>();
        //1.查找策略
        PaperStrategy paperStrategy = paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paperStrategy_uuid);
        //2.解析策略具体内容
        List<ProblemStrategy> problemStrategyList = paperStrategy.problemStrategyList();
        for (ProblemStrategy problemStrategy : problemStrategyList) {
            String problemType = problemStrategy.getProblemType();
            float problemAllScore = problemStrategy.getProblemAllScore();
            int problemNum = problemStrategy.getProblemNum();
            float problemScore = problemStrategy.getProblemScore();
            switch (problemType){
                case "单选题":{
                    //3.1解析单选题(如果有)
                    //3.1.1查找符合知识点要求的单选题集合
                    List<SingleChoice> singleChoiceList = singleChoiceService.selectMyAllSingleChoiceWithKnowledgeMap(teacher.getAccount(),singleChoiceKnowledgeList);
                    if(singleChoiceList.size() < problemNum){
                        //单选题满足考察知识点要求的题库不足,无法出题  终止算法,返回错误信息
                        return ResponseMessage.getInstance().setCode(403).setMsg("单选题满足考察知识点要求的题库不足,无法出题.要求"+problemNum+"道,但仅有"+singleChoiceList.size()+"道.");
                    }
                    //3.1.2随机从 singleChoiceList 中找 problemNum 道单选题
                    paper_singleChoiceList = getPaperProblemList(singleChoiceList,problemNum);
                    map.put("singleChoiceList",paper_singleChoiceList);
                    break;
                }
                case "多选题":{
                    List<MultipleChoice> multipleChoiceList = multipleChoiceService.selectMyAllMultipleChoiceWithKnowledgeMap(teacher.getAccount(),multipleChoiceKnowledgeList);
                    if(multipleChoiceList.size() < problemNum){
                        return ResponseMessage.getInstance().setCode(403).setMsg("多选题满足考察知识点要求的题库不足,无法出题.要求"+problemNum+"道,但仅有"+multipleChoiceList.size()+"道.");
                    }
                    paper_multipleChoiceList = getPaperProblemList(multipleChoiceList,problemNum);
                    map.put("multipleChoiceList",paper_multipleChoiceList);
                    break;
                }
                case "填空题":{
                    List<Fill> fillList = fillService.selectMyAllFillWithKnowledgeMap(teacher.getAccount(),fillKnowledgeList);
                    if(fillList.size() < problemNum){
                        return ResponseMessage.getInstance().setCode(403).setMsg("填空题满足考察知识点要求的题库不足,无法出题.要求"+problemNum+"道,但仅有"+fillList.size()+"道.");
                    }
                    paper_fillList = getPaperProblemList(fillList,problemNum);
                    map.put("fillList",paper_fillList);
                    break;
                }
                case "判断题":{
                    List<Judge> judgeList = judgeService.selectMyAllJudgeWithKnowledgeMap(teacher.getAccount(),judgeKnowledgeList);
                    if(judgeList.size() < problemNum){
                        return ResponseMessage.getInstance().setCode(403).setMsg("判断题满足考察知识点要求的题库不足,无法出题.要求"+problemNum+"道,但仅有"+judgeList.size()+"道.");
                    }
                    paper_judgeList = getPaperProblemList(judgeList,problemNum);
                    map.put("judgeList",paper_judgeList);
                    break;
                }
                case "简答题":{
                    List<Short> shortList = shortService.selectMyAllShortWithKnowledgeMap(teacher.getAccount(),shortKnowledgeList);
                    if(shortList.size() < problemNum){
                        return ResponseMessage.getInstance().setCode(403).setMsg("简答题满足考察知识点要求的题库不足,无法出题.要求"+problemNum+"道,但仅有"+shortList.size()+"道.");
                    }
                    paper_shortList = getPaperProblemList(shortList,problemNum);
                    map.put("shortList",paper_shortList);
                    break;
                }
                case "编程题":{
                    List<Program> programList = programService.selectMyAllShortWithKnowledgeMap(teacher.getAccount(),programKnowledgeList);
                    if(programList.size() < problemNum){
                        return ResponseMessage.getInstance().setCode(403).setMsg("简答题满足考察知识点要求的题库不足,无法出题.要求"+problemNum+"道,但仅有"+programList.size()+"道.");
                    }
                    List<Program> paper_programList1 = getPaperProblemList(programList, problemNum);
                    map.put("programList",paper_programList1);
                    break;
                }
            }
        }

        //4.暂时保存
        session.setAttribute(SESSION_PAPER_RANDOM,map);
        session.setAttribute(SESSION_PAPERSTRATEGY_RANDOM,paperStrategy);
        session.setAttribute(SESSION_PAPERSTRATEGY_UUID,paperStrategy_uuid);
        return ResponseMessage.getInstance().setCode(200).setMsg("合成成功").setData(map);
    }

    /**
     * @Description
     * @date 2020/3/14 18:26
     * @param course_uuid 课程uuid
     * @param during 考试时间
     * @param sort
     * @param begin 最早开考时间
     * @param end 最迟开考时间
     * @param session
     * @return void
     */
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
        String paperStrategy_uuid = (String) session.getAttribute(SESSION_PAPERSTRATEGY_UUID);
        LinkedHashMap<String,Object> paperInfo = (LinkedHashMap<String, Object>) session.getAttribute(SESSION_PAPER_RANDOM);
        PaperStrategy paperStrategy = (PaperStrategy) session.getAttribute(SESSION_PAPERSTRATEGY_RANDOM);
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
                    List<Short> shortList = (List<Short>) paperInfo.get("shortList");
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
                .paperStrategy_uuid(paperStrategy_uuid)
                .build();
        paperService.insertPaper(paper);
        return true;
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
//        for (int i = 0; i < problemNum; i++) {
//            Integer randomIdx = random.nextInt(problemList.size());
//            while(randomIdxList.contains(randomIdx)){
//                //当生成了重复的随机数时,算法可能有性能故障
//                randomIdx = random.nextInt(problemList.size());
//            }
//            randomIdxList.add(randomIdx);
//        }
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
        for (int i = 0; i < problemSize; i++){
            //初始化数组
            problemSizeList.add(i);
        }
        SecureRandom secureRandom = new SecureRandom();
        List<Integer> returnRandomIdxList = new ArrayList<>();
        for (int i = 0; i < problemNum; i++) {
            Integer randomIdx = problemSizeList.get(secureRandom.nextInt(problemSizeList.size()));
            problemSizeList.remove(randomIdx);
            returnRandomIdxList.add(randomIdx);
        }
        return returnRandomIdxList;
    }
    @Deprecated// 性能故障 当problemNum > 9999 时,消耗时间长
    private static List<Integer> get(int problemSize,int problemNum){
        Random random = new Random(System.currentTimeMillis() % 1000);
        List<Integer> randomIdxList = new ArrayList<>();
        for (int i = 0; i < problemNum; i++) {
            Integer randomIdx = random.nextInt(problemSize);
            while(randomIdxList.contains(randomIdx)){
                //当生成了重复的随机数时,算法可能有性能故障
                randomIdx = random.nextInt(problemSize);
            }
            randomIdxList.add(randomIdx);
        }
        return randomIdxList;
    }
}
