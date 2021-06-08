package com.wangzhen.controllers.manager;

import com.wangzhen.models.Cla;
import com.wangzhen.models.users.Manager;
import com.wangzhen.services.manager.ClaService;
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
 * @Description 班级类操作的控制层
 * @CreateDate 2020/3/5 14:57
 */
@RestController
@RequestMapping("/manager")
public class ClaController {
    @Autowired
    private ClaService claService;

    /**
     * @Description 查找自己所在学院的所有班级，考务只管理自己学院的考务
     * @date 2020/3/5 15:55
     * @param session
     * @return java.util.List<com.wangzhen.models.Cla>
     */
    @PostMapping("/selectMyCollegeAllCla")
    public List<Cla> selectMyCollegeAllCla(HttpSession session){
        Manager manager = Utils.getUser(session, Manager.class);
        String college = manager.getCollege();
        return claService.selectMyCollegeAllCla(college);
    }

    @PostMapping("/insertCla")
    public ResponseMessage insertCla(HttpSession session, @RequestParam("cla") Cla cla){
        Manager user = Utils.getUser(session, Manager.class);
        String uuid = Utils.randomUuid();
        cla.setCollege(user.getCollege());
        cla.setUuid(uuid);
        claService.insertCla(cla);
        return ResponseMessage.getInstance().setCode(200).setData(uuid);
    }

    @PostMapping("/deleteClaByUuid")
    public ResponseMessage deleteClaByUuid(@RequestParam("uuid")String uuid){
        claService.deleteClaByUuid(uuid);
        return ResponseMessage.OK;
    }

    @PostMapping("/updateClaInfoByUuid")
    public ResponseMessage updateClaInfoByUuid(@RequestParam("cla")Cla cla){
        claService.updateClaInfoByUuid(cla);
        return ResponseMessage.OK;
    }
}
