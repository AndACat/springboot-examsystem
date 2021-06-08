package com.wangzhen.services.teacher.problemservice;

import com.wangzhen.models.problem.Fill;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.dao.teacher.problemdao.FillDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/28 1:13
 */
@Service
@Slf4j
public class FillService {
    @Autowired
    private FillDao fillDao;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    @Qualifier("videoTypeList")
    private List<String> videoTypeList;
    public void insertFill(String account, Fill fill){
        fillDao.insertFill(account,fill);
    }


    public void deleteFillByUuid(String account,String uuid){
        fillDao.deleteFillByUuid(account,uuid);
    }

    public List<Fill> selectMyAllFill( String account){
        return fillDao.selectMyAllFill(account);
    }
    public void updateFillInfo( String account, Fill fill){
        fillDao.updateFillInfo(account,fill);
    }
    public void createFillTable( String account){
        fillDao.createFillTable(account);
    }
    public String saveVideoToDisk(MultipartFile excelFile, String saveName) {
        String videoOriginalFileName = excelFile.getOriginalFilename();
        String videoType = videoOriginalFileName.substring(videoOriginalFileName.lastIndexOf(".")+1,videoOriginalFileName.length());
        if(!videoTypeList.contains(videoType)) return "";//不是指定文件格式

        try {
            excelFile.transferTo(new File(uploadStaticParamter.getProblemFolderLocalPath()+"/fill",saveName));
        } catch (IOException e) {
            log.error("视频文件保存失败,错误信息：{},错误原因：{}",e.getMessage(),e.getCause().getMessage());
            return "";
        }
        return uploadStaticParamter.getProblemFolderLocalPath()+"/"+saveName;
    }

    public boolean deleteOldVideo(String videoPath) {
        if(videoPath == null){
            return true;
        }
        videoPath = videoPath.substring(6,videoPath.length());
        File file = new File(uploadStaticParamter.getProblemFolderLocalPath(), videoPath);
        if(file.exists()){
            return file.delete();
        }
        return true;
    }

    public void insertFillWithExcel(List<Fill> fillList, String account) {
        for (Fill fill : fillList) {
            this.insertFill(account,fill);
        }
    }

    public List<Fill> selectMyAllFillWithKnowledgeMap(String account, List<String> fillKnowledgeList) {
        List<Fill> returnFillList = new ArrayList<>();//要返回的满足要求的题集合
        List<Fill> fillList = selectMyAllFill(account);
        for (Fill fill : fillList) {
            List<String> knowledgeList = fill.getKnowledgeList();//本题考试的知识点集合
            for (String knowledge : knowledgeList) {
                //如果本题的知识点包含试卷要考察的知识点,则包含
                if(fillKnowledgeList.contains(knowledge)){
                    returnFillList.add(fill);
                    break;
                }

            }
        }
        return returnFillList;
    }

    public void addUseNum(String account, String uuid) {
        fillDao.addUseNum(account,uuid);
    }

    public Fill selectFillWithoutUuidList(String teacher_account, List<String> uuidList) {
        if(uuidList.size() == 0){
            return fillDao.selectFillUseCondition(teacher_account,"");
        }
        StringBuilder stringBuilder = new StringBuilder("where ");
        for (int i = 0; i < uuidList.size(); i++) {
            if(i != uuidList.size()-1){
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' and ");
            }else{
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' ");
            }
        }
        return fillDao.selectFillUseCondition(teacher_account,stringBuilder.toString());
    }

    public List<Fill> selectProblemWithNum(String teacher_account, int problemNum) {
        return fillDao.selectProblemWithNum(teacher_account,problemNum);
    }

    public Fill selectProblemByUuid(String account, String uuid){
        return fillDao.selectProblemByUuid(account,uuid);
    }
}
