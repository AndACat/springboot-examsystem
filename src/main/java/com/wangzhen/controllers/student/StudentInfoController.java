package com.wangzhen.controllers.student;

import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.admin.TeacherService;
import com.wangzhen.services.manager.StudentService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/5 23:51
 */
@RestController
@RequestMapping("/student")
public class StudentInfoController {
    @Autowired
    private StudentService studentService;
    @RequestMapping("/myInfo")
    public ResponseMessage myInfo(HttpSession session){
        Student user = Utils.getUser(session, Student.class);
        return ResponseMessage.getInstance().setCode(200).setData(user);
    }

    @RequestMapping("/updateMyInfo")
    public ResponseMessage updateMyInfo(@RequestParam("account") String account, @RequestParam("name") String name, @RequestParam("code") String code,HttpSession session){
        studentService.updateStudentInfo(account,name,code);
        session.invalidate();
        return ResponseMessage.OK;
    }

}
