package com.wangzhen.controllers.teacher;

import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.admin.TeacherService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/5 23:51
 */
@RestController
@RequestMapping("/teacher")
public class TeacherInfoController {
    @Autowired
    private TeacherService teacherService;
    @RequestMapping("/myInfo")
    public ResponseMessage myInfo(HttpSession session){
        Teacher user = Utils.getUser(session, Teacher.class);
        return ResponseMessage.getInstance().setCode(200).setData(user);
    }

    @RequestMapping("/updateMyInfo")
    public ResponseMessage updateMyInfo(@RequestParam("account") String account, @RequestParam("name") String name, @RequestParam("code") String code,HttpSession session){
        teacherService.updateTeacherInfo(account,name,code);
        session.invalidate();
        return ResponseMessage.OK;
    }

}
