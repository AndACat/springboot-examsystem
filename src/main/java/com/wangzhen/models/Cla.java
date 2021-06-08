package com.wangzhen.models;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.manager.StudentService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author wangzhen
 * @Description 班级
 * @CreateDate 2020/1/8 18:00
 */
//@Deprecated

public class Cla {
    @Autowired
    private StudentService studentService;
    @Setter@Getter
    private String uuid;                //主键
    @Setter@Getter
    private String college;//学院
    @Setter@Getter
    private String profession;//专业
    @Setter@Getter
    private String claName;           //班级名
    //@Getter
    private List<String> student_uuid_list;//学生uuid列表
    @Setter@Getter
    private List<Student> studentList;//学生列表
    @Getter
    private List<String> course_uuid_list;//课程列表的uuid集合
    @Setter@Getter
    private List<Course> courseList;//班级底下的课程集合

    public void setStudent_uuid_list(String student_uuid_list) {
        this.student_uuid_list = JSON.parseArray(student_uuid_list,String.class);
    }

    public void setCourse_uuid_list(String course_uuid_list) {
        this.course_uuid_list = JSON.parseArray(course_uuid_list,String.class);
    }

    public String getStudent_uuid_list() {
        return JSON.toJSONString(student_uuid_list);
    }

    public List<String> student_uuid_list(){
        return student_uuid_list;
    }
}
