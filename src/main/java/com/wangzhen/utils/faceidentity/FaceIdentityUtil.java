package com.wangzhen.utils.faceidentity;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import com.wangzhen.configuration.FaceIdentityConfig;
import com.wangzhen.configuration.MyWebAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;
/**
 * @Author wangzhen
 * @Description 人脸识别工具类，可导出特征值、识别人脸、人脸对比
 * @CreateDate 2020/1/26 22:10
 */
@SuppressWarnings("all")
public class FaceIdentityUtil {
    public FaceIdentityUtil(){
        this.faceEngine = MyWebAppConfig.context.getBean("imgFaceEngine",FaceEngine.class);
    }
    public FaceIdentityUtil(DetectMode detectMode) {
        if(detectMode == DetectMode.ASF_DETECT_MODE_VIDEO){
            this.faceEngine = MyWebAppConfig.context.getBean("videoFaceEngine",FaceEngine.class);
        }else{
            this.faceEngine = MyWebAppConfig.context.getBean("imgFaceEngine",FaceEngine.class);
        }
    }
    public FaceIdentityUtil(FaceEngine faceEngine) {
        this.faceEngine = faceEngine;
    }
    private static final Logger logger = LoggerFactory.getLogger(FaceIdentityUtil.class);
    private FaceEngine faceEngine;
    /**
     * @Description 返回人脸的3D偏角度集合
     * @date 2020/1/26 22:15
     * @param file
     * @return java.util.List<com.arcsoft.face.Face3DAngle>
     */
    public List<Face3DAngle> getFace3DAngle(InputStream inputStream){
        ImageInfo imageInfo = null;
        List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        try{
            imageInfo = getRGBData(inputStream);
            handle(imageInfo,faceInfoList);
            //人脸3D偏角度提取
            faceEngine.getFace3DAngle(face3DAngleList);
        }catch (Exception e){
            if(e!=null){
                logger.error(e.getMessage());
            }
        }
        return face3DAngleList;
    }
    /**
     * @Description 返回人脸的3D偏角度集合
     * @date 2020/1/26 22:15
     * @param file
     * @return java.util.List<com.arcsoft.face.Face3DAngle>
     */
    public List<Face3DAngle> getFace3DAngle(File file){
        ImageInfo imageInfo = getRGBData(file);
        List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        handle(imageInfo,faceInfoList);

        //人脸3D偏角度提取
        faceEngine.getFace3DAngle(face3DAngleList);
        return face3DAngleList;
    }
    /**
     * @Description 人脸对比，返回指定两个特质值得相似层度 [0.0-1.0]
     * @date 2020/1/26 22:27
     * @param targetFaceFeature
     * @param sourceFaceFeature
     * @return float
     */
    public float compareFace(FaceFeature targetFaceFeature, FaceFeature sourceFaceFeature){
        FaceSimilar faceSimilar = new FaceSimilar();
        int compareCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
        return faceSimilar.getScore();
    }

    /**
     * @Description 返回人脸的特征值集合
     * @date 2020/1/26 22:26
     * @param file
     * @return java.util.List<com.arcsoft.face.FaceFeature>
     */
    public List<FaceFeature> getFaceFeature(File file){
        ImageInfo imageInfo = getRGBData(file);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        handle(imageInfo,faceInfoList);
        //人脸识别 特征值 提取
        List<FaceFeature> faceFeatureList = new ArrayList<FaceFeature>();
        for(int i=0;i<faceInfoList.size() ; ++i){
            FaceFeature faceFeature = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),ImageFormat.CP_PAF_BGR24,faceInfoList.get(i),faceFeature);
            faceFeatureList.add(faceFeature);
        }
        return faceFeatureList;
    }

    /**
     * @Description 返回人脸的特征值集合
     * @date 2020/1/26 22:34
     * @param inputStream
     * @return java.util.List<com.arcsoft.face.FaceFeature>
     */
    public List<FaceFeature> getFaceFeature(InputStream inputStream) throws IOException {
        ImageInfo imageInfo = getRGBData(inputStream);
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        handle(imageInfo,faceInfoList);

        //人脸识别 特征值 提取
        List<FaceFeature> faceFeatureList = new ArrayList<FaceFeature>();
        for(int i=0;i<faceInfoList.size() ; ++i){
            FaceFeature faceFeature = new FaceFeature();
            faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),ImageFormat.CP_PAF_BGR24,faceInfoList.get(i),faceFeature);
            faceFeatureList.add(faceFeature);
        }
        return faceFeatureList;
    }
    public FaceFeature getFaceFeature(String stringFaceFeatureData){
        byte[] faceFeatureData = getFaceFeatureData(stringFaceFeatureData);
        FaceFeature faceFeature = new FaceFeature();
        faceFeature.setFeatureData(faceFeatureData);
        return faceFeature;
    }


    /**
     * @Description 人脸处理
     * @date 2020/1/26 22:25
     * @param imageInfo
     * @param faceInfoList
     * @return void
     */
    private void handle(ImageInfo imageInfo,List<FaceInfo> faceInfoList){
        byte[] imageData = imageInfo.getImageData();
        Integer width = imageInfo.getWidth();
        Integer height = imageInfo.getHeight();
        //把imageInfo转变成人脸集合
        faceEngine.detectFaces(imageData, width, height, ImageFormat.CP_PAF_BGR24, faceInfoList);

        //根据人脸集合 识别 人脸3d旋转角属性 偏角度...
        int process = faceEngine.process(imageData, width, height, ImageFormat.CP_PAF_BGR24, faceInfoList, FaceIdentityConfig.face3DAngleConfiguration);
        logger.info("#图片中人脸的数量为："+faceInfoList.size());
    }
    /**
     * @Description
     * @date 2020/2/19 17:18
     * @param faceFeature 人脸特征值包装类
     * @return java.lang.String  人脸特征值
     */
    public String getFaceFeatureData (FaceFeature faceFeature){
        StringBuilder sb = new StringBuilder();
        for (byte featureDatum : faceFeature.getFeatureData()) {
            sb.append(featureDatum+",");
        }
        return sb.toString();
    }
    public byte[] getFaceFeatureData (String faceFeature){
        StringBuilder sb = new StringBuilder();
        String[] split = faceFeature.split(",");
        byte[] bytes = new byte[split.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = Byte.decode(split[i]);
        }
        return bytes;
    }
    /**
     * @Description
     * @date 2020/2/19 17:18
     * @param faceFeatureList 人脸特征值包装类集合
     * @return java.util.List<java.lang.String>  人脸特征值集合
     */
    public List<String> getFaceFeatureDataList (List<FaceFeature> faceFeatureList){
        List<String> list = new ArrayList<>();
        for (FaceFeature faceFeature : faceFeatureList) {
            list.add(getFaceFeatureData(faceFeature));
        }
        return list;
    }
    public void close(){
        if(faceEngine != null){
            faceEngine.unInit();
        }
    }
}
