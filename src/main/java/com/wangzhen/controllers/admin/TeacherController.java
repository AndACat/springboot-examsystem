package com.wangzhen.controllers.admin;
import com.alibaba.fastjson.JSON;
import com.arcsoft.face.FaceFeature;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.admin.TeacherService;
import com.wangzhen.utils.*;
import com.wangzhen.utils.faceidentity.FaceIdentityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/17 22:14
 */
@RestController
@RequestMapping("/admin")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);
    @Autowired
    private TeacherService teacherService;
    @Autowired
    @Qualifier("faceImageTypeList")
    private List<String> faceImageTypeList = new ArrayList<String>();
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    private CodeUtil codeUtil;


    @RequestMapping("/selectAllTeacher")
    public List<Teacher> selectAllTeacher(HttpServletRequest request){
        return teacherService.selectAllTeacher();
    }
    @RequestMapping("/deleteTeacherByAccount/{account}")
    public ResponseMessage deleteTeacherByAccount(@PathVariable("account") String account){
        boolean tag = true;// = teacherService.deleteTeacherByAccount(account);
        return new ResponseMessage()
                .setCode(tag ? 200: 403)
                .setMsg(tag ? "   删除成功   ":"   删除失败   ");
    }

    @RequestMapping("/updateTeacherInfo")
    public ResponseMessage updateTeacherInfo(@RequestParam("account") String account,
                                             @RequestParam("name") String name,
                                             @RequestParam("birthday") Date birthday,
                                             @RequestParam("sex")boolean sex,
                                             @RequestParam("email")String email,
                                             @RequestParam("phone")String phone,
                                             @RequestParam(name = "faceImg", required = false)MultipartFile faceImg,
                                             @RequestParam("code")String code,
                                             @RequestParam("tno")String tno,
                                             @RequestParam("college")String college,
                                             @RequestParam("profession")String profession,
                                             @RequestParam("enabled")boolean enabled) throws IOException {
        List<FaceFeature> faceFeatureList = null;
        String originalFilename = null;
        String fileType = null;
        String randomFileName = null;
        String faceFeatureData = null;
        if(faceImg!=null){
            originalFilename = faceImg.getOriginalFilename();// 获取文件名，带后缀
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();// 获取文件的后缀格式
            if (faceImageTypeList.contains(fileType)) {
                faceFeatureList = new FaceIdentityUtil().getFaceFeature(faceImg.getInputStream());//单张人脸才保存
                if(faceFeatureList.size() > 1 || faceFeatureList.isEmpty()){
                    return new ResponseMessage().setCode(403).setMsg("修改失败,图片中无脸或包含多张人脸");
                }else{
                    faceFeatureData = new FaceIdentityUtil().getFaceFeatureData(faceFeatureList.get(0));//得到人脸中的数据
                    randomFileName = Utils.randomString(originalFilename);//产生随机文件名
                    File destFile = new File(uploadStaticParamter.getFaceFolderLocalPath()+"/teacher",randomFileName);
                    faceImg.transferTo(destFile);
                    logger.debug("#上传的照片的磁盘中的绝对路径："+destFile.getAbsolutePath());
                }
            }
        }

        String faceImgString = faceImg == null ? null : "/face/teacher/"+randomFileName;

        boolean tag = teacherService.updateTeacherInfo(account, name,college,profession, birthday, sex, email, phone, faceImgString, faceFeatureData, tno, enabled);
        return new ResponseMessage()
                .setCode(tag ? 200 : 403)
                .setMsg(tag ? "   修改成功   " : "   修改失败   ")
                .setData(faceImgString == null ? teacherService.selectTeacherByAccount(account).getFaceImg() : faceImgString);

    }

    @RequestMapping("/changeTeacherCode")
    public ResponseMessage autoChangeCode(@RequestParam("account") String account){
        String code = codeUtil.getRandomCode(8);
        boolean tag = teacherService.changeCode(account,code);
        return new ResponseMessage()
                .setCode(tag ? 200 : 403)
                .setMsg(tag ? "   修改成功   " : "   修改失败   ")
                .setData(code);

    }

    @PostMapping("/insertTeacher")
    public ResponseMessage insertUser(@RequestParam("teacher")Teacher teacher,@RequestParam(value = "faceImg",required = false)MultipartFile faceImg) throws IOException {
        String originalFileName = null;
        String fileType = null;
        List<FaceFeature> faceFeatureList = null;
        String randomFileName = null;
        String randomCode = new CodeUtil().getRandomCode(6);
        teacher.setCode(randomCode);
        if(faceImg != null){
            //人脸识别图片不为空
             originalFileName = faceImg.getOriginalFilename();// 获取文件名，带后缀
             fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();// 获取文件的后缀格式
            //1.检测是不是人脸
            if(faceImageTypeList.contains(fileType)){
                //图片格式正确
                faceFeatureList = new FaceIdentityUtil().getFaceFeature(faceImg.getInputStream());
                int faceFeatureSize = faceFeatureList.size();
                if( faceFeatureSize == 0 || faceFeatureSize >= 2){
                    return new ResponseMessage().setData(403).setMsg("图片中无脸或包含多张人脸");
                }
                //2.得到人脸的特征值
                String faceFeatureData = new FaceIdentityUtil().getFaceFeatureData(faceFeatureList.get(0));
                teacher.setFaceFeatureData(faceFeatureData);
                //3.保存人脸图片到磁盘
                randomFileName = Utils.randomString(originalFileName);
                faceImg.transferTo(new File(uploadStaticParamter.getFaceFolderLocalPath()+"/teacher",randomFileName));
                //4.插入信息到数据库
                teacher.setFaceImg("/face/teacher/"+randomFileName);
                teacher.setFaceFeatureData(faceFeatureData);
                String a;
            }
        }
        boolean tag = teacherService.insertUser(teacher);//保存信息
        return new ResponseMessage().setCode(tag ? 200 : 403).setData(JSON.toJSONString(teacher));
    }

    @PostMapping("/insertTeacherByExcel")
    public ResponseMessage insertUserByExcel(@RequestParam("excelFile") MultipartFile excelFile){
        StringBuffer errMsg = new StringBuffer();
        List<Teacher> teacherList = ExcelReadUtil.getTeacherList(excelFile, errMsg);
        teacherService.insertUser(teacherList);
        return ResponseMessage.getInstance().setCode(200).setMsg(errMsg.toString());
    }

}
