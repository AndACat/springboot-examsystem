package com.wangzhen;

import com.wangzhen.dao.admin.userdao.TeacherDao;
import com.wangzhen.services.admin.AdminService;
import com.wangzhen.services.admin.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/14 14:20
 */
@SpringBootTest
public class UserSelectTest {
    @Autowired
    AdminService adminService;
    @Autowired
    TeacherDao teacherDao;
    @Autowired
    TeacherService teacherService;

    @Test
    public void selectAdmin(){
//        String account = "1608054401";
//        Admin admin = adminService.selectAdminByAccount(account);
//        System.out.println(JSON.toJSONString(admin));
    }
    @Test
    public void selectAllTeacher(){
//        List<Teacher> teachers = teacherService.selectAllTeacher();
//        System.out.println("teacher个数"+teachers.size());
//        System.out.println(JSON.toJSONString(teachers));
    }

    @Test
    public void getRole(){
        //System.out.println(new RoleUtil().addTeacher().buildString());
    }

    @Test//更改教师账户密码
    private void m1(){

    }
}
