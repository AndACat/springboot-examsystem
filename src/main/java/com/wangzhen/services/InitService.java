package com.wangzhen.services;

import com.wangzhen.ExamsystemApplication;
import com.wangzhen.dao.DataTableDao;
import com.wangzhen.dao.manager.ClaDao;
import com.wangzhen.dao.teacher.PaperStrategyDao;
import com.wangzhen.services.manager.ClaService;
import com.wangzhen.services.teacher.PaperStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/4 15:55
 */
@Service
public class InitService {
    @Autowired
    private DataTableDao dataTableDao;

    public void init(){
        dataTableDao.initServerTimeZone();
        dataTableDao.createClaTable();
        dataTableDao.createPaperStrategyTable();
        dataTableDao.createCourseTable();
        dataTableDao.createKnowledgeArray();
    }
}
