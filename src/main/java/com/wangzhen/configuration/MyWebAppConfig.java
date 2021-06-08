package com.wangzhen.configuration;

import com.wangzhen.models.Course;
import com.wangzhen.models.Paper;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Student;
import com.wangzhen.staticparamter.FaceIdentityStaticParamter;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.users.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/27 22:35
 */
@Component
@Configuration
public class MyWebAppConfig implements WebMvcConfigurer {
    public static ApplicationContext context;
    @Autowired
    private FaceIdentityStaticParamter faceIdentityStaticParamter;
    @Autowired
    public UploadStaticParamter uploadStaticParamter;
    @Autowired
    private Converter<String, SingleChoice> stringSingleChoiceConverter;
    @Autowired
    private Converter<String, Date> stringDateConverter;
    @Autowired
    private Converter<String [], List<String>> stringListConverter;
    @Autowired
    private Converter<String, Teacher> stringTeacherConverter;
    @Autowired
    private Converter<String, MultipleChoice> stringMultipleChoiceConverter;
    @Autowired
    private Converter<String, Judge> stringJudgeConverter;
    @Autowired
    private Converter<String, Fill> stringFillConverter;
    @Autowired
    private Converter<String,Short> stringShortConverter;
    @Autowired
    private Converter<String, PaperStrategy> stringPaperStrategyConverter;
    @Autowired
    private Converter<String, Manager> stringManagerConverter;
    @Autowired
    private Converter<String, Student> stringStudentConverter;
    @Autowired
    private Converter<String, Course> stringCourseConverter;
    @Autowired
    private Converter<String,Map<String, List<String>>> stringMapConverter;
    @Autowired
    private Converter<String, Program> stringProgramConverter;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(uploadStaticParamter.getFaceAccessPath())
                .addResourceLocations("file:"+uploadStaticParamter.getFaceFolderLocalPath());
        registry.addResourceHandler(uploadStaticParamter.getProblemAccessPath())
                .addResourceLocations("file:"+uploadStaticParamter.getProblemFolderLocalPath());
        registry.addResourceHandler(uploadStaticParamter.getExamFaceDetectAccessPath())
                .addResourceLocations("file:"+uploadStaticParamter.getExamFaceDetectLocalPath());


        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    //custom converters
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class,SingleChoice.class,stringSingleChoiceConverter);
        registry.addConverter(String[].class,List.class,stringListConverter);
        registry.addConverter(String.class,Date.class,stringDateConverter);
        registry.addConverter(String.class,Teacher.class,stringTeacherConverter);
        registry.addConverter(String.class,MultipleChoice.class,stringMultipleChoiceConverter);
        registry.addConverter(String.class,Judge.class,stringJudgeConverter);
        registry.addConverter(String.class,Fill.class,stringFillConverter);
        registry.addConverter(String.class,Short.class,stringShortConverter);
        registry.addConverter(String.class, PaperStrategy.class,stringPaperStrategyConverter);
        registry.addConverter(String.class,Manager.class,stringManagerConverter);
        registry.addConverter(String.class,Student.class,stringStudentConverter);
        registry.addConverter(String.class,Course.class,stringCourseConverter);
        registry.addConverter(String.class,Map.class,stringMapConverter);
        registry.addConverter(String.class,Program.class,stringProgramConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor);
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        MyWebAppConfig.context = context;
    }
}
