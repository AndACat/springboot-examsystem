package com.wangzhen.services.admin;

import com.alibaba.fastjson.JSON;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.dao.admin.userdao.TeacherDao;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.*;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.Date;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/13 17:50
 */
@Service
@SuppressWarnings("all")
public class TeacherService {
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private SingleChoiceService singleChoiceService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    private KnowledgeArrayService knowledgeArrayService;
    @Autowired
    private MultipleChoiceService multipleChoiceService;
    @Autowired
    private JudgeService judgeService;
    @Autowired
    private FillService fillService;
    @Autowired
    private ShortService shortService;
    @Autowired
    private ProgramService programService;

    /**
     * @Description 返回指定账户或email的用户
     * @date 2020/1/13 17:55
     * @param account
     * @return com.wangzhen.models.users.Teacher
     */
    public Teacher selectTeacherByAccount(String account){
        return teacherDao.selectTeacherByAccount(account,account);
    }

    /**
     * @Description 查找所有教师
     * @date 2020/1/17 22:16
     * @return java.util.List<com.wangzhen.models.users.Teacher>
     */
    public List<Teacher> selectAllTeacher() {
        return teacherDao.selectAllTeacher();
    }

    public boolean deleteTeacherByAccount(String account) {
        return teacherDao.deleteTeacherByAccount(account);
    }


    public boolean updateTeacherInfo(String account, String name, String college, String profession, Date birthday, boolean sex, String email, String phone, String faceImg, String faceFeatureData, String tno, boolean enabled) {
        this.deleteFaceImgFile(account);
        return teacherDao.updateTeacherInfo(account, name, college, profession, birthday, sex, email, phone, faceImg, faceFeatureData, tno, enabled);
    }
    public boolean updateTeacherInfo(String account, String name, String code) {
        return teacherDao.updateTeacherInfo1(account, name, encoder.encode(code));
    }

    public boolean deleteFaceImgFile(String account) {
        Teacher teacher = teacherDao.selectTeacherByAccount(account, account);
        if(teacher == null) return true;
        if(!((teacher.getFaceImg() == null) || teacher.getFaceImg() == null || "".equals(teacher.getFaceImg()))){
            String faceImg = teacher.getFaceImg().replace("/faces","");
            File file = new File(uploadStaticParamter.getFaceFolderLocalPath()+faceImg);
            if(file.exists()){
                return file.delete();
            }
        }
        return false;
    }

    public boolean changeCode(String account,String code) {
        return teacherDao.changeCode(account, encoder.encode(code));
    }

    /**
     * @Description 插入教师
     * @date 2020/2/18 22:15
     * @param teacher 教师object
     * @return boolean
     */
    public boolean insertUser(Teacher teacher) {
        String faceFeatureData = JSON.toJSONString(teacher.getFaceFeatureData());
        faceFeatureData = faceFeatureData.equalsIgnoreCase("null") ? null : faceFeatureData;
        //创建题库表
        this.initTeacherDatabase(teacher.getAccount());
        return teacherDao.insertTeacher(
                Utils.randomUuid(),
                teacher.getName(),
                teacher.getAccount(),
                encoder.encode(teacher.getCode()),
                teacher.getBirthday(),
                teacher.isSex(),
                teacher.getTno(),
                teacher.getEmail(),
                teacher.getPhone(),
                teacher.getCollege(),
                teacher.getProfession(),
                teacher.getFaceImg(),
                faceFeatureData);
    }

    public void insertUser(List<Teacher> teacherList) {
        for (Teacher teacher : teacherList) {
            this.insertUser(teacher);
        }
    }

    public void initTeacherDatabase(String account){
        singleChoiceService.createSingleChoiceDataTable(account);//单选题
        multipleChoiceService.createMultipleChoiceDataTable(account);//多选题
        judgeService.createJudgeTable(account);//判断题
        fillService.createFillTable(account);//填空题
        shortService.createShortTable(account);//简答题
        programService.createProgramTable(account);
    }
}
