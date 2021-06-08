package com.wangzhen.services.teacher.problemservice;
import com.alibaba.fastjson.JSON;
import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.models.problem.SingleChoice;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.dao.teacher.problemdao.MultipleChoiceDao;
import com.wangzhen.models.College;
import com.wangzhen.models.problem.KnowledgeArray;
import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.services.admin.CollegeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/10 22:34
 */
@Slf4j
@Service
public class MultipleChoiceService {
    @Autowired
    private MultipleChoiceDao multipleChoiceDao;
    @Autowired
    private CollegeService collegeService;
    @Autowired
    @Qualifier("videoTypeList")
    private List<String> videoTypeList;
    @Autowired
    private KnowledgeArrayService knowledgeArrayService;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;

    public MultipleChoice selectProblemByUuid(String account, String uuid){
        return multipleChoiceDao.selectProblemByUuid(account,uuid);
    }

    public List<MultipleChoice> selectMyAllMultipleChoice(String account){
        return multipleChoiceDao.selectMyAllMultipleChoice(account);
    }

    public List<MultipleChoice> selectMyCollegeAllMultipleChoice(String collegeName){
        College college = collegeService.selectCollegeByName(collegeName);
        if(college == null){
            log.error("查找教师所在学院的单选题库失败：学院: " + collegeName + " ,不存在");
            return null;
        }
        return multipleChoiceDao.selectMyCollegeAllMultipleChoice(college.getUuid());
    }

    public boolean insertMultipleChoice(MultipleChoice multipleChoice, String teacherAccount){
        return multipleChoiceDao.insertMultipleChoice(
                multipleChoice.getUuid(),
                multipleChoice.getProblem(),
                multipleChoice.getChoice_a(),
                multipleChoice.getChoice_b(),
                multipleChoice.getChoice_c(),
                multipleChoice.getChoice_d(),
                multipleChoice.getChoice_e(),
                multipleChoice.getChoice_f(),
                multipleChoice.getChoice_g(),
                multipleChoice.getChoice_h(),
                JSON.toJSONString(multipleChoice.getAnswer()),
                multipleChoice.getAnalysis(),
                JSON.toJSONString(multipleChoice.getDifficultyVal()),
                JSON.toJSONString(multipleChoice.getKnowledgeList()),
                multipleChoice.getVideoPath(),
                teacherAccount);
    }
    public boolean createMultipleChoiceDataTable(String account){
        return multipleChoiceDao.createMultipleChoiceDataTable(account);
    }


//    public boolean updateMultipleChoiceInfo(String problem, String choice_a, String choice_b,
//                                   String choice_c, String choice_d, String choice_e,
//                                   String choice_f, String choice_g, String choice_h,
//                                   String answer, float difficultyVal, String knowledgeList,String analysis,  String uuid){
//        return multipleChoiceDao.updateMultipleChoiceInfoByUuid(problem,choice_a,choice_b,choice_c,choice_d,choice_e,choice_f,choice_g,choice_h,answer,difficultyVal,analysis,knowledgeList,uuid);
//    }
    public boolean updateMultipleChoiceInfo(MultipleChoice problem,String account){
        return multipleChoiceDao.updateMultipleChoiceInfoByUuid(account,problem.getProblem(),problem.getChoice_a(),problem.getChoice_b(),
                problem.getChoice_c(),problem.getChoice_d(),problem.getChoice_e(),
                problem.getChoice_f(),problem.getChoice_g(),problem.getChoice_h(),
                JSON.toJSONString(problem.getAnswer()),problem.getDifficultyVal(), problem.getAnalysis(),
                JSON.toJSONString(problem.getKnowledgeList()), problem.getUuid());
    }

    public boolean updateMultipleChoiceInfo(MultipleChoice problem,String videoPath,String account){
        return multipleChoiceDao.updateMultipleChoiceInfoWithVideoByUuid(
                account, problem.getProblem(), problem.getChoice_a(), problem.getChoice_b(),
                problem.getChoice_c(), problem.getChoice_d(), problem.getChoice_e(),
                problem.getChoice_f(), problem.getChoice_g(), problem.getChoice_h(),
                JSON.toJSONString(problem.getAnswer()), problem.getDifficultyVal(), problem.getAnalysis(),
                JSON.toJSONString(problem.getKnowledgeList()), videoPath, problem.getUuid()
        );
    }

    public void insertMultipleChoiceWithExcel(List<MultipleChoice> multipleChoiceList,String account) {
        List<KnowledgeArray> knowledgeArrays = knowledgeArrayService.selectAllKnowledgeArray(account);
        List<String> newKnowledgeList = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for(MultipleChoice s : multipleChoiceList){
            if(!s.getProblem().equals("")) {
                List<String> knowledgeList = s.getKnowledgeList();//excel传来的，没有保存到默认分组
                for (String s1 : knowledgeList) {
                    set.add(s1);
                }
                this.insertMultipleChoice(s, account);
            }
        }
        boolean tag = false;
        for(String setString : set) {
            for (KnowledgeArray k : knowledgeArrays) {
                if(tag) break;
                for (String s : k.getKnowledgeList()) {
                    if(setString.trim().equals(s)){
                        tag = true;//包含，不需要保存
                        break;
                    }
                }
            }
            if(!tag) newKnowledgeList.add(setString);
        }
        KnowledgeArray knowledgeArray = knowledgeArrayService.selectKnowledgeArrayByName(account, "默认分组");
        newKnowledgeList.addAll(knowledgeArray.getKnowledgeList());
        knowledgeArrayService.updateKnowledgeArrayByName(account,"默认分组",newKnowledgeList);
    }

    public boolean deleteMultipleChoiceByUuid(String account,String uuid) {
        return multipleChoiceDao.deleteMultipleChoiceByUuid(account,uuid);
    }

    /**
     * @Description 用于题目的视频文件上传
     * @date 2020/2/17 17:50
     * @param file .mp4, .avi等视频文件MultipartFile对象
     * @return java.lang.String 视频文件保存的到服务器的绝对地址
     */
    public String saveVideoToDisk(MultipartFile file, String saveName){
        String videoOriginalFileName = file.getOriginalFilename();
        String videoType = videoOriginalFileName.substring(videoOriginalFileName.lastIndexOf(".")+1,videoOriginalFileName.length());
        if(!videoTypeList.contains(videoType)) return "";//不是指定文件格式

        try {
            file.transferTo(new File(uploadStaticParamter.getProblemFolderLocalPath()+"/multiplechoice",saveName));
        } catch (IOException e) {
            log.error("视频文件保存失败,错误信息：{},错误原因：{}",e.getMessage(),e.getCause().getMessage());
            return "";
        }
        return uploadStaticParamter.getProblemFolderLocalPath()+File.separator+saveName;
    }

    public boolean deleteOldVideo(String videoPath) {
        if(videoPath == null || videoPath.isBlank()) return true;
        videoPath = videoPath.substring(6,videoPath.length());
        File file = new File(uploadStaticParamter.getProblemFolderLocalPath(), videoPath);
        if(file.exists()){
            return file.delete();
        }
        return true;
    }

    public List<MultipleChoice> selectMyAllMultipleChoiceWithKnowledgeMap(String account, List<String> knowledgeList) {
        List<MultipleChoice> returnMultipleChoiceList = new ArrayList<>();//要返回的满足要求的题集合
        List<MultipleChoice> multipleChoiceList = selectMyAllMultipleChoice(account);
        for (MultipleChoice multipleChoice : multipleChoiceList) {
            List<String> knowledgeList1 = multipleChoice.getKnowledgeList();//本题考试的知识点集合
            for (String knowledge : knowledgeList1) {
                //如果本题的知识点包含试卷要考察的知识点,则包含
                if(knowledgeList.contains(knowledge)){
                    returnMultipleChoiceList.add(multipleChoice);
                    break;
                }

            }
        }
        return returnMultipleChoiceList;
    }

    public void addUseNum(String account, String uuid) {
        multipleChoiceDao.addUseNum(account,uuid);

    }


    public MultipleChoice selectMultipleChoiceWithoutUuidList(String teacher_account, List<String> uuidList) {
        if(uuidList.size() == 0){
            return multipleChoiceDao.selectMultipleChoiceUseCondition(teacher_account,"");
        }
        StringBuilder stringBuilder = new StringBuilder("where ");
        for (int i = 0; i < uuidList.size(); i++) {
            if(i != uuidList.size()-1){
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' and ");
            }else{
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' ");
            }
        }
        return multipleChoiceDao.selectMultipleChoiceUseCondition(teacher_account,stringBuilder.toString());
    }

    public List<MultipleChoice> selectProblemWithNum(String teacher_account, int problemNum) {
        return multipleChoiceDao.selectProblemWithNum(teacher_account,problemNum);
    }

}
