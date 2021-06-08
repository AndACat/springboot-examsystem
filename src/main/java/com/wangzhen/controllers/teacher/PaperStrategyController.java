package com.wangzhen.controllers.teacher;
import com.wangzhen.models.PaperStrategy;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.services.teacher.PaperService;
import com.wangzhen.services.teacher.PaperStrategyService;
import com.wangzhen.utils.ResponseMessage;
import com.wangzhen.utils.Utils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/1 17:34
 */
@RestController
@RequestMapping("/teacher")
public class PaperStrategyController {
    @Autowired
    private PaperStrategyService paperStrategyService;
    @Autowired
    private PaperService paperService;
    @RequestMapping("/selectMyAllPaperStrategy")
    public List<PaperStrategy> selectMyAllPaperStrategy(HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        return paperStrategyService.selectMyAllPaperStrategy(teacher.getAccount());
    }
    @RequestMapping("/insertPaperStrategy")
    public ResponseMessage insertPaperStrategy(@Param("paperStrategy") PaperStrategy paperStrategy, HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        String uuid = Utils.randomUuid();
        paperStrategy.setUuid(uuid);
        paperStrategyService.insertPaperStrategy(paperStrategy,teacher.getAccount());
        return new ResponseMessage().setCode(200).setData(uuid);
    }
    @RequestMapping("/deletePaperStrategy")
    public ResponseMessage deletePaperStrategy(HttpSession session, @RequestParam("uuid") String uuid){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        if(paperService.selectCountOfPaperByPaperStrategy_uuid(uuid) != 0){
            return ResponseMessage.getInstance().setCode(403).setMsg("无法删除,该策略被其他试卷引用。");
        }
        paperStrategyService.deletePaperStrategy(teacher.getAccount(),uuid);
        return ResponseMessage.OK;
    }
    @RequestMapping("/updatePaperStrategyInfo")
    public ResponseMessage updatePaperStrategyInfo(@Param("paperStrategy")PaperStrategy paperStrategy,HttpSession session){
        Teacher teacher = Utils.getUser(session, Teacher.class);
        paperStrategyService.updatePaperStrategyInfo(paperStrategy,teacher.getAccount());
        return ResponseMessage.OK;
    }
    @RequestMapping("/selectPaperStrategyByPaperStrategy_uuid")
    public PaperStrategy selectPaperStrategyByPaperStrategy_uuid(@RequestParam("paperStrategy_uuid") String paperStrategy_uuid){
        return paperStrategyService.selectPaperStrategyByPaperStrategy_uuid(paperStrategy_uuid);
    }
}
