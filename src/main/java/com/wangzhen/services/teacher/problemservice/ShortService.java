package com.wangzhen.services.teacher.problemservice;

import com.wangzhen.models.problem.Program;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.problem.SingleChoice;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.dao.teacher.problemdao.ShortDao;
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
 * @CreateDate 2020/2/28 1:13
 */
@Service
@Slf4j
public class ShortService {
    @Autowired
    private ShortDao shortDao;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    @Qualifier("videoTypeList")
    private List<String> videoTypeList;
    public void insertShort(String account, Short sho){
        shortDao.insertShort(account,sho);
    }


    public void deleteShortByUuid(String account,String uuid){
        shortDao.deleteShortByUuid(account,uuid);
    }

    public List<Short> selectMyAllShort( String account){
        return shortDao.selectMyAllShort(account);
    }
    public void updateShortInfo( String account, Short sho){
        shortDao.updateShortInfo(account,sho);
    }
    public void createShortTable( String account){
        shortDao.createShortTable(account);
    }
    public String saveVideoToDisk(MultipartFile excelFile, String saveName) {
        String videoOriginalFileName = excelFile.getOriginalFilename();
        String videoType = videoOriginalFileName.substring(videoOriginalFileName.lastIndexOf(".")+1,videoOriginalFileName.length());
        if(!videoTypeList.contains(videoType)) return "";//不是指定文件格式

        try {
            excelFile.transferTo(new File(uploadStaticParamter.getProblemFolderLocalPath()+"/short",saveName));
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

    public void insertSingleChoiceWithExcel(List<Short> shortList, String account) {
        for (Short sho : shortList) {
            this.insertShort(account,sho);
        }
    }

    public List<Short> selectMyAllShortWithKnowledgeMap(String account, List<String> shortKnowledgeList) {
        List<Short> returnShortList = new ArrayList<>();//要返回的满足要求的题集合
        List<Short> shortList = selectMyAllShort(account);
        for (Short sho : shortList) {
            List<String> knowledgeList = sho.getKnowledgeList();//本题考试的知识点集合
            for (String knowledge : knowledgeList) {
                //如果本题的知识点包含试卷要考察的知识点,则包含
                if(shortKnowledgeList.contains(knowledge)){
                    returnShortList.add(sho);
                    break;
                }
            }
        }
        return returnShortList;
    }

    public void addUseNum(String account, String uuid) {
        shortDao.addUseNum(account,uuid);
    }

    public Short selectShortWithoutUuidList(String teacher_account, List<String> uuidList) {
        if(uuidList.size() == 0){
            return shortDao.selectShortUseCondition(teacher_account,"");
        }
        StringBuilder stringBuilder = new StringBuilder("where ");
        for (int i = 0; i < uuidList.size(); i++) {
            if(i != uuidList.size()-1){
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' and ");
            }else{
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' ");
            }
        }
        return shortDao.selectShortUseCondition(teacher_account,stringBuilder.toString());
    }

    public List<Short> selectProblemWithNum(String teacher_account, int problemNum) {
        return shortDao.selectProblemWithNum(teacher_account,problemNum);
    }
    public Short selectProblemByUuid(String account, String uuid){
        return shortDao.selectProblemByUuid(account,uuid);
    }
}
