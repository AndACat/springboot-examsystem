package com.wangzhen.models.users;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.Cla;
import com.wangzhen.models.Course;
import com.wangzhen.utils.AuthorityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 学生的实体类 继承自UserDetails
 * @CreateDate 2020/1/8 18:00
 */
@Builder
@AllArgsConstructor
public class Student implements UserDetails {
    public Student(){}
    private static Collection<SimpleGrantedAuthority> authorities = new AuthorityUtil().def().student().build();
    @Getter@Setter
    private String uuid;                                        //学生主键
    @Getter@Setter
    private String name;                                        //姓名
    @Getter@Setter
    private String account;                                     //账号
    @Getter@Setter
    private String code;                                        //密码
    @Getter@Setter
    private Date birthday;                                        //年龄
    @Getter@Setter
    private boolean sex;                                         //性别
    @Getter@Setter
    private String college;
    @Getter@Setter
    private String profession;
    @Getter@Setter
    private String sno;                                        //学号
    @Getter@Setter
    private String email;                                       //邮箱号
    @Getter@Setter
    private String phone;                                       //手机
    @Getter@Setter
    private String faceImg;
    @Getter@Setter
    private String photo;                                       //头像
    @Getter@Setter
    private String faceFeatureData;                                 //人脸识别特征值
    @Getter@Setter
    private String cla_uuid;                                          //所在班级
    @Getter@Setter
    private Cla cla;
    @Getter
    private List<String> course_uuid_list;                            //所学课程
    @Getter@Setter
    private List<Course> courseList;
    @Getter@Setter
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

    public void setCourse_uuid_list(String course_uuid_list) {
        this.course_uuid_list = JSON.parseArray(course_uuid_list,String.class);
    }
}
