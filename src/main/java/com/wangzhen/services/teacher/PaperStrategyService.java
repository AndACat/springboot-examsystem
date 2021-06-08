package com.wangzhen.services.teacher;

import com.wangzhen.dao.teacher.PaperStrategyDao;
import com.wangzhen.models.PaperStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/1 17:18
 */
@Service
public class PaperStrategyService {
    @Autowired
    private PaperStrategyDao paperStrategyDao;
    public List<PaperStrategy> selectMyAllPaperStrategy(String account){
        return paperStrategyDao.selectMyAllPaperStrategy(account);
    }
    public void insertPaperStrategy(PaperStrategy paperStrategy, String account){
       paperStrategyDao.insertPaperStrategy(paperStrategy,account);
    }
    public void deletePaperStrategy(String account, String uuid){
        paperStrategyDao.deletePaperStrategy(account,uuid);
    }
    public void updatePaperStrategyInfo(PaperStrategy paperStrategy,String account){
        paperStrategyDao.updatePaperStrategyInfo(paperStrategy,account);
    }
    public PaperStrategy selectPaperStrategyByPaperStrategy_uuid(String uuid){
        return paperStrategyDao.selectPaperStrategyByPaperStrategy_uuid(uuid);
    }
}
