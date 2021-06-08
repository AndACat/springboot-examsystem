package com.wangzhen.services.teacher.problemservice;

import com.wangzhen.dao.teacher.problemdao.ProgramDao;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Program;
import com.wangzhen.models.problem.Short;
import com.wangzhen.staticparamter.UploadStaticParamter;
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
 * @CreateDate 2020/3/15 17:12
 */
@Service
@Slf4j
public class ProgramService {
    @Autowired
    private ProgramDao programDao;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    @Qualifier("videoTypeList")
    private List<String> videoTypeList;
    public void insertProgram(String account, Program program){
        programDao.insertProgram(account,program);
    }

    public void deleteProgramByUuid(String account,String uuid){
        programDao.deleteProgramByUuid(account,uuid);
    }

    public List<Program> selectMyAllProgram(String account){
        return programDao.selectMyAllProgram(account);
    }

    public void updateProgramInfo(String account,Program program){
        programDao.updateProgramInfo(account,program);
    }


    public void createProgramTable(String account){
        programDao.createProgramTable(account);
    }

    public String saveVideoToDisk(MultipartFile excelFile, String saveName) {
        String videoOriginalFileName = excelFile.getOriginalFilename();
        String videoType = videoOriginalFileName.substring(videoOriginalFileName.lastIndexOf(".")+1,videoOriginalFileName.length());
        if(!videoTypeList.contains(videoType)) return "";//不是指定文件格式

        try {
            excelFile.transferTo(new File(uploadStaticParamter.getProblemFolderLocalPath()+"/program",saveName));
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

    public void insertProgramWithExcel(List<Program> programList, String account) {
        for (Program program : programList) {
            this.insertProgram(account,program);
        }
    }

    public List<Program> selectMyAllProgramWithKnowledgeMap(String account, List<String> programKnowledgeList) {
        List<Program> returnProgramList = new ArrayList<>();//要返回的满足要求的题集合
        List<Program> programList = this.selectMyAllProgram(account);
        for (Program program : programList) {
            List<String> knowledgeList = program.getKnowledgeList();//本题考试的知识点集合
            for (String knowledge : knowledgeList) {
                //如果本题的知识点包含试卷要考察的知识点,则包含
                if(programKnowledgeList.contains(knowledge)){
                    returnProgramList.add(program);
                    break;
                }

            }
        }
        return returnProgramList;
    }

    public List<Program> selectMyAllShortWithKnowledgeMap(String account, List<String> shortKnowledgeList) {
        List<Program> returnProgramList = new ArrayList<>();//要返回的满足要求的题集合
        List<Program> programList = selectMyAllProgram(account);
        for (Program program : programList) {
            List<String> knowledgeList = program.getKnowledgeList();//本题考试的知识点集合
            for (String knowledge : knowledgeList) {
                //如果本题的知识点包含试卷要考察的知识点,则包含
                if(shortKnowledgeList.contains(knowledge)){
                    returnProgramList.add(program);
                    break;
                }
            }
        }
        return returnProgramList;
    }

    public void addUseNum(String account, String uuid) {
        programDao.addUseNum(account,uuid);
    }

    public Program selectProgramWithoutUuidList(String teacher_account, List<String> uuidList) {
        if(uuidList.size() == 0){
            return programDao.selectProgramUseCondition(teacher_account,"");
        }
        StringBuilder stringBuilder = new StringBuilder("where ");
        for (int i = 0; i < uuidList.size(); i++) {
            if(i != uuidList.size()-1){
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' and ");
            }else{
                stringBuilder.append("uuid != '"+uuidList.get(i)+ "' ");
            }
        }
        return programDao.selectProgramUseCondition(teacher_account,stringBuilder.toString());
    }

    public List<Program> selectProblemWithNum(String teacher_account, int problemNum) {
        return programDao.selectProblemWithNum(teacher_account,problemNum);
    }

    public Program selectProblemByUuid(String account, String uuid){
        return programDao.selectProblemByUuid(account,uuid);
    }
}
