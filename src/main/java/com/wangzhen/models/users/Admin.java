package com.wangzhen.models.users;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wangzhen.utils.AuthorityUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 管理员 实现了UserDetail接口
 * @CreateDate 2020/1/8 18:00
 */
@Getter
@Setter
@ToString
public class Admin implements UserDetails {
    private static Collection<SimpleGrantedAuthority> authorities = new AuthorityUtil().def().admin().manager().build();
    private String uuid;                                        //主键
    private String name;                                        //管理员姓名
    private String account;                                     //账号
    private String code;                                        //密码
    private Integer age;                                        //年龄
    private boolean sex;                                         //性别
    private String photo;                                       //头像
    private String faceImg;                                     //人脸图片
    private String faceFeature;                                 //人脸识别特征值
    private String email;                                       //邮箱号
    private String phone;                                       //电话
    private boolean enabled;                                    //账户是否可用
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return code;
    }
    @Override
    public String getUsername() {
        return account;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
