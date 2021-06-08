package com.wangzhen;

import com.wangzhen.models.problem.Judge;
import com.wangzhen.services.teacher.problemservice.JudgeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Map;

@SpringBootTest
@SuppressWarnings("all")
class ExamsystemApplicationTests {

    @Autowired
    JudgeService judgeService;
    @Autowired
    Converter<String,Map<String, Map<Float,String>>> stringMapConverter;
    @Test
    void contextLoads() {
        String str = "{" +
                "    \"单选题\": {" +
                "        \"20\": \"行列式\"," +
                "        \"60\": \"方程组\"" +
                "    }," +
                "    \"多选题\": {" +
                "        \"20\": \"数学\"," +
                "        \"60\": \"生物\"" +
                "    }" +
                "}";
        Map<String, Map<Float, String>> convert = stringMapConverter.convert(str);
        System.out.println(convert);
    }
}
