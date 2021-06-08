package com.wangzhen.login.formlogin;

import com.wangzhen.models.users.Admin;
import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.admin.AdminService;
import com.wangzhen.services.admin.ManagerService;
import com.wangzhen.services.admin.TeacherService;
import com.wangzhen.services.manager.StudentService;
import com.wangzhen.services.teacher.problemservice.KnowledgeArrayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description 自定义用户，继承自SpringSecurity的UserDetailService接口
 * @CreateDate 2020/1/12 18:00
 */
@Component
public class LoginUserDetailService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(LoginUserDetailService.class);
    @Autowired
    private StudentService studentService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private KnowledgeArrayService knowledgeArrayService;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        logger.info("#LoginUserDetailService查找用户信息");
        //1.查找学生账户
        Student student = studentService.selectStudentByAccount(account);
        if(student != null) {
            return student;
        }
        //2.查找教师账户
        Teacher teacher = teacherService.selectTeacherByAccount(account);
        if(teacher != null){
            teacherService.initTeacherDatabase(account);
            return teacher;
        }
        //3.查找考务账户
        Manager manager = managerService.selectManagerByAccount(account);
        if(manager !=null){
            return manager;
        }
        //4.查找管理员账户
        Admin admin = adminService.selectAdminByAccount(account);
        if(admin != null){
            return admin;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
