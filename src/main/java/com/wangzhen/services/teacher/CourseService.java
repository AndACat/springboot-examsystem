package com.wangzhen.services.teacher;
import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.manager.CourseDao;
import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.manager.ClaService;
import com.wangzhen.services.manager.StudentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/6 0:12
 */
@Service
public class CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private ClaService claService;
    @Autowired
    private StudentService studentService;

    public List<Course> selectMyAllCourse(String teacher_uuid){
        List<Cla> claList = new ArrayList<>();
        List<Course> courseList = courseDao.selectMyAllCourse(teacher_uuid);
        List<Student> studentList = new ArrayList<>();
        for (Course course : courseList) {
            String cla_uuid = course.getCla_uuid();
            boolean tag = false;
            for (Cla cla : claList) {
                if(cla.getUuid().equals(cla_uuid)){
                    course.setCla(cla);
                    tag = true;
                    continue;
                }
            }
            if(!tag){
                Cla cla = claService.selectClaByUuid(cla_uuid);
                if(cla != null){
                    claList.add(cla);
                    course.setCla(cla);
                }
            }

            //开始查学生列表
            List<String> courseStudentUuidList = course.getStudent_uuid_list();
            List<Student> courseStudentList = new ArrayList<>();
            for(String uuid : courseStudentUuidList){
                boolean tag1 = false;
                for(Student student : courseStudentList){
                    if(student.getUuid().equals(uuid)){
                        courseStudentList.add(student);
                        tag1 = true;
                        break;
                    }
                }
                if(!tag1){
                    Student student = studentService.selectSimpleStudentByUuid(uuid);
                    if(student!=null){
                        studentList.add(student);
                        courseStudentList.add(student);
                    }
                }
            }
            course.setStudentList(courseStudentList);
            course.setStudent_uuid_list(null);

        }
        return courseList;
    }
    public void insertCourse(Course course){
        String cla_uuid = course.getCla_uuid();
        Cla cla = claService.selectClaByUuid(cla_uuid);

        courseDao.insertCourse(course);
        claService.addCourse(cla_uuid,course.getUuid());
    }
    public void deleteCourseByUuid(String uuid){
        courseDao.deleteCourseByUuid(uuid);
    }
    public void updateCourseInfo(List<String> new_student_uuid_list,String course_uuid){
        //双向引用重新绑定
        Course course = this.selectCourseByUuid(course_uuid);
        //旧的student_uuid_list
        List<String> old_student_uuid_list = course.getStudent_uuid_list();

        List<String> should_add_student_uuid_list = new ArrayList<>();
        List<String> should_remove_student_uuid_list = new ArrayList<>();
        for (String uuid : new_student_uuid_list) {
            if(! old_student_uuid_list.contains(uuid)){
                //旧的不包含新的 应该被add
                should_add_student_uuid_list.add(uuid);
            }
        }
        for (String uuid : old_student_uuid_list) {
            if(! new_student_uuid_list.contains(uuid)){
                should_remove_student_uuid_list.add(uuid);
            }
        }
        //更新课程信息
        courseDao.updateCourseInfo(JSON.toJSONString(new_student_uuid_list),course_uuid);
        //重新绑定学生对课程的引用

        for (String student_uuid : should_add_student_uuid_list) {
            studentService.addCourse(student_uuid,course_uuid);
        }
        for (String student_uuid : should_remove_student_uuid_list) {
            studentService.removeCourse(student_uuid,course_uuid);
        }

    }
    public Course selectCourseByUuid(String uuid){
        return courseDao.selectCourseByUuid(uuid);
    }

    public List<Student> selectNoSelectedStudentOfCourseOfCla(String cla_uuid,String course_uuid) {
        Cla cla = claService.selectClaByUuid(cla_uuid);
        List<String> cla_student_uuid_list = cla.student_uuid_list();
        List<Student> noSelectedStudentOfCourseOfCla = new ArrayList<>();//班级里所有的学生

        Course course = this.selectCourseByUuid(course_uuid);//课程
        List<String> course_student_uuid_list = course.getStudent_uuid_list();

        for(String cla_student_uuid : cla_student_uuid_list){
            if(!course_student_uuid_list.contains(cla_student_uuid)){
                //课程不包含学生，以前没选中
                noSelectedStudentOfCourseOfCla.add(studentService.selectSimpleStudentByUuid(cla_student_uuid));
            }
        }
        return noSelectedStudentOfCourseOfCla;
    }
}
