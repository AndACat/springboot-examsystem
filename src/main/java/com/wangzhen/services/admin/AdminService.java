package com.wangzhen.services.admin;

import com.alibaba.fastjson.JSON;
import com.wangzhen.dao.admin.userdao.AdminDao;
import com.wangzhen.models.users.Admin;
import com.wangzhen.models.users.Role;
import com.wangzhen.utils.UserCheckUtil;
import com.wangzhen.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * @Author wangzhen
 * @Description 管理员的服务层
 * @CreateDate 2020/1/12 21:00
 */
@Component
@SuppressWarnings("all")
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * @Description 插入管理员账户
     * @date 2020/1/12 21:01
     * @param name
     * @param account
     * @param code
     * @param roles
     * @return boolean
     */
    public boolean insertAdmin(String name,String account, String code, List<Role> roles){
        return new UserCheckUtil()
                .checkAccount(account)
                .checkName(name)
                .checkCode(code)
                .checkAbove() == true
                ?
                adminDao.insertAdmin(Utils.randomUuid(),name,account,passwordEncoder.encode(code), JSON.toJSONString(roles)) : false;
    }

    /**
     * @Description
     * @date 2020/1/13 16:02
     * @param account
     * @return boolean
     */
    public boolean deleteAdmin(String account){
        return adminDao.deleteAdmin(account);
    }

    /**
     * @Description
     * @date 2020/1/13 16:02
     * @param account
     * @return com.wangzhen.models.users.Admin
     */
    public Admin selectAdminByAccount(String account){
        return adminDao.selectAdminByAccount(account);
    }
    /**
     * @Description
     * @date 2020/1/13 16:03
     * @param
     * @return java.util.List<com.wangzhen.models.users.Admin>
     */
    public List<Admin> selectAllAdmin(){
        return adminDao.selectAllAdmin();
    }
    /**
     * @Description
     * @date 2020/1/13 16:03
     * @param account
     * @param code
     * @return boolean
     */
    public boolean updateAdminCode(String account,String code){
        return new UserCheckUtil()
                .checkCode(code)
                .checkAbove() == true
                ?
                adminDao.updateAdminCode(account,passwordEncoder.encode(code)) : false;
    }
    /**
     * @Description
     * @date 2020/1/13 16:03
     * @param account
     * @param name
     * @param age
     * @param email
     * @param phone
     * @param acccountNonExpired
     * @param accountNonLocked
     * @param credentialsNonExpired
     * @param enabled
     * @return boolean
     */
    public boolean updateAdminInfo(String account,String name,Integer age,String email,String phone,boolean acccountNonExpired,boolean accountNonLocked,boolean credentialsNonExpired,boolean enabled){
        return new UserCheckUtil()
                .checkName(name)
                .checkAge(age)
                .checkEmail(email)
                .checkPhone(phone)
                .checkAbove() == true
                ?
                adminDao.updateAdminInfo(account,name,age,email,phone,acccountNonExpired,accountNonLocked,credentialsNonExpired,enabled) : false;
    }
    /**
     * @Description
     * @date 2020/1/13 16:05
     * @param account
     * @return boolean
     */
    public boolean updateAdminAccount(String account){
        return new UserCheckUtil()
                .checkAccount(account)
                .checkAbove() == true
                ?
                adminDao.updateAdminAccount(account) : false;
    }
}
