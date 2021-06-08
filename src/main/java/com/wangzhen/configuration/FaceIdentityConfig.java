package com.wangzhen.configuration;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.wangzhen.staticparamter.FaceIdentityStaticParamter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @Author wangzhen
 * @Description 人脸识别的配置类
 * @CreateDate 2020/1/26 22:00
 */
@Configuration
public class FaceIdentityConfig {
    private static final Logger logger = LoggerFactory.getLogger(FaceIdentityConfig.class);
    public static final FunctionConfiguration defaultFunctionConfiguration = FunctionConfiguration.builder().supportFaceDetect(true).supportFaceRecognition(true).supportFace3dAngle(true).build();
    public static final FunctionConfiguration face3DAngleConfiguration = FunctionConfiguration.builder().supportFace3dAngle(true).build();
    public static final FunctionConfiguration faceRecognitionConfiguration = FunctionConfiguration.builder().supportFaceRecognition(true).build();
    public static String currentProjectPath = "";
    static {
        String str = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        if(str.startsWith("file:/")){
            str = str.replace("file:/","");
        }else if(str.startsWith("/")){
            str = str.replace("/","");
        }
        currentProjectPath = str;
    }
    @Autowired
    private FaceIdentityStaticParamter faceIdentityStaticParamter;

    @Bean("videoFaceEngine")
    @Scope("prototype")
    public FaceEngine getVideoFaceEngine(){
        logger.debug("项目绝对路径地址"+currentProjectPath);
        FaceEngine faceEngine = new FaceEngine(faceIdentityStaticParamter.getFaceEngineLibPath());
        //激活引擎
        int activeCode = faceEngine.activeOnline(faceIdentityStaticParamter.getAppId(),faceIdentityStaticParamter.getSdkKey());

        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            logger.error("脸部识别引擎(图片模式)联网激活失败");
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_VIDEO);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(1);
        engineConfiguration.setDetectFaceScaleVal(16);
        engineConfiguration.setFunctionConfiguration(defaultFunctionConfiguration);
        //初始化引擎
        int initCode = faceEngine.init(engineConfiguration);
        if (initCode != ErrorInfo.MOK.getValue()) {
            logger.error("初始化引擎(图片模式)失败...");
        }else{
            logger.info("脸部识别引擎(图片模式)激活成功");
        }
        return faceEngine;
    }
    @Bean("imgFaceEngine")
    public FaceEngine getImgFaceEngine(){
        logger.debug("项目绝对路径地址  "+currentProjectPath);
        FaceEngine faceEngine = new FaceEngine(faceIdentityStaticParamter.getFaceEngineLibPath());
        //激活引擎
        int activeCode = faceEngine.activeOnline(faceIdentityStaticParamter.getAppId(),faceIdentityStaticParamter.getSdkKey());

        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            logger.error("脸部识别引擎(图片模式)联网激活失败");
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_VIDEO);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(1);
        engineConfiguration.setDetectFaceScaleVal(16);
        engineConfiguration.setFunctionConfiguration(defaultFunctionConfiguration);
        //初始化引擎
        int initCode = faceEngine.init(engineConfiguration);
        if (initCode != ErrorInfo.MOK.getValue()) {
            logger.error("初始化引擎(图片模式)失败");
        }else{
            logger.info("脸部识别引擎(图片模式)激活成功");
        }
        return faceEngine;
    }
}

