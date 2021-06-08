package com.wangzhen.services.teacher.problemservice;
import com.wangzhen.models.problem.Judge;
import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.models.problem.SingleChoice;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.dao.teacher.problemdao.JudgeDao;
import com.wangzhen.models.problem.Judge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/27 15:03
 */
@Service
@Slf4j
public class JudgeService {
    @Autowired
    private JudgeDao judgeDao;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    @Qualifier("videoTypeList")
    private List<String> videoTypeList;
    public List<Judge> selectMyAllJudge(String account){
        return judgeDao.selectMyAllJudge(account);
    }
    public Boolean insertJudge(String account, Judge judge){
        return judgeDao.insertJudge(account,judge);
    }
    public Boolean createJudgeTable(String account){
        return judgeDao.createJudgeTable(account);
    }
    public Boolean deleteJudgeByUuid(String account, String uuid){
        return judgeDao.deleteJudgeByUuid(account,uuid);
    }
    public Boolean updateJudgeInfo(String account, Judge judge){
        return judgeDao.updateJudgeInfo(account,judge);
    }

    public String saveVideoToDisk(MultipartFile excelFile, String saveName) {
        String videoOriginalFileName = excelFile.getOriginalFilename();
        String videoType = videoOriginalFileName.substring(videoOriginalFileName.lastIndexOf(".")+1,videoOriginalFileName.length());
        if(!videoTypeList.contains(videoType)) return "";//不是指定文件格式

        try {
            excelFile.transferTo(new File(uploadStaticParamter.getProblemFolderLocalPath()+"/judge",saveName));
        } catch (IOException e) {
            log.error("视频文件保存失败,错误信息：{},错误原因：{}",e.getMessage(),e.getCause().getMessage());
            return "";
        }
        return uploadStaticParamter.getProblemFolderLocalPath()+"/"+saveName;
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

    public void insertSingleChoiceWithExcel(List<Judge> judgeList, String account) {
        for (Judge judge : judgeList) {
            this.insertJudge(account,judge);
        }
    }

    public List<Judge> selectMyAllJudgeWithKnowledgeMap(String account, List<String> judgeKnowledgeList) {
        List<Judge> returnJudgeList = new ArrayList<>();//要返回的满足要求的题集合
        List<Judge> judgeList = selectMyAllJudge(account);
        for (Judge judge : judgeList) {
            List<String> knowledgeList = judge.getKnowledgeList();//本题考试的知识点集合
            for (String knowledge : knowledgeList) {
                //如果本题的知识点包含试卷要考察的知识点,则包含
                if(judgeKnowledgeList.contains(knowledge)){
                    returnJudgeList.add(judge);
                    break;
                }

            }
        }
        return returnJudgeList;
    }

    public void addUseNum(String account, String uuid) {
        judgeDao.addUseNum(account,uuid);
    }

    public Judge selectJudgeWithoutUuidList(String teacher_account, List<String> uuidList) {
        if(uuidList.size() == 0){
            return judgeDao.selectJudgeUseCondition(teacher_account,"");
        }
        StringBuilder stringBuilder = new StringBuilder("where ");
        for (int i = 0; i < uuidList.size(); i++) {
            if(i != uuidList.size()-1){
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' and ");
            }else{
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' ");
            }
        }
        return judgeDao.selectJudgeUseCondition(teacher_account,stringBuilder.toString());
    }

    public List<Judge> selectProblemWithNum(String teacher_account, int problemNum) {
        return judgeDao.selectProblemWithNum(teacher_account,problemNum);
    }

    public Judge selectProblemByUuid(String account, String uuid){
        return judgeDao.selectProblemByUuid(account,uuid);
    }
}
