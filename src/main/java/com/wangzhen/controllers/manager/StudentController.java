package com.wangzhen.controllers.manager;

import com.arcsoft.face.FaceFeature;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.manager.StudentService;
import com.wangzhen.utils.*;
import com.wangzhen.utils.faceidentity.FaceIdentityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/5 18:28
 */
@Slf4j
@RestController
@RequestMapping("/manager")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    @Qualifier("faceImageTypeList")
    private List<String> faceImageTypeList = new ArrayList<String>();
    @Autowired
    private UploadStaticParamter uploadStaticParamter;

    @PostMapping("/selectStudentByAccount")
    public Student selectStudentByAccount(@Param("account") String account){
        return studentService.selectStudentByAccount(account);
    }
    @PostMapping("/selectMyCollegeAllStudent")
    public List<Student> selectMyCollegeAllStudent(HttpSession session){
        Manager manager = Utils.getUser(session, Manager.class);
        return studentService.selectMyCollegeAllStudent(manager.getCollege());
    }
    @PostMapping("/insertStudent")
    public ResponseMessage insertStudent(@Param("student") Student student,@RequestParam(value = "faceImgFile",required = false)MultipartFile faceImgFile) throws IOException {
        String originalFileName = null;
        String fileType = null;
        List<FaceFeature> faceFeatureList = null;
        String randomFileName = null;
        String faceImg = null;
        String randomCode = Utils.getCode();
        student.setCode(randomCode);
        String uuid = Utils.randomUuid();
        student.setUuid(uuid);
        if(faceImgFile != null){
            //人脸识别图片不为空
            originalFileName = faceImgFile.getOriginalFilename();// 获取文件名，带后缀
            fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();// 获取文件的后缀格式
            //1.检测是不是人脸
            if(faceImageTypeList.contains(fileType)){
                //图片格式正确
                faceFeatureList = new FaceIdentityUtil().getFaceFeature(faceImgFile.getInputStream());
                int faceFeatureSize = faceFeatureList.size();
                if( faceFeatureSize == 0 || faceFeatureSize >= 2){
                    return new ResponseMessage().setData(403).setMsg("图片中无脸或包含多张人脸");
                }
                //2.得到人脸的特征值
                String faceFeatureData = new FaceIdentityUtil().getFaceFeatureData(faceFeatureList.get(0));
                student.setFaceFeatureData(faceFeatureData);
                //3.保存人脸图片到磁盘
                randomFileName = Utils.randomString(originalFileName);
                faceImgFile.transferTo(new File(uploadStaticParamter.getFaceFolderLocalPath()+"/student",randomFileName));
                //4.插入信息到数据库
                faceImg = randomFileName == null ? null : "/face/student/"+randomFileName;
                student.setFaceImg(faceImg);
                student.setFaceFeatureData(faceFeatureData);
                String a;
            }
        }
        studentService.insertStudent(student);//保存信息
        return new ResponseMessage().setCode(200).setData(Utils.getArray(uuid,faceImg));
    }
    @PostMapping("/deleteStudentByAccount/{account}")
    public ResponseMessage deleteStudentByAccount(@PathVariable("account") String account){
        studentService.deleteStudentByAccount(account);
        return ResponseMessage.OK;
    }
    @PostMapping("/updateStudentInfo")
    public ResponseMessage updateStudentInfo(@Param("student")Student student,@RequestParam(value = "faceImgFile",required = false)MultipartFile faceImgFile) throws IOException {
        List<FaceFeature> faceFeatureList = null;
        String originalFilename = null;
        String fileType = null;
        String randomFileName = null;
        String faceFeatureData = null;
        if(faceImgFile!=null){
            originalFilename = faceImgFile.getOriginalFilename();// 获取文件名，带后缀
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();// 获取文件的后缀格式
            if (faceImageTypeList.contains(fileType)) {
                faceFeatureList = new FaceIdentityUtil().getFaceFeature(faceImgFile.getInputStream());//单张人脸才保存
                if(faceFeatureList.size() > 1 || faceFeatureList.isEmpty()){
                    return new ResponseMessage().setCode(403).setMsg("修改失败,图片中无脸或包含多张人脸");
                }else{
                    faceFeatureData = new FaceIdentityUtil().getFaceFeatureData(faceFeatureList.get(0));//得到人脸中的数据
                    randomFileName = Utils.randomString(originalFilename);//产生随机文件名
                    File destFile = new File(uploadStaticParamter.getFaceFolderLocalPath()+"/student",randomFileName);
                    faceImgFile.transferTo(destFile);
                    log.debug("#上传的照片的磁盘中的绝对路径："+destFile.getAbsolutePath());
                }
            }
        }

        String faceImgString = faceImgFile == null ? null : "/face/student/"+randomFileName;
        student.setFaceImg(faceImgString);
        student.setFaceFeatureData(faceFeatureData);
        studentService.updateStudentInfo(student);
        return new ResponseMessage().setCode(200).setData(faceImgString);
    }

    @PostMapping("/changeStudentCode/{account}")
    public ResponseMessage changeStudentCode(@PathVariable("account") String account){
        String randomCode = Utils.getCode();
        studentService.changeStudentCode(account,randomCode);
        return new ResponseMessage().setCode(200).setData(randomCode);
    }

    @PostMapping("/insertStudentByExcel")
    public ResponseMessage insertStudentByExcel(@RequestParam("excelFile") MultipartFile excelFile){
        StringBuffer errMsg = new StringBuffer();
        List<Student> studentList = ExcelReadUtil.getStudentList(excelFile, errMsg);
        studentService.insertStudent(studentList);
        return ResponseMessage.getInstance().setCode(200).setMsg(errMsg.toString());
    }

    @PostMapping("/getManagerCollege")
    public String getManagerCollege(HttpSession session){
        return Utils.getUser(session,Manager.class).getCollege();
    }

    @PostMapping("/selectAllNoClaStudentByProfession")
    public List<Student> selectAllNoClaStudentByProfession(@Param("profession") String profession,HttpSession session){
        Manager manager = Utils.getUser(session, Manager.class);
        return studentService.selectAllNoClaStudentByProfession(manager.getCollege(),profession);
    }
}
