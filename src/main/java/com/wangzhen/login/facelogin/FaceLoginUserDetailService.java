package com.wangzhen.login.facelogin;

import com.arcsoft.face.FaceFeature;
import com.wangzhen.configuration.PublicSession;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.manager.StudentService;
import com.wangzhen.utils.faceidentity.FaceIdentityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 自定义用户，继承自SpringSecurity的UserDetailService接口
 * @CreateDate 2020/1/12 18:00
 */
@Component
public class FaceLoginUserDetailService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(FaceLoginUserDetailService.class);
    @Autowired
    private StudentService studentService;

    private float topSimilar = 0.8f;

    public static List<Student> studentList = new ArrayList<>();
    @PostConstruct
    public void init(){
        studentList = studentService.selectAllStudentFaceFeatureData();
    }

    @Override
    public UserDetails loadUserByUsername(String faceKey)throws IllegalArgumentException , UsernameNotFoundException {
        logger.info("#FaceLoginUserDetailService查找用户信息");
        //1.查找学生账户
        Part face = (Part) PublicSession.getVal(faceKey);
        try {
            List<FaceFeature> faceFeatureList = new FaceIdentityUtil().getFaceFeature(face.getInputStream());
            if(faceFeatureList.size() == 0){
                throw new IllegalArgumentException("照片中无人.");
            }
            for (Student student : studentList) {
                FaceFeature sourceFaceFeature = new FaceIdentityUtil().getFaceFeature(student.getFaceFeatureData());
                float similar = new FaceIdentityUtil().compareFace(faceFeatureList.get(0), sourceFaceFeature);
                System.out.println("相似度："+similar);
                if(similar > topSimilar){
                    return studentService.selectStudentByUuid(student.getUuid());
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("照片损坏.");
        }
        throw new UsernameNotFoundException("不存在此用户");
    }
}
