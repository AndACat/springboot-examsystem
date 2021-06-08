package com.wangzhen.staticparamter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description 人脸识别的一些参数
 * @CreateDate 2020/1/26 21:52
 */
@Component
@ConfigurationProperties("arcsoft-static-paramter")
public class FaceIdentityStaticParamter {
    private  String appId;
    private  String sdkKey;
    private  String faceEngineLibPath;

    private String webScoketPort;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSdkKey() {
        return sdkKey;
    }

    public void setSdkKey(String sdkKey) {
        this.sdkKey = sdkKey;
    }

    public String getFaceEngineLibPath() {
        return faceEngineLibPath;
    }

    public String getWebScoketPort() {
        return webScoketPort;
    }

    public void setWebScoketPort(String webScoketPort) {
        this.webScoketPort = webScoketPort;
    }

    public void setFaceEngineLibPath(String faceEngineLibPath) {
        this.faceEngineLibPath = faceEngineLibPath;
    }

    @Override
    public String toString() {
        return "FaceIdentityStaticParamter{" +
                "appId='" + appId + '\'' +
                ", sdkKey='" + sdkKey + '\'' +
                ", faceEngineLibPath='" + faceEngineLibPath + '\'' +
                '}';
    }
}
