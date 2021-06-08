package com.wangzhen.controllers.teacher;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.problem.KnowledgeArray;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.problemservice.KnowledgeArrayService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 操作知识点集合的Controller
 * @CreateDate 2020/2/13 16:22
 */
@RestController
@RequestMapping("/teacher")
@Slf4j
@SuppressWarnings("all")
public class KnowledgeArrayController {
    @Autowired
    private KnowledgeArrayService knowledgeArrayService;

    @RequestMapping("/selectKnowledgeArrayByName")
    public KnowledgeArray selectKnowledgeArrayByName(HttpSession session, String name){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return knowledgeArrayService.selectKnowledgeArrayByName(teacher.getAccount(),name);
    }
    @RequestMapping("/selectAllKnowledgeArray")
    public List<KnowledgeArray> selectAllKnowledgeArray(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return knowledgeArrayService.selectAllKnowledgeArray(teacher.getAccount());
    }
    @RequestMapping("/insertKnowledgeArrayList")
    public ResponseMessage insertKnowledgeArrayList(HttpSession session, String knowledgeArrayList){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        List<KnowledgeArray> knowledgeArrays = JSON.parseArray(knowledgeArrayList, KnowledgeArray.class);
        knowledgeArrayService.insertKnowledgeArrayList(teacher.getAccount(),knowledgeArrays);
        return new ResponseMessage().setCode(200);

    }
    @RequestMapping("/insertKnowledgeArray")
    public ResponseMessage insertKnowledgeArray(HttpSession session, @RequestParam("name") String name, @RequestParam("knowledgeList") String knowledgeList){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        String uuid = Utils.randomUuid();
        boolean tag = knowledgeArrayService.insertKnowledgeArray(uuid,teacher.getAccount(),name,knowledgeList);
        return new ResponseMessage().setCode(tag?200:403).setData(uuid);

    }
    @RequestMapping("/updateKnowledgeArrayList")
    public ResponseMessage updateKnowledgeArrayList(HttpSession session, String knowledgeArrayList){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        knowledgeArrayService.updateKnowledgeArrayList(teacher.getAccount(),knowledgeArrayList);
        return new ResponseMessage().setCode(200);
    }
    @RequestMapping("/deleteKnowledgeArrayByUuid")
    public ResponseMessage deleteKnowledgeArrayByUuid(HttpSession session, String uuid){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        boolean tag = knowledgeArrayService.deleteKnowledgeArrayByUuid(teacher.getAccount(),uuid);
        return new ResponseMessage().setCode(tag?200:403);
    }
}
