package com.wangzhen.controllers.teacher;

import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.manager.ClaService;
import com.wangzhen.services.teacher.CourseService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/7 16:53
 */
@RestController
@RequestMapping("/teacher")
public class CourseController {
    @Autowired
    private ClaService claService;
    @Autowired
    private CourseService courseService;
    @PostMapping("/selectMyCollegeAllCla")
    public List<Cla> selectMyCollegeAllCla(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return claService.selectMyCollegeAllSimpleCla(teacher.getCollege());
    }
    @PostMapping("/selectMyAllCourse")
    public List<Course> selectMyAllCourse(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return courseService.selectMyAllCourse(teacher.getUuid());
    }
    @PostMapping("/insertCourse")
    public ResponseMessage insertCourse(@RequestParam("course") Course course,HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        String uuid = Utils.randomUuid();
        course.setUuid(uuid);
        course.setTeacher_uuid(teacher.getUuid());
        courseService.insertCourse(course);
        return ResponseMessage.getInstance().setCode(200).setData(uuid);
    }
    @PostMapping("/deleteCourseByUuid")
    public ResponseMessage deleteCourseByUuid(@RequestParam("uuid") String uuid){
        courseService.deleteCourseByUuid(uuid);
        return ResponseMessage.OK;
    }
    @PostMapping("/updateCourseInfo")
    public ResponseMessage updateCourseInfo(@RequestParam("student_uuid_list") List<String> student_uuid_list,@RequestParam("uuid")String uuid){
        courseService.updateCourseInfo(student_uuid_list,uuid);
        return ResponseMessage.OK;
    }
    @PostMapping("/selectCourseByUuid")
    public Course selectCourseByUuid(@RequestParam("uuid")String uuid){
        return courseService.selectCourseByUuid(uuid);
    }

    @PostMapping("/selectNoSelectedStudentOfCourseOfCla")
    public List<Student> selectNoSelectedStudentOfCourseOfCla(@RequestParam("cla_uuid")String cla_uuid,@RequestParam("course_uuid")String course_uuid){
        return courseService.selectNoSelectedStudentOfCourseOfCla(cla_uuid,course_uuid);
    }
}
