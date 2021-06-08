package com.wangzhen.services.admin;

import com.wangzhen.dao.admin.CollegeDao;
import com.wangzhen.models.College;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/7 21:35
 */
@Service
public class CollegeService {
    @Autowired
    private CollegeDao collegeDao;

    public boolean insertCollege(String name,String professionList){
        return null == collegeDao.selectCollegeByName(name)?collegeDao.insertCollege(Utils.randomUuid(),name,professionList):false;
    }

    public boolean updateCollege(String name,String professionList,String uuid){
        return collegeDao.updateCollege(name,professionList,uuid);
    }

    public List<College> selectAllCollege(){
        return collegeDao.selectAllCollege();
    }

    public College selectCollegeByName(String name){
        return collegeDao.selectCollegeByName(name);
    }

    /**
     * @Description 根据profession模糊查询college
     * @date 2020/2/7 21:44
     * @param profession
     * @return com.wangzhen.models.College
     */
    public College selectCollegeByProfession(String profession){
        return collegeDao.selectCollegeByName("%"+profession+"%");
    }

    public boolean deleteCollegeByUuid(String uuid){
        return collegeDao.deleteCollegeByUuid(uuid);
    }

    public boolean deleteCollegeByName(String name){
        return collegeDao.deleteCollegeByName(name);
    }
}
