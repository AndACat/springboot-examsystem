package com.wangzhen.controllers.admin;

import com.arcsoft.face.FaceFeature;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.models.users.Manager;
import com.wangzhen.services.admin.ManagerService;
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
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/17 22:14
 */
@RestController
@RequestMapping("/admin")
public class ManagerController {
    private static final Logger logger = LoggerFactory.getLogger(ManagerController.class);
    @Autowired
    private ManagerService managerService;
    @Autowired
    @Qualifier("faceImageTypeList")
    private List<String> faceImageTypeList = new ArrayList<String>();
    @Autowired
    private UploadStaticParamter uploadStaticParamter;
    @Autowired
    private CodeUtil codeUtil;


    @RequestMapping("/selectAllManager")
    public List<Manager> selectAllManager(HttpServletRequest request){
        return managerService.selectAllManager();
    }
    @RequestMapping("/deleteManagerByAccount/{account}")
    public ResponseMessage deleteManagerByAccount(@PathVariable("account") String account){
        boolean tag = true;// = managerService.deleteManagerByAccount(account);
        return new ResponseMessage()
                .setCode(tag ? 200: 403)
                .setMsg(tag ? "   删除成功   ":"   删除失败   ");
    }

    @RequestMapping("/updateManagerInfo")
    public ResponseMessage updateManagerInfo(@RequestParam("manager") Manager manager,
                                             @RequestParam(name = "faceImg", required = false)MultipartFile faceImg) throws IOException {
        List<FaceFeature> faceFeatureList = null;
        String originalFilename = null;
        String fileType = null;
        String randomFileName = null;//随机文件名
        String faceFeatureData = null;//人脸数据
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
                    File destFile = new File(uploadStaticParamter.getFaceFolderLocalPath()+"/manager",randomFileName);
                    faceImg.transferTo(destFile);
                    logger.debug("#上传的照片的磁盘中的绝对路径："+destFile.getAbsolutePath());
                }
            }
        }

        String faceImgString = faceImg == null ? null : "/face/manager/"+randomFileName;//保存到数据库的结果
        manager.setFaceImg(faceImgString);
        manager.setFaceFeatureData(faceFeatureData);
        managerService.updateManagerInfo(manager);
        return new ResponseMessage()
                .setCode(200 )
                .setMsg("   修改成功   ")
                .setData(faceImgString);

    }

    @RequestMapping("/changeManagerCode")
    public ResponseMessage autoChangeCode(@RequestParam("account") String account){
        String code = codeUtil.getRandomCode(8);
        managerService.changeCode(account,code);
        return new ResponseMessage()
                .setCode(200)
                .setMsg("   修改成功   ")
                .setData(code);

    }

    @PostMapping("/insertManager")
    public ResponseMessage insertUser(@RequestParam("manager")Manager manager,@RequestParam(value = "faceImg",required = false)MultipartFile faceImg) throws IOException {
        String originalFileName = null;
        String fileType = null;
        List<FaceFeature> faceFeatureList = null;
        String randomFileName = null;
        String randomCode = new CodeUtil().getRandomCode(6);
        manager.setCode(randomCode);
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
                manager.setFaceFeatureData(faceFeatureData);
                //3.保存人脸图片到磁盘
                randomFileName = Utils.randomString(originalFileName);
                faceImg.transferTo(new File(uploadStaticParamter.getFaceFolderLocalPath()+"/manager",randomFileName));
                //4.插入信息到数据库
                manager.setFaceImg("/face/manager/"+randomFileName);
                manager.setFaceFeatureData(faceFeatureData);
                String a;
            }
        }
        String uuid = Utils.randomUuid();
        manager.setUuid(uuid);
        managerService.insertUser(manager);//保存信息
        return new ResponseMessage().setCode(200).setData(uuid);
    }

    @PostMapping("/insertManagerByExcel")
    public ResponseMessage insertUserByExcel(@RequestParam("excelFile") MultipartFile excelFile){
        StringBuffer errMsg = new StringBuffer();
        List<Manager> managerList = ExcelReadUtil.getManagerList(excelFile, errMsg);
        managerService.insertUser(managerList);
        return ResponseMessage.getInstance().setCode(200).setMsg(errMsg.toString());
    }

}
