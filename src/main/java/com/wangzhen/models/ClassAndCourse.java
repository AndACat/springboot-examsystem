package com.wangzhen.models;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description 供教师账户用的
 * @CreateDate 2020/1/17 21:27
 */
@Component
public class ClassAndCourse {
    private String class_uuid;
    private String class_name;

    private String course_uuid;
    private String course_name;

    public String getClass_uuid() {
        return class_uuid;
    }

    public ClassAndCourse setClass_uuid(String class_uuid) {
        this.class_uuid = class_uuid;
        return this;
    }

    public String getClass_name() {
        return class_name;
    }

    public ClassAndCourse setClass_name(String class_name) {
        this.class_name = class_name;
        return this;
    }

    public String getCourse_uuid() {
        return course_uuid;
    }

    public ClassAndCourse setCourse_uuid(String course_uuid) {
        this.course_uuid = course_uuid;
        return this;
    }

    public String getCourse_name() {
        return course_name;
    }

    public ClassAndCourse setCourse_name(String course_name) {
        this.course_name = course_name;
        return this;
    }

    @Override
    public String toString() {
        return "ClassAndCourse{" +
                "class_uuid='" + class_uuid + '\'' +
                ", class_name='" + class_name + '\'' +
                ", course_uuid='" + course_uuid + '\'' +
                ", course_name='" + course_name + '\'' +
                '}';
    }
    public String toJsonString(){
        return JSON.toJSONString(this);
    }
}
