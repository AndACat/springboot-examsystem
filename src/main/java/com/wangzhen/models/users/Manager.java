package com.wangzhen.models.users;

import com.alibaba.fastjson.JSON;
import com.wangzhen.utils.AuthorityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 考务 实现了UserDetails接口
 * @CreateDate 2020/1/8 18:00
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Manager implements UserDetails {
    public Manager(){}
    private static Collection<SimpleGrantedAuthority> authorities = new AuthorityUtil().def().manager().build();
    private String uuid;                                        //主键
    private String name;                                        //考务员姓名
    private String account;                                     //账号
    private String code;                                        //密码
    private Date birthday;                                        //出生年月
    private boolean sex;                                         //性别
    private String college;
    private String profession;
    private String mno;//职工号
    private String faceImg;
    private String photo;                                       //头像
    private String faceFeatureData;                                 //人脸识别特征值
    private String tno;                 //职工号
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
