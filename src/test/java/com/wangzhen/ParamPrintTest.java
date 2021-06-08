package com.wangzhen;

import com.wangzhen.staticparamter.FaceIdentityStaticParamter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/26 17:54
 */
@SpringBootTest
public class ParamPrintTest {
    @Autowired
    private FaceIdentityStaticParamter faceIdentityStaticParamter;
    @Test
    public void m(){
        //System.out.println(faceIdentityStaticParamter.toString());
    }
}
