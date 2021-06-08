package com.wangzhen;

import com.arcsoft.face.Face3DAngle;
import com.wangzhen.utils.faceidentity.FaceIdentityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/26 18:35
 */
@SpringBootTest
public class FaceTest {
    @Test
    public void m(){
        List<Face3DAngle> faceFeature = new FaceIdentityUtil().getFace3DAngle(new File("S:/照片/注册专用/免冠照.jpg"));
        System.out.println(faceFeature.size());
//        byte[] featureData = faceFeature.get(0).getFeatureData();
//        StringBuffer stringBuffer = new StringBuffer();
//        for(byte b : featureData){
//            stringBuffer.append(b);
//        }
//        System.out.println(stringBuffer.toString());
//        System.out.println(JSON.toJSONString(faceFeature.get(0).getFeatureData()));
//        System.out.println();
    }
}
