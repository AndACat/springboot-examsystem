package com.wangzhen.controllers.teacher;

import com.wangzhen.services.teacher.RandomPaperBuilderService;
import com.wangzhen.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/8 17:04
 */
@RestController
@RequestMapping("/teacher")
public class RandomPaperBuilderController {
    @Autowired
    private RandomPaperBuilderService paperBuilderService;


    @PostMapping("/randomPaper")
    public ResponseMessage randomPaper(@RequestParam("paperStrategy_uuid")String paperStrategy_uuid,//策略uuid
                                       @RequestParam("knowledgeMap")Map<String, List<String>> knowledgeMap,
                                       HttpSession session){
        ResponseMessage responseMessage = paperBuilderService.randomPaper(paperStrategy_uuid, knowledgeMap,session);




        return responseMessage;
    }
    /**
     * @Description
     * @date 2020/3/14 18:34
     * @param course_uuid 课程uuid
     * @param paperName 试卷名
     * @param during 考试时间
     * @param sort
     * @param loadAll
     * @param begin_time 最早开考时间
     * @param end_time 最迟开考时间
     * @return com.wangzhen.utils.ResponseMessage
     */
    @RequestMapping("/saveRandomPaper")
    public ResponseMessage saveRandomPaper(@RequestParam("course_uuid") String course_uuid,
                                           @RequestParam("paperName")String paperName,
                                           @RequestParam("during")Integer during,
                                           @RequestParam("sort")boolean sort,
                                           @RequestParam("openFaceIdentity")boolean openFaceIdentity,
                                           @RequestParam("begin_time")long begin_time,
                                           @RequestParam("end_time")long end_time,
                                           @RequestParam("visible") boolean visible,
                                           HttpSession session){
        boolean tag = paperBuilderService.savePaper(paperName, course_uuid, during, sort, openFaceIdentity, new Timestamp(begin_time), new Timestamp(end_time),visible ,session);
        return tag ? ResponseMessage.OK : ResponseMessage.FAIL;
    }
}
