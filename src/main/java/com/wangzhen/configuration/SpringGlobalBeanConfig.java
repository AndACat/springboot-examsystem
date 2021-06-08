package com.wangzhen.configuration;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yeauty.standard.ServerEndpointExporter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author wangzhen
 * @Description spring全局配置文件，所有配置集中配置在此处
 * @CreateDate 2020/1/12 18:00
 */
@Slf4j
@Configuration
public class SpringGlobalBeanConfig {
    /**
     * @Description  //明文＋一个随机盐值，每次加密之后的密码都不一样  加密存储
     * @date 2020/1/12
     * @param
     * @return org.springframework.security.crypto.password.PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * @Description 加载springsecurity的中文回显消息，默认为英文
     * @date 2020/1/12
     * @param
     * @return org.springframework.context.support.ReloadableResourceBundleMessageSource
     */
    @Bean("messageSource")
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource message = new ReloadableResourceBundleMessageSource();
        message.setBasename("classpath:/org/springframework/security/messages_zh_CN");
        return message;
    }


    @Bean("faceImageTypeList")
    public List<String> imageTypeList(){
        List<String> imageTypeList = new ArrayList<String>();
        imageTypeList.add("jpg");
        imageTypeList.add("jpeg");
        imageTypeList.add("png");
        imageTypeList.add("bmp");
        imageTypeList.add("gif");
        return imageTypeList;
    }
    @Bean("videoTypeList")
    public List<String> videoTypeList(){
        List<String> videoTypeList = new ArrayList<String>();
        videoTypeList.add("mp4");
        videoTypeList.add("webm");
        videoTypeList.add("ogg");
        return videoTypeList;
    }

    @Bean
    public Converter<String, Fill> stringFillConverter(){
        return new Converter<String, Fill>() {
            @Override
            public Fill convert(String source) {
                return JSON.parseObject(source,Fill.class);
            }
        };
    }
    @Bean
    public Converter<String, Date> stringDateConverter(){
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/mm/dd");
                try {
                    java.util.Date parse = simpleDateFormat.parse(source);
                    return new java.sql.Date(parse.getTime());
                } catch (ParseException e) {
                    log.error("#自定义类型转换器转换失败，失败原因：{}",e.getMessage());
                }
                return null;
            }
        };
    }

    @Bean
    public Converter<String, Judge> stringJudgeConverter(){
        return new Converter<String, Judge>() {
            @Override
            public Judge convert(String source) {
                return JSON.parseObject(source,Judge.class);
            }
        };
    }

    @Bean
    public Converter<String [], List<String>> stringListConverter(){
        return new Converter<String[], List<String>>() {
            @Override
            public List<String> convert(String[] source) {
                return Arrays.asList(source);
            }
        };
    }
    @Bean
    public Converter<String, MultipleChoice> stringMultipleChoiceConverter(){
        return new Converter<String, MultipleChoice>() {
            @Override
            public MultipleChoice convert(String source) {
                return JSON.parseObject(source, MultipleChoice.class);
            }
        };
    }
    @Bean
    public Converter<String, SingleChoice> stringSingleChoiceConverter(){
        return new Converter<String, SingleChoice>() {
            @Override
            public SingleChoice convert(String source) {
                return JSON.parseObject(source,SingleChoice.class);
            }
        };
    }
    @Bean
    public Converter<String, Teacher> stringTeacherConverter(){
        return new Converter<String, Teacher>() {
            @Override
            public Teacher convert(String source) {
                return JSON.parseObject(source,Teacher.class);
            }
        };
    }
    @Bean
    public Converter<String, Short> stringShortConverter(){
        return new Converter<String, Short>() {
            @Override
            public Short convert(String source) {
                return JSON.parseObject(source,Short.class);
            }
        };
    }
    @Bean
    public Converter<String, PaperStrategy> stringPaperStrategyConverter(){
        return new Converter<String, PaperStrategy>() {
            @Override
            public PaperStrategy convert(String source) {
                return JSON.parseObject(source,PaperStrategy.class);
            }
        };
    }
    @Bean
    public Converter<String, Manager> stringManagerConverter(){
        return new Converter<String, Manager>() {
            @Override
            public Manager convert(String source) {
                return JSON.parseObject(source,Manager.class);
            }
        };
    }
    @Bean
    public Converter<String, Student> stringStudentConverter(){
        return new Converter<String, Student>() {
            @Override
            public Student convert(String source) {
                return JSON.parseObject(source,Student.class);
            }
        };
    }
    @Bean
    public Converter<String, Cla> stringClaConverter(){
        return new Converter<String, Cla>() {
            @Override
            public Cla convert(String source) {
                return JSON.parseObject(source,Cla.class);
            }
        };
    }
    @Bean
    public Converter<String, Course> stringCourseConverter(){
        return new Converter<String, Course>() {
            @Override
            public Course convert(String source) {
                return JSON.parseObject(source,Course.class);
            }
        };
    }
    /**
     * @Description
     * @date 2020/3/9 17:01
     * @param
     * @return org.springframework.core.convert.converter.Converter<java.lang.String,java.util.Map<java.lang.String,java.util.Map<java.lang.Float,java.lang.String>>>
     *     {
     *     "单选题": {
     *         "20": "行列式",
     *         "60": "方程组"
     *     },
     *     "多选题": {
     *         "20": "数学",
     *         "60": "生物"
     *     }
     * }
     */
    @Bean
    public Converter<String,Map<String, List<String>>> stringMapConverter(){
        return new Converter<String, Map<String, List<String>>>() {
            @Override
            public Map<String, List<String>> convert(String source) {
                Map<String, List<String>> map = new HashMap<>();
                JSONObject jsonObject = JSON.parseObject(source);
                for (String key : jsonObject.keySet()) {
                    JSONArray jsonArray = jsonObject.getJSONArray(key);
                    List<String> list = jsonArray.toJavaList(String.class);
                    map.put(key, list);
                }
                return map;
            }
        };
    }
    @Bean
    public Converter<String,Program> stringProgramConverter(){
        return new Converter<String, Program>() {
            @Override
            public Program convert(String source) {
                return JSON.parseObject(source,Program.class);
            }
        };
    }
    @Bean
    public Converter<String,List<String>> stringLisConverter(){
        return new Converter<String, List<String>>() {
            @Override
            public List<String> convert(String source) {
                return JSON.parseArray(source,String.class);
            }
        };
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
