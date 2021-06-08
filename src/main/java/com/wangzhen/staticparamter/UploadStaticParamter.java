package com.wangzhen.staticparamter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/27 23:08
 */
@ToString
@NoArgsConstructor
@Component
@ConfigurationProperties("upload-static-paramter")
public class UploadStaticParamter {
    /*人脸识别*/
    private String faceFolderLocalPath;
    private String faceAccessPath;

    private String problemFolderLocalPath;
    private String problemAccessPath;

    private String examFaceDetectLocalPath;
    private String examFaceDetectAccessPath;

    public String getFaceFolderLocalPath() {
        return faceFolderLocalPath;
    }

    public void setFaceFolderLocalPath(String faceFolderLocalPath) {
        this.faceFolderLocalPath = faceFolderLocalPath;
        File file = new File(faceFolderLocalPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public String getFaceAccessPath() {
        return faceAccessPath;
    }

    public void setFaceAccessPath(String faceAccessPath) {
        this.faceAccessPath = faceAccessPath;
    }

    public String getProblemFolderLocalPath() {
        return problemFolderLocalPath;
    }

    public void setProblemFolderLocalPath(String problemFolderLocalPath) {
        this.problemFolderLocalPath = problemFolderLocalPath;
        File file = new File(problemFolderLocalPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public String getProblemAccessPath() {
        return problemAccessPath;
    }

    public void setProblemAccessPath(String problemAccessPath) {
        this.problemAccessPath = problemAccessPath;
    }

    public String getExamFaceDetectLocalPath() {
        return examFaceDetectLocalPath;
    }

    public void setExamFaceDetectLocalPath(String examFaceDetectLocalPath) {
        this.examFaceDetectLocalPath = examFaceDetectLocalPath;
        File file = new File(examFaceDetectLocalPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public String getExamFaceDetectAccessPath() {
        return examFaceDetectAccessPath;
    }

    public void setExamFaceDetectAccessPath(String examFaceDetectAccessPath) {
        this.examFaceDetectAccessPath = examFaceDetectAccessPath;
    }
}
