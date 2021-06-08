package com.wangzhen.controllers.student;

import com.alibaba.fastjson.JSON;
import com.arcsoft.face.Face3DAngle;
import com.wangzhen.models.Paper;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.ProblemStrategy;
import com.wangzhen.models.StudentPaperAnswer;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.manager.StudentService;
import com.wangzhen.services.student.StudentPaperAnswerService;
import com.wangzhen.services.teacher.PaperService;
import com.wangzhen.services.teacher.PaperStrategyService;
import com.wangzhen.utils.*;
import com.wangzhen.utils.compiler.CompilerUtil;
import com.wangzhen.utils.compiler.ResultEqualUtil;
import com.wangzhen.utils.compiler.RunInfo;
import com.wangzhen.utils.compiler.RunResultObj;
import com.wangzhen.utils.faceidentity.FaceIdentityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.awt.geom.FlatteningPathIterator;
import java.io.IOException;
import java.util.*;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/19 16:22
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentHomeController {
    private static final String WANTTAKEEXAM_TAG = "WANTTAKEEXAM_TAG";
    private static final String WANTTAKEEXAM_PAPER = "WANTTAKEEXAM_PAPER";//去除答案后的试卷
    private static final String WANTTAKEEXAM_PAPER_INFO = "WANTTAKEEXAM_PAPER_INFO";//去除答案后的试卷
    private static final String WANTTAKEEXAM_PAPER_BACK = "WANTTAKEEXAM_PAPER_BACK";//备份的试卷
    private static final String WANTTAKEEXAM_PAPER_INFO_BACK = "WANTTAKEEXAM_PAPER_INFO_BACK";//备份的试卷信息
    private static final String WANTTAKEEXAM_BEGIN = "WANTTAKEEXAM_BEGIN";//备份的试卷信息
    private static final String WANTTAKEEXAM_FIRSTPROBLEMNODE = "WANTTAKEEXAM_FIRSTPROBLEMNODE";
    private static final String WANTTAKEEXAM_PROBLEMNODEUTIL = "WANTTAKEEXAM_PROBLEMNODEUTIL";
    private static final String WANTTAKEEXAM_SINGLECHOICELIST = "WANTTAKEEXAM_SINGLECHOICELIST";
    private static final String WANTTAKEEXAM_MULTIPLECHOICELIST = "WANTTAKEEXAM_MULTIPLECHOICELIST";
    private static final String WANTTAKEEXAM_FILLLIST = "WANTTAKEEXAM_FILLLIST";
    private static final String WANTTAKEEXAM_SHORTLIST = "WANTTAKEEXAM_SHORTLIST";
    private static final String WANTTAKEEXAM_JUDGELIST = "WANTTAKEEXAM_JUDGELIST";
    private static final String WANTTAKEEXAM_PROGRAMLIST = "WANTTAKEEXAM_PROGRAMLIST";
    private static final String WANTTAKEEXAM_PAPER_UUID = "WANTTAKEEXAM_PAPER_UUID";
    private static final String WANTTAKEEXAM_PAPERSTRATEGY = "WANTTAKEEXAM_PAPERSTRATEGY";
    @Autowired
    private PaperService paperService;
    @Autowired
    private PaperStrategyService paperStrategyService;
    @Autowired
    private StudentPaperAnswerService studentPaperAnswerService;
    @Autowired
    private StudentService studentService;
    @RequestMapping("/selectSimplePaperForStudentToDisplay")
    public List<Paper> selectSimplePaperForStudentToDisplay(HttpSession session){
        Student student = Utils.getUser(session, Student.class);
        List<Paper> papers = paperService.selectSimplePaperForStudentToDisplay(student.getCourse_uuid_list());
        return papers;
    }
    @RequestMapping("/selectPaperForStudentToExam")
    public Paper selectPaperForStudentToExam(@RequestParam("uuid") String uuid){
        return paperService.selectPaperForStudentToExam(uuid);
    }

    /**
     * @Description 考试前的准备工作
     * @date 2020/3/23 13:48
     * @param uuid
     * @param session
     * @return void
     */
    @RequestMapping("/prepareToTakeExam")
    public ResponseMessage wantTakeExam(@RequestParam("uuid") String uuid,HttpSession session){
        Student student = Utils.getUser(session, Student.class);
        StudentPaperAnswer studentPaperAnswer = studentPaperAnswerService.selectStudentPaperAnswerByPaper_uuidAndStudent_uuid(uuid, student.getUuid());
        if(studentPaperAnswer != null){
            return ResponseMessage.getInstance().setCode(403).setMsg("您已参加完考试");
        }
        Paper paper = paperService.selectPaperByPaper_uuid(uuid);
        String paperStrategy_uuid = paper.getPaperStrategy_uuid();
        PaperStrategy paperStrategy = paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paperStrategy_uuid);
        session.setAttribute(WANTTAKEEXAM_PAPERSTRATEGY,paperStrategy);
        session.setAttribute(WANTTAKEEXAM_TAG,true);
        session.setAttribute(WANTTAKEEXAM_PAPER_UUID,uuid);
        LinkedHashMap<String, Object> paperInfo = paper.getPaperInfo();//正常顺序的paperInfo
        session.setAttribute(WANTTAKEEXAM_PAPER_INFO_BACK,paperInfo.clone());
        //去除答案
        for (String problemType : paperInfo.keySet()) {
            switch (problemType){
                case "singleChoiceList":{
                    List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get(problemType);
                    removeAnswer(singleChoiceList,SingleChoice.class);
                    paperInfo.put("singleChoiceList",singleChoiceList);
                    session.setAttribute(WANTTAKEEXAM_SINGLECHOICELIST,singleChoiceList);

                    break;
                }
                case "multipleChoiceList":{
                    List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get(problemType);
                    paperInfo.put("multipleChoiceList",removeAnswer(multipleChoiceList,MultipleChoice.class));
                    session.setAttribute(WANTTAKEEXAM_MULTIPLECHOICELIST,multipleChoiceList);
                    break;
                }
                case "fillList":{
                    List<Fill> fillList = (List<Fill>) paperInfo.get(problemType);
                    paperInfo.put("fillList",removeAnswer(fillList,Fill.class));
                    session.setAttribute(WANTTAKEEXAM_FILLLIST,fillList);
                    break;
                }
                case "shortList":{
                    List<Short> shortList = (List<Short>) paperInfo.get(problemType);
                    paperInfo.put("shortList",removeAnswer(shortList,Short.class));
                    session.setAttribute(WANTTAKEEXAM_SHORTLIST,shortList);
                    break;
                }
                case "judgeList":{
                    List<Judge> judgeList = (List<Judge>) paperInfo.get(problemType);
                    paperInfo.put("judgeList",removeAnswer(judgeList,Judge.class));
                    session.setAttribute(WANTTAKEEXAM_JUDGELIST,judgeList);
                    break;
                }
                case "programList":{
                    List<Program> programList = (List<Program>) paperInfo.get(problemType);
                    paperInfo.put("programList",removeAnswer(programList,Program.class));
                    session.setAttribute(WANTTAKEEXAM_PROGRAMLIST,programList);
                    break;
                }
            }
        }
        //打乱考题顺序
        if(paper.getSort()){//打乱顺序
            for (String problemType : paperInfo.keySet()) {
                switch (problemType){
                    case "singleChoiceList":{
                        List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get(problemType);
                        paperInfo.put("singleChoiceList",sort(singleChoiceList));
                        break;
                    }
                    case "multipleChoiceList":{
                        List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get(problemType);
                        paperInfo.put("multipleChoiceList",sort(multipleChoiceList));
                        break;
                    }
                    case "fillList":{
                        List<Fill> fillList = (List<Fill>) paperInfo.get(problemType);
                        paperInfo.put("fillList",sort(fillList));
                        break;
                    }
                    case "shortList":{
                        List<Short> shortList = (List<Short>) paperInfo.get(problemType);
                        paperInfo.put("shortList",sort(shortList));
                        break;
                    }
                    case "judgeList":{
                        List<Judge> judgeList = (List<Judge>) paperInfo.get(problemType);
                        paperInfo.put("judgeList",sort(judgeList));
                        break;
                    }
                    case "programList":{
                        List<Program> programList = (List<Program>) paperInfo.get(problemType);
                        paperInfo.put("programList",sort(programList));
                        break;
                    }
                }
            }
        }

        //将打乱顺序后的paperInfo转换成双链表
        ProblemNodeUtil problemNodeUtil = new ProblemNodeUtil();
        for (String problemType : paperInfo.keySet()) {
            switch (problemType){
                case "singleChoiceList":{
                    List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get(problemType);
                    for (int i = 0; i < singleChoiceList.size(); i++) {
                        problemNodeUtil.addNewNode("单选题",singleChoiceList.get(i),i);
                    }
                    break;
                }
                case "multipleChoiceList":{
                    List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get(problemType);
                    for (int i = 0; i < multipleChoiceList.size(); i++) {
                        problemNodeUtil.addNewNode("多选题",multipleChoiceList.get(i),i);
                    }
                    break;
                }
                case "fillList":{
                    List<Fill> fillList = (List<Fill>) paperInfo.get(problemType);
                    for (int i = 0; i < fillList.size(); i++) {
                        problemNodeUtil.addNewNode("填空题",fillList.get(i),i);
                    }
                    break;
                }
                case "shortList":{
                    List<Short> shortList = (List<Short>) paperInfo.get(problemType);
                    for (int i = 0; i < shortList.size(); i++) {
                        problemNodeUtil.addNewNode("简答题",shortList.get(i),i);
                    }
                    break;
                }
                case "judgeList":{
                    List<Judge> judgeList = (List<Judge>) paperInfo.get(problemType);
                    for (int i = 0; i < judgeList.size(); i++) {
                        problemNodeUtil.addNewNode("判断题",judgeList.get(i),i);
                    }
                    break;
                }
                case "programList":{
                    List<Program> programList = (List<Program>) paperInfo.get(problemType);
                    for (int i = 0; i < programList.size(); i++) {
                        problemNodeUtil.addNewNode("编程题",programList.get(i),i);
                    }
                    break;
                }
            }
        }

        session.setAttribute(WANTTAKEEXAM_PROBLEMNODEUTIL,problemNodeUtil);
        session.setAttribute(WANTTAKEEXAM_PAPER,paper);
        session.setAttribute(WANTTAKEEXAM_PAPER_INFO,paperInfo);
        session.setAttribute(WANTTAKEEXAM_BEGIN,System.currentTimeMillis()-1000);
        return ResponseMessage.getInstance().setCode(200).setMsg("准备工作完成");
    }



    @RequestMapping("/takeExam_getProblem")
    public Map<String,Object> takeExam(HttpSession session,@RequestParam("totalProblemIdx") Integer totalProblemIdx){
        Map<String,Object> map = new HashMap<>();
        ProblemNodeUtil problemNodeUtil = (ProblemNodeUtil) session.getAttribute(WANTTAKEEXAM_PROBLEMNODEUTIL);
        ProblemNode problemNode = problemNodeUtil.getProblemNode(totalProblemIdx);
        map.put("currentProblemData",problemNode.problemData);
        map.put("currentProblemType",problemNode.problemType);
        map.put("currentProblemIdx",problemNode.problemIdx);
        map.put("currentTotalProblemIdx",problemNode.totalProblemIdx);

        map.put("lastProblemType",problemNode.lastProblemNode != null ? problemNode.lastProblemNode.problemType : -1);
        map.put("lastTotalProblemIdx",problemNode.lastProblemNode != null ? problemNode.lastProblemNode.totalProblemIdx : -1);
        map.put("lastProblemIdx",problemNode.lastProblemNode != null ? problemNode.lastProblemNode.problemIdx : -1);

        map.put("nextProblemType",problemNode.nextProblemNode != null ? problemNode.nextProblemNode.problemType : -1);
        map.put("nextTotalProblemIdx",problemNode.nextProblemNode != null ? problemNode.nextProblemNode.totalProblemIdx : -1);
        map.put("nextProblemIdx",problemNode.nextProblemNode != null ? problemNode.nextProblemNode.problemIdx : -1);
        return map;
    }

    @RequestMapping("/takeExam_getSimplePaperInfo")
    public Map<String,Object> getSimplePaperInfo(HttpSession session){
        Student student = Utils.getUser(session, Student.class);
        Paper paper = (Paper) session.getAttribute(WANTTAKEEXAM_PAPER);
        Map<String,Object> returnMap = new HashMap<>();
        StudentPaperAnswer studentPaperAnswer = studentPaperAnswerService.selectStudentPaperAnswerByPaper_uuidAndStudent_uuid(paper.getUuid(), student.getUuid());
        if(studentPaperAnswer != null){
            returnMap.put("errorCode",203);
            returnMap.put("errorMsg","已参加过考试");
            return returnMap;
        }


        LinkedHashMap<String, Object> paperInfo  = (LinkedHashMap<String, Object>) session.getAttribute(WANTTAKEEXAM_PAPER_INFO);
        LinkedList<Info> simplePaperInfoList = new LinkedList<>();//每道题的数量
        PaperStrategy paperStrategy = (PaperStrategy) session.getAttribute(WANTTAKEEXAM_PAPERSTRATEGY);
        List<ProblemStrategy> problemStrategyList = paperStrategy.problemStrategyList();
        for (ProblemStrategy problemStrategy : problemStrategyList) {
            simplePaperInfoList.add(new Info(problemTypeToEnglish(problemStrategy.getProblemType()),problemStrategy.getProblemNum()));
        }
        returnMap.put("student_uuid",student.getUuid());
        returnMap.put("paper_uuid",paper.getUuid());
        returnMap.put("simplePaperInfo",simplePaperInfoList);
        returnMap.put("studentName",student.getName());
        returnMap.put("studentSno",student.getSno());
        returnMap.put("begin",session.getAttribute(WANTTAKEEXAM_BEGIN));
        returnMap.put("end",(Long)session.getAttribute(WANTTAKEEXAM_BEGIN)+paper.getDuring()*1000*60);
        returnMap.put("during",paper.getDuring());
        returnMap.put("openFaceIdentity",paper.getOpenFaceIdentity());//是否开启人脸识别考试

        ProblemNodeUtil problemNodeUtil = (ProblemNodeUtil) session.getAttribute(WANTTAKEEXAM_PROBLEMNODEUTIL);
        ProblemNode problemNode = problemNodeUtil.firstProblemNode;
        returnMap.put("currentProblemData",problemNode.problemData);
        returnMap.put("currentProblemType",problemNode.problemType);
        returnMap.put("currentProblemIdx",problemNode.problemIdx);
        returnMap.put("currentTotalProblemIdx",problemNode.totalProblemIdx);

        returnMap.put("lastProblemType", -1);
        returnMap.put("lastTotalProblemIdx",-1);
        returnMap.put("lastProblemIdx", -1);

        returnMap.put("nextProblemType",problemNode.nextProblemNode != null ? problemNode.nextProblemNode.problemType : -1);
        returnMap.put("nextTotalProblemIdx",problemNode.nextProblemNode != null ? problemNode.nextProblemNode.totalProblemIdx : -1);
        returnMap.put("nextProblemIdx",problemNode.nextProblemNode != null ? problemNode.nextProblemNode.problemIdx : -1);

        returnMap.put("openFaceIdentity",paper.getOpenFaceIdentity());

        return returnMap;
    }

    @RequestMapping("/takeExam_saveAnswer")
    public ResponseMessage saveExamAnswer(HttpSession session,@RequestParam("totalProblemIdx")Integer totalProblemIdx,@RequestParam("answer") String answer){
        ProblemNodeUtil problemNodeUtil = (ProblemNodeUtil) session.getAttribute(WANTTAKEEXAM_PROBLEMNODEUTIL);
        ProblemNode problemNode = problemNodeUtil.getProblemNode(totalProblemIdx);

        try{
            switch (problemNode.problemType){
                case "单选题":{
                    SingleChoice singleChoice = (SingleChoice) problemNode.problemData;
                    singleChoice.setAnswer( answer);
                    break;
                }
                case "多选题":{
                    MultipleChoice multipleChoice = (MultipleChoice) problemNode.problemData;
                    multipleChoice.setAnswer(Arrays.asList(answer.split(",")));
                    break;
                }
                case "填空题":{
                    Fill fill = (Fill) problemNode.problemData;
                    fill.setAnswer(JSON.parseArray(answer,String.class));
                    break;
                }
                case "简答题":{
                    Short aShort = (Short) problemNode.problemData;
                    aShort.setAnswer(answer);
                    break;
                }
                case "判断题":{
                    Judge judge = (Judge)problemNode.problemData;
                    judge.setAnswer(answer.equals("true")?true:false);
                    break;
                }
                case "编程题":{
                    Program program = (Program)problemNode.problemData;
                    program.setAnswer(answer);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + problemNode.problemType);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }


        return ResponseMessage.OK;
    }


    @RequestMapping("/takeExam_overExam")
    public ResponseMessage overExam(HttpSession session){
        Student student = Utils.getUser(session, Student.class);
        ProblemNodeUtil problemNodeUtil = (ProblemNodeUtil) session.getAttribute(WANTTAKEEXAM_PROBLEMNODEUTIL);
        boolean canOverExam = problemNodeUtil.canOverExam();
        if(!canOverExam){//如果不能结束考试
            return new ResponseMessage().setCode(403).setMsg("答题未完成,无法结束考试");
        }
        //可以结束考试
        //paperInfo 为保存了用户的答案,且不是乱序的paperInfo 因为对象引用相同,提交了答案之后,此paperInfo的答案也更改
        LinkedHashMap<String,Object> paperInfo = (LinkedHashMap<String, Object>) session.getAttribute(WANTTAKEEXAM_PAPER_INFO_BACK);

        //开始阅卷
        String paper_uuid = (String) session.getAttribute(WANTTAKEEXAM_PAPER_UUID);
        Paper paper = paperService.selectPaperByPaper_uuid(paper_uuid);//查找数据库里的试卷
        LinkedHashMap<String, Object> dataBasePaperInfo = paper.getPaperInfo();//数据库里保存的原本的paperInfo,答案是正确的那个
        PaperStrategy paperStrategy = (PaperStrategy) session.getAttribute(WANTTAKEEXAM_PAPERSTRATEGY);
        List<ProblemStrategy> problemStrategyList = paperStrategy.problemStrategyList();



        Float singgleChoiceTotalScore = 0f;
        Float multipleChoiceTotalScore = 0f;
        Float judgeTotalScore = 0f;
        Float programTotalScore = 0f;
        LinkedHashMap<Integer,Object> answers = new LinkedHashMap<>();
        Integer totalProblemIdx = 1;
        Boolean complete = true;//是否阅卷完成
        for (ProblemStrategy problemStrategy : problemStrategyList) {
            String problemType = problemStrategy.getProblemType();
            Integer problemNum = problemStrategy.getProblemNum();
            Float problemScore = problemStrategy.getProblemScore();
            switch (problemType){
                case "单选题":{
                    List<SingleChoice> singleChoiceList = (List<SingleChoice>) paperInfo.get("singleChoiceList");
                    List<SingleChoice> dataBaseSingleChoiceList = (List<SingleChoice>) dataBasePaperInfo.get("singleChoiceList");
                    for (int i = 0; i < singleChoiceList.size(); i++) {
                        SingleChoice singleChoice = singleChoiceList.get(i);
                        SingleChoice dataBaseSingleChoice = dataBaseSingleChoiceList.get(i);
                        if(singleChoice.getAnswer().equals(dataBaseSingleChoice.getAnswer())){
                            singgleChoiceTotalScore += problemScore;
                        }
                        answers.put(totalProblemIdx++,singleChoice.getAnswer());
                    }
                    break;
                }
                case "多选题":{
                    List<MultipleChoice> multipleChoiceList = (List<MultipleChoice>) paperInfo.get("multipleChoiceList");
                    List<MultipleChoice> dataBaseMultipleChoiceList = (List<MultipleChoice>) dataBasePaperInfo.get("multipleChoiceList");
                    for (int i = 0; i < multipleChoiceList.size(); i++) {
                        MultipleChoice multipleChoice = multipleChoiceList.get(i);
                        MultipleChoice dataBaseMultipleChoice = dataBaseMultipleChoiceList.get(i);
                        if(multipleChoice.getAnswer().containsAll(dataBaseMultipleChoice.getAnswer())){
                            multipleChoiceTotalScore += problemScore;
                        }
                        answers.put(totalProblemIdx++,multipleChoice.getAnswer());
                    }
                    break;
                }
                //填空题不自动阅卷
                case "判断题":{
                    List<Judge> judgeList = (List<Judge>) paperInfo.get("judgeList");
                    List<Judge> dataBaseJudgeList = (List<Judge>) dataBasePaperInfo.get("judgeList");
                    for (int i = 0; i < judgeList.size(); i++) {
                        Judge judge = judgeList.get(i);
                        Judge dataBaseJudge = dataBaseJudgeList.get(i);
                        if(judge.getAnswer().equals(dataBaseJudge.getAnswer())){
                            judgeTotalScore += problemScore;
                        }
                        answers.put(totalProblemIdx++,judge.getAnswer());
                    }
                    break;
                }
                case "填空题":{
                    List<Fill> fillList = (List<Fill>) paperInfo.get("fillList");//不阅卷 只保存答案
                    complete = false;
                    for (int i = 0; i < fillList.size(); i++) {
                        Fill fill = fillList.get(i);
                        answers.put(totalProblemIdx++,fill.getAnswer());
                    }
                    break;
                }
                case "简答题":{
                    List<Short> shortList = (List<Short>) paperInfo.get("shortList");//不阅卷 只保存答案
                    complete = false;
                    for (int i = 0; i < shortList.size(); i++) {
                        Short aShort = shortList.get(i);
                        answers.put(totalProblemIdx++,aShort.getAnswer());
                    }
                    break;
                }
                case "编程题":{//也应自动阅卷 待完善.....
                    List<Program> programList = (List<Program>) paperInfo.get("programList");//不阅卷 只保存答案
                    for (int i = 0; i < programList.size(); i++) {
                        Program program = programList.get(i);
                        String sourceCode = program.getAnswer();
                        List<Option> ioList = program.getIoList();
                        String[] inputs = new String[ioList.size()];
                        for (int j = 0; j < ioList.size(); j++) {
                            inputs[j] = new String(ioList.get(j).getInput());
                        }
                        RunInfo runInfo = CompilerUtil.runMainMethod(sourceCode, inputs);
                        List<RunResultObj> runResultObjList = runInfo.getRunResultObjList();
                        String[] outputs = new String[ioList.size()];
                        for (int j = 0; j < runResultObjList.size(); j++) {
                            String runResult = runResultObjList.get(j).getRunResult();
                            if(ResultEqualUtil.equals(ioList.get(j).getOutput(),runResult)){
                                programTotalScore += problemScore/ioList.size();
                            }
                            outputs[j] = new String(runResult);
                        }
                        answers.put(totalProblemIdx++,outputs);
                    }
                    break;
                }
            }
        }
        StudentPaperAnswer studentPaperAnswer = new StudentPaperAnswer();
        studentPaperAnswer.setJudgeScore(judgeTotalScore);
        studentPaperAnswer.setMultipleChoiceScore(multipleChoiceTotalScore);
        studentPaperAnswer.setSingleChoiceScore(singgleChoiceTotalScore);
        studentPaperAnswer.setProgramScore(programTotalScore);
        studentPaperAnswer.setAnswers(answers);
        studentPaperAnswer.setPaper_uuid(paper_uuid);
        studentPaperAnswer.setStudent_uuid(student.getUuid());
        studentPaperAnswer.setUuid(Utils.randomUuid());
        if(complete){
            studentPaperAnswer.setTotalScore(singgleChoiceTotalScore + multipleChoiceTotalScore + judgeTotalScore + programTotalScore);
            studentPaperAnswer.setPass(studentPaperAnswer.getTotalScore() > paperStrategy.getAllScore() * 0.6);
        }
        studentPaperAnswer.setComplete(complete);
        studentPaperAnswer.setPaperTotalScore(paperStrategy.getAllScore());
        studentPaperAnswerService.insertStudentPaperAnswer(studentPaperAnswer);
        return new ResponseMessage().setCode(200).setMsg("考试已结束");
    };

    /**
     * @Description
     * Code:
     *  1001:没有检测到人脸
     *  200: 检测正常，返回人脸偏角度
     * @date 2020/3/25 11:59
     * @param face
     * @param session
     * @return com.wangzhen.utils.ResponseMessage
     */
    @RequestMapping("/takeExam_faceDetect")
    public ResponseMessage faceDetect(@RequestParam("face") MultipartFile face,HttpSession session){
        try {
            Student student = Utils.getUser(session, Student.class);
            String faceFeatureData = student.getFaceFeatureData();
            List<Face3DAngle> face3DAngleList = new FaceIdentityUtil().getFace3DAngle(face.getInputStream());
            if(face3DAngleList.size()<1){
                //没有检测到人脸
                return ResponseMessage.getInstance().setCode(1001).setMsg("没有检测到人脸,请回到座位");
            }else{
                Face3DAngle face3DAngle = face3DAngleList.get(0);
                float pitch = face3DAngle.getPitch();//上下低头 上正 下负
                float roll = face3DAngle.getRoll();//偏头 左偏 负 右偏正
                float yaw = face3DAngle.getYaw();//左右扭头 左负 右正
                return ResponseMessage.getInstance().setCode(200).setMsg("检测正常").setData(face3DAngleList.get(0));
            }
        } catch (IOException e) {
            return ResponseMessage.FAIL;
        }
    }

    @RequestMapping("/getMyScore")
    public List<StudentPaperAnswer> getMyScore(HttpSession session){
        Student student = Utils.getUser(session, Student.class);
        List<StudentPaperAnswer> studentPaperAnswerList = studentPaperAnswerService.getMyScore(student.getUuid());
        for (StudentPaperAnswer studentPaperAnswer : studentPaperAnswerList) {
            studentPaperAnswer.setStudent(studentService.selectStudentByUuid(studentPaperAnswer.getStudent_uuid()));
            studentPaperAnswer.setPaper(paperService.selectPaperByPaper_uuid(studentPaperAnswer.getPaper_uuid()));
        }
        return studentPaperAnswerList;
    }

    private <T> List<T> sort(List<T> problemList) {
        Random random = new Random(System.currentTimeMillis());
        List<T> paper_problemList = new ArrayList<>(problemList.size());
        List<Integer> randomIdxList = getRandomIdxList(problemList.size(),problemList.size());
        for (int i = 0; i < problemList.size(); i++) paper_problemList.add(problemList.get(randomIdxList.get(i)));
        return paper_problemList;
    }
    private <T> List<T> removeAnswer(List<T> problemList,Class<T> clazz) {
        if(clazz == SingleChoice.class){
            for (T t : problemList) {
                SingleChoice singleChoice = (SingleChoice)t;
                singleChoice.setAnswer("");
                singleChoice.setAllNum(null);
                singleChoice.setVideoPath(null);

                problemList.set(problemList.indexOf(t),(T)singleChoice);
            }
        }
        if(clazz == MultipleChoice.class){
            for (T t : problemList) {
                MultipleChoice singleChoice = (MultipleChoice)t;
                singleChoice.setAnswer(new ArrayList<>());
                singleChoice.setAllNum(null);
                singleChoice.setVideoPath(null);
                problemList.set(problemList.indexOf(t),(T)singleChoice);
            }
        }
        if(clazz == Fill.class){
            for (T t : problemList) {
                Fill singleChoice = (Fill)t;
                singleChoice.setAnswer(new ArrayList<>());
                singleChoice.setAllNum(null);
                problemList.set(problemList.indexOf(t),(T)singleChoice);
                singleChoice.setVideoPath(null);
            }
        }
        if(clazz == Short.class){
            for (T t : problemList) {
                Short singleChoice = (Short)t;
                singleChoice.setAnswer("");
                singleChoice.setAllNum(null);
                singleChoice.setVideoPath(null);
                problemList.set(problemList.indexOf(t),(T)singleChoice);
            }
        }
        if(clazz == Judge.class){
            for (T t : problemList) {
                Judge singleChoice = (Judge)t;
                singleChoice.setAllNum(null);
                singleChoice.setAnswer(null);
                singleChoice.setVideoPath(null);
                problemList.set(problemList.indexOf(t),(T)singleChoice);
            }
        }
        if(clazz == Program.class){
            for (T t : problemList) {
                Program program = (Program)t;
                program.setAnswer("");
                program.setAllNum(null);
                program.setVideoPath(null);
                problemList.set(problemList.indexOf(t),(T)program);
            }
        }
        return problemList;
    }
    
    private List<Integer> getRandomIdxList(int problemSize,int problemNum){
        List<Integer> problemSizeList = new ArrayList<>(problemSize);
        for (int i = 0; i < problemSize; i++){
            problemSizeList.add(i);//初始化数组
        }

        Random random = new Random(System.currentTimeMillis() % 1000);

        List<Integer> returnRandomIdxList = new ArrayList<>();
        for (int i = 0; i < problemNum; i++) {
            Integer randomIdx = problemSizeList.get(random.nextInt(problemSizeList.size()));
            problemSizeList.remove(randomIdx);
            returnRandomIdxList.add(randomIdx);
        }
        return returnRandomIdxList;

    }
    private String problemTypeToChinese(String problemType){
        switch (problemType){
            case "singleChoiceList":{
                return "单选题";
            }
            case "multipleChoiceList":{
                return "多选题";
            }
            case "fillList":{
                return "填空题";
            }
            case "shortList":{
                return "简答题";
            }
            case "judgeList":{
                return "判断题";
            }
            case "programList":{
                return "编程题";
            }
        }
        return "";
    }
    private String problemTypeToEnglish(String problemType){
        switch (problemType){
            case "单选题":{
                return "singleChoiceList";
            }
            case "多选题":{
                return "multipleChoiceList";
            }
            case "填空题":{
                return "fillList";
            }
            case "简答题":{
                return "shortList";
            }
            case "判断题":{
                return "judgeList";
            }
            case "编程题":{
                return "programList";
            }
        }
        return "";
    }

    private class Info{
        private String problemType;
        private Integer problemSize;

        public Info(String problemType, Integer problemSize) {
            this.problemType = problemType;
            this.problemSize = problemSize;
        }

        public String getProblemType() {
            return problemType;
        }

        public void setProblemType(String problemType) {
            this.problemType = problemType;
        }

        public Integer getProblemSize() {
            return problemSize;
        }

        public void setProblemSize(Integer problemSize) {
            this.problemSize = problemSize;
        }
    }

}

