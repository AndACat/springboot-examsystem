package com.wangzhen.models;
import com.alibaba.fastjson.JSON;
import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author wangzhen
 * @Description 课程的实体类
 *  学生有个班级,
 *      学院-专业-班级名（班级名不能重复）
 *      学生在什么班级由考务管理员设定，设定之后学生在此班级中
 *  创建课程类
 *      1.课程名
 *      2.选择学院-专业-班级（班级多选）
 *      3.一键加入此班级下的所有考生
 *      4.创建试卷
 * @CreateDate 2020/1/8 18:00
 */
@Getter
@Setter
public class Course {
    private String uuid;//主键
    private String courseName;//课程名
    private List<String> student_uuid_list;
    private List<Student> studentList;

    private String teacher_uuid;
    private Teacher teacher;//任课教师

    private String cla_uuid;
    private Cla cla;//所属班级
}
