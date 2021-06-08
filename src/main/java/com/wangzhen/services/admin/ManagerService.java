package com.wangzhen.services.admin;

import com.alibaba.fastjson.JSON;
import com.wangzhen.staticparamter.UploadStaticParamter;
import com.wangzhen.dao.admin.userdao.ManagerDao;
import com.wangzhen.models.users.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/13 17:58
 */
@Component
public class ManagerService {
    @Autowired
    private ManagerDao managerDao;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UploadStaticParamter uploadStaticParamter;


    public Manager selectManagerByAccount(String account) {
        return managerDao.selectManagerByAccount(account,account);
    }

    public List<Manager> selectAllManager() {
        return managerDao.selectAllManager();
    }

    public void deleteManagerByAccount(String account) {
        managerDao.deleteManagerByAccount(account);
    }


    public void updateManagerInfo(Manager manager) {
        this.deleteFaceImgFile(manager.getAccount());
        managerDao.updateManagerInfo(manager);
    }

    public void deleteFaceImgFile(String account) {
        Manager manager = managerDao.selectManagerByAccount(account, account);
        if(manager == null) return;if(!((manager.getFaceImg() == null) || manager.getFaceImg()==null || "".equals(manager.getFaceImg()))){
            String faceImg = manager.getFaceImg().replace("/faces","");
            File file = new File(uploadStaticParamter.getFaceFolderLocalPath()+faceImg);
            if(file.exists()){
                file.delete();
            }
        }
    }

    public void changeCode(String account,String code) {
        managerDao.changeCode(account, encoder.encode(code));
    }

    /**
     * @Description 插入教师
     * @date 2020/2/18 22:15
     * @param manager 教师object
     * @return void
     */
    public void insertUser(Manager manager) {
        String faceFeatureData = JSON.toJSONString(manager.getFaceFeatureData());
        faceFeatureData = faceFeatureData.equalsIgnoreCase("null") ? null : faceFeatureData;
        manager.setCode(encoder.encode(manager.getPassword()));
        managerDao.insertManager(manager);
    }

    public void insertUser(List<Manager> managerList) {
        for (Manager manager : managerList) {
            this.insertUser(manager);
        }
    }

}
