package com.wangzhen.models.users;
import com.alibaba.fastjson.JSONArray;
import com.wangzhen.models.ClassAndCourse;
import com.wangzhen.utils.AuthorityUtil;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
/**
 * @Author wangzhen
 * @Description 教师的实体类
 * @CreateDate 2020/1/8 18:00
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Teacher implements UserDetails {
    private static Collection<SimpleGrantedAuthority> authorities = new AuthorityUtil().def().teacher().build();
    private String uuid;                //主键
    private String name;                //教师姓名
    private String account;             //账号
    private String code;                //密码
    private String college;             //学院名
    private String profession;          //专业名
    private Date birthday;                //出生年月
    private boolean sex;                //性别
    private String photo;               //头像
    private String faceImg;             //人脸图片
    private String faceFeatureData;         //人脸识别特征值
    private String tno;                 //职工号
    private String email;               //邮箱号
    private String phone;               //电话
    private List<ClassAndCourse> classAndCourseLists;           //班级和课程
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
    public void setClassAndCourseLists(String classAndCourseLists) {
        this.classAndCourseLists = JSONArray.parseArray(classAndCourseLists,ClassAndCourse.class);
    }
}
