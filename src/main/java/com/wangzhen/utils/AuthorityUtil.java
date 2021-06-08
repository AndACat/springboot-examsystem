package com.wangzhen.utils;

import com.wangzhen.staticparamter.UserIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author wangzhen
 * @Description Role角色类的工具类，快捷建立相应角色集合
 * @CreateDate 2020/1/12 21:14
 */
public class AuthorityUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthorityUtil.class);

    private Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    public static final SimpleGrantedAuthority admin = new SimpleGrantedAuthority(UserIdentity.ADMIN);
    public static final SimpleGrantedAuthority manager = new SimpleGrantedAuthority(UserIdentity.MANAGER);
    public static final SimpleGrantedAuthority teacher = new SimpleGrantedAuthority(UserIdentity.TEACHER);
    public static final SimpleGrantedAuthority student = new SimpleGrantedAuthority(UserIdentity.STUDENT);
    /**
     * @Description 返回默认为空的角色集合
     * @date 2020/1/12 21:15
     * @param
     * @return java.util.List<com.wangzhen.models.users.Role>
     */
    public AuthorityUtil def(){
        authorities = new ArrayList<SimpleGrantedAuthority>();
        return this;
    }
    public AuthorityUtil admin(){
        authorities.add(admin);
        return this;
    }
    public AuthorityUtil manager(){
        authorities.add(manager);
        return this;
    }
    public AuthorityUtil teacher(){
        authorities.add(teacher);
        return this;
    }
    public AuthorityUtil student(){
        authorities.add(student);
        return this;
    }
    public Collection<SimpleGrantedAuthority> build(){
        return authorities;
    }
}
