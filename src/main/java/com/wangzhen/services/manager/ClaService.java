package com.wangzhen.services.manager;

import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.manager.ClaDao;
import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.teacher.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/4 15:49
 */
@Service
@SuppressWarnings("all")
public class ClaService {
    @Autowired
    private ClaDao claDao;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;
    public Cla selectClaByUuid(String uuid){
        Cla cla = claDao.selectClaByUuid(uuid);
        return cla;
    }
    List<Cla> selectAllCla(){
        return claDao.selectAllCla();
    }
    public void deleteClaByUuid(String uuid){
        claDao.deleteClaByUuid(uuid);
    }
    public void updateClaInfoByUuid(Cla cla){
        String uuid = cla.getUuid();
        //旧的学生列表集合
        List<String> old_student_uuid_list = claDao.selectClaByUuid(uuid).student_uuid_list();
        //新的学生列表集合
        List<String> new_student_uuid_list = cla.student_uuid_list();
        //如果没有旧数据
        if(old_student_uuid_list.size() == 0){
            //给新学生加入班级
            for(String student_uuid : new_student_uuid_list){
                studentService.updateStudentCla(student_uuid,cla.getUuid());
            }
        }else{//如果有旧数据
            List<String> should_add_cla_list = new ArrayList<>();
            List<String> should_remove_cla_list = new ArrayList<>();
            for (String old_student_uuid : old_student_uuid_list) {
                if(!new_student_uuid_list.contains(old_student_uuid)){
                    //新数据不包含，应该被 remove
                    should_remove_cla_list.add(old_student_uuid);
                }
            }
            for (String new_student_uuid : new_student_uuid_list) {
                if(!old_student_uuid_list.contains(new_student_uuid)){
                    //旧数据不包含 应该被 add
                    should_add_cla_list.add(new_student_uuid);
                }
            }
            for (String should_add_cla : should_add_cla_list) {
                // should_add_cla 是对应student 的 uuid
                studentService.updateStudentCla(should_add_cla,cla.getUuid());
            }
            for (String should_remove_cla : should_remove_cla_list) {
                // should_add_cla 是对应student 的 uuid
                studentService.updateStudentCla(should_remove_cla,"");
            }
        }
        claDao.updateClaInfoByUuid(cla);

    }
    public void insertCla(Cla cla){
        claDao.insertCla(cla);
    }

    public List<Cla> selectMyCollegeAllCla(String college) {
        List<Cla> claList = claDao.selectMyCollegeAllCla(college);
        List<Course> courseList = new ArrayList<>();
        List<Student> studentList = new ArrayList<>();
        for (Cla cla : claList) {
            List<Course> claCourseList = new ArrayList<>();
            for (String course_uuid : cla.getCourse_uuid_list()) {
                boolean tag = false;
                for(Course c : courseList){
                    if(c.getUuid().equals(course_uuid)){
                        tag = true;
                        claCourseList.add(c);
                        break;
                    }
                }
                if(!tag){
                    Course course = courseService.selectCourseByUuid(course_uuid);
                    if(course!=null){
                        courseList.add(course);
                        claCourseList.add(course);
                    }
                }
            }
            cla.setCourseList(claCourseList);
            cla.setCourse_uuid_list(null);
            //开始查学生列表
            List<String> claStudentUuidList = cla.student_uuid_list();
            List<Student> claStudentList = new ArrayList<>();
            for(String uuid : claStudentUuidList){
                boolean tag = false;
                for(Student student : studentList){
                    if(student.getUuid().equals(uuid)){
                        claStudentList.add(student);
                        tag = true;
                        break;
                    }
                }
                if(!tag){
                    Student student = studentService.selectSimpleStudentByUuid(uuid);
                    if(student!=null){
                        studentList.add(student);
                        claStudentList.add(student);
                    }
                }
            }
            cla.setStudentList(claStudentList);
            cla.setStudent_uuid_list(null);
        }

        return claList;
    }
    public List<Cla> selectMyCollegeAllSimpleCla(String college) {
        return claDao.selectMyCollegeAllCla(college);
    }

    public Cla selectClaByCourseUuid(String uuid) {
        return claDao.selectClaByCourseUuid(uuid);
    }

    public void addCourse(String cla_uuid, String uuid) {
        Cla cla = claDao.selectClaByUuid(cla_uuid);
        List<String> course_uuid_list = cla.getCourse_uuid_list();
        course_uuid_list.add(uuid);
        claDao.addCourse(cla_uuid, JSON.toJSONString(course_uuid_list));
    }
}
