package com.wangzhen.controllers;

import com.wangzhen.models.College;
import com.wangzhen.services.admin.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author wangzhen
 * @Description 学院及其专业的设置
 * @CreateDate 2020/2/7 21:47
 */
@RestController
public class CollegeController {
    @Autowired
    private CollegeService collegeService;

    private List<College> collegeList;

    @RequestMapping("/selectAllCollege")
    public List<College> selectAllCollege(){
        return collegeService.selectAllCollege();
    }

    @RequestMapping("/insertCollege")
    public boolean insertCollege(String name,String professionList){
        return collegeService.insertCollege(name,professionList);
    }

    @RequestMapping("/updateCollege")
    public boolean updateCollege(String uuid, String name, String professionList){
        return collegeService.updateCollege(name,professionList,uuid);
    }

    @RequestMapping("/deleteCollegeByUuid")
    public boolean deleteCollegeByUuid(String uuid){
        return collegeService.deleteCollegeByUuid(uuid);
    }
}
