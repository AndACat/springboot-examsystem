package com.wangzhen.services.teacher.problemservice;

import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.teacher.problemdao.KnowledgeArrayDao;
import com.wangzhen.models.problem.KnowledgeArray;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author wangzhen
 * @Description 知识点集合操作的服务层
 * @CreateDate 2020/2/13 16:17
 */
@Component
public class KnowledgeArrayService {
    @Autowired
    private KnowledgeArrayDao knowledgeArrayDao;

    public KnowledgeArray selectKnowledgeArrayByName(String account, String name){
        return knowledgeArrayDao.selectKnowledgeArrayByName(account,name);
    }

    public List<KnowledgeArray> selectAllKnowledgeArray(String account){
        return knowledgeArrayDao.selectAllKnowledgeArray(account);
    }

    public boolean insertKnowledgeArray(String uuid, String account, String name, String knowledgeList){
        return knowledgeArrayDao.insertKnowledgeArray(account,name,knowledgeList);
    }

    public boolean updateKnowledgeArrayByUuid(String account,int sort,String uuid, String name, String knowledgeList){
        return knowledgeArrayDao.updateKnowledgeArrayByUuid(account,sort,uuid,name,knowledgeList);
    }

    public boolean deleteKnowledgeArrayByUuid(String account, String uuid){
        return knowledgeArrayDao.deleteKnowledgeArrayByUuid(account,uuid);
    }


    public void insertKnowledgeArrayList(String account, List<KnowledgeArray> knowledgeArrays) {
//        for (int i = knowledgeArrays.size()-1; i != 0; i--) {
//            KnowledgeArray k = knowledgeArrays.get(i);
//            this.updateKnowledgeArrayByUuid(account,k.getUuid(),k.getName(),JSON.toJSONString(k.getKnowledgeList()));
//        }
        for(KnowledgeArray k : knowledgeArrays){
            String name = k.getName();
            String knowledgeList = JSON.toJSONString(k.getKnowledgeList());
            knowledgeArrayDao.insertKnowledgeArray(account,name, knowledgeList);
        }
    }

    public void updateKnowledgeArrayList(String account, String knowledgeArrayList) {
        List<KnowledgeArray> knowledgeArrays = JSON.parseArray(knowledgeArrayList,KnowledgeArray.class);
//        knowledgeArrayDao.deleteAllKnowledgeArray(account);
        int sort = 0;
        for (KnowledgeArray knowledgeArray : knowledgeArrays) {
            updateKnowledgeArrayByUuid(account,sort++,knowledgeArray.getUuid(),knowledgeArray.getName(),JSON.toJSONString(knowledgeArray.getKnowledgeList()));
        }
    }

    public boolean updateKnowledgeArrayByName(String account, String name, List<String> knowledgeList){
        return knowledgeArrayDao.updateKnowledgeArrayByName(account,name,JSON.toJSONString(knowledgeList));
    }
}
