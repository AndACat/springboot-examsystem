package com.wangzhen.services.manager;

import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.admin.userdao.StudentDao;
import com.wangzhen.login.facelogin.FaceLoginUserDetailService;
import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.models.users.Student;
import com.wangzhen.services.teacher.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/5 18:18
 */
@Service
public class StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private ClaService claService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private FaceLoginUserDetailService faceLoginUserDetailService;
    public Student selectStudentByAccount(String account){
        return studentDao.selectStudentByAccount(account);
    }
    
    public List<Student> selectMyCollegeAllStudent(String college){
        List<Student> studentList = studentDao.selectMyCollegeAllStudent(college);
        List<Cla> claList = new ArrayList<Cla>();
        List<Course> courseList = new ArrayList<Course>();
        for (Student student : studentList) {
            //start查班级
            String cla_uuid = student.getCla_uuid();
            boolean tag = false;//不包含
            for (Cla cla : claList) {
                if(cla.getUuid().equals(cla_uuid)){
                    tag = true;//改成包含
                    student.setCla(cla);//放置cla
                    break;
                }
            }
            if(!tag){
                //查询一次
                Cla cla = claService.selectClaByUuid(cla_uuid);
                if(cla != null){
                    claList.add(cla);
                    student.setCla(cla);
                }
            }
            //start查课程
            List<Course> studentCourseList = new ArrayList<>();
            List<String> course_uuid_list = student.getCourse_uuid_list();
            for (String course_uuid : course_uuid_list) {//学生的课程列表
                boolean tag1 = false;
                for (Course course : courseList) {//缓存的课程列表
                    if(course.getUuid().equals(course_uuid)){//如果缓存的列表中包含
                        tag1 = true;//设置包含
                        studentCourseList.add(course);
                        break;
                    }
                }
                if(!tag1){//如果不包含
                    Course course = courseService.selectCourseByUuid(course_uuid);
                    if(course != null){
                        studentCourseList.add(course);
                        courseList.add(course);
                    }
                }
            }
            student.setCourseList(studentCourseList);
            student.setCla_uuid(null);
            student.setCourse_uuid_list(null);
        }
        return studentList;
    }
    
    public void insertStudent(Student student){
        student.setCode(encoder.encode(student.getCode()));
        studentDao.insertStudent(student);
        faceLoginUserDetailService.init();
    }
    
    public void deleteStudentByAccount(String account){
        studentDao.deleteStudentByAccount(account);
    }

    public void updateStudentInfo(Student student){
        if(student.getFaceFeatureData() != null){
            faceLoginUserDetailService.init();
        }
        studentDao.updateStudentInfo(student);
        faceLoginUserDetailService.init();
    }

    public void changeStudentCode(String account, String randomCode) {
        studentDao.changeStudentCode(account, encoder.encode(randomCode));
    }

    public void insertStudent(List<Student> studentList) {
        for (Student student : studentList) {
            studentDao.insertStudent(student);
        }
        faceLoginUserDetailService.init();
    }
    public Student selectStudentByUuid(String uuid){
        return studentDao.selectStudentByUuid(uuid);
    }

    public List<Student> selectAllNoClaStudentByProfession(String college,String profession) {
        return studentDao.selectAllNoClaStudentByProfession(college,profession);
    }

    public void updateStudentCla(String student_uuid, String cla_uuid) {
        studentDao.updateStudentCla(student_uuid,cla_uuid);
    }

    public Student selectSimpleStudentByUuid(String uuid) {
        return studentDao.selectSimpleStudentByUuid(uuid);
    }

    private void updateStudentCourseInfo(String student_uuid, List<String> course_uuid_list) {
        studentDao.updateStudentCourseInfo(student_uuid, JSON.toJSONString(course_uuid_list));
    }

    public void addCourse(String student_uuid, String course_uuid) {
        Student student = selectStudentByUuid(student_uuid);
        List<String> course_uuid_list = student.getCourse_uuid_list();
        course_uuid_list.add(course_uuid);
        this.updateStudentCourseInfo(student_uuid,course_uuid_list);
    }
    public void removeCourse(String student_uuid, String course_uuid) {
        Student student = selectStudentByUuid(student_uuid);
        List<String> course_uuid_list = student.getCourse_uuid_list();
        course_uuid_list.remove(course_uuid);
        this.updateStudentCourseInfo(student_uuid,course_uuid_list);
    }

    public List<Student> selectAllStudentFaceFeatureData() {
        return studentDao.selectAllStudentFaceFeatureData();
    }

    public void updateStudentInfo(String account, String name, String code) {
        studentDao.updateStudentInfo1(account,name,encoder.encode(code));
    }
}
