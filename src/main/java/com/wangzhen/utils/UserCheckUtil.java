package com.wangzhen.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/13 13:45
 */
@Component
@ConfigurationProperties(prefix = "usercheckutil")
public class UserCheckUtil {
    private Integer maxAccountLength;
    private Integer minAccountLength;
    private String emailRegex;
    private String phoneRegex;
    private Integer minAge;
    private Integer maxAge;
    private Integer minNameLength;
    private Integer maxNameLength;
    private Integer minCodeLength;
    private Integer maxCodeLength;
    boolean tag = true;
    public UserCheckUtil checkAccount(String account){
        if(account == null || account.isEmpty() || account.length() < minAccountLength || account.length() > maxAccountLength){
            tag = false;
        }
        return this;
    }
    public UserCheckUtil checkEmail(String email){
        if(email == null || email.isEmpty() || !email.matches(emailRegex))
            tag = false;
        return this;
    }
    public UserCheckUtil checkPhone(String phone){
        if(phone == null || phone.isEmpty() || !phone.matches(phoneRegex))
            tag = false;
        return this;
    }
    public UserCheckUtil checkAge(Integer age){
        if(age == null || age < minAge || age > maxAge)
            tag = false;
        return this;
    }
    public UserCheckUtil checkName(String name){
        if(name == null || name.isEmpty() || name.length() < minNameLength || name.length() > maxNameLength)
            tag = false;
        return this;
    }
    public UserCheckUtil checkCode(String code){
        if(code == null || code.isEmpty() || code.length() < minCodeLength || code.length() > maxCodeLength)
            tag = false;
        return this;
    }
    public boolean checkAbove(){
        return tag;
    }
    public void setMaxAccountLength(Integer maxAccountLength) {
        this.maxAccountLength = maxAccountLength;
    }
    public void setMinAccountLength(Integer minAccountLength) {
        this.minAccountLength = minAccountLength;
    }
    public void setEmailRegex(String emailRegex) {
        this.emailRegex = emailRegex;
    }
    public void setPhoneRegex(String phoneRegex) {
        this.phoneRegex = phoneRegex;
    }
    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }
    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }
    public void setMinNameLength(Integer minNameLength) {
        this.minNameLength = minNameLength;
    }
    public void setMaxNameLength(Integer maxNameLength) {
        this.maxNameLength = maxNameLength;
    }
    public void setMinCodeLength(Integer minCodeLength) {
        this.minCodeLength = minCodeLength;
    }

    public void setMaxCodeLength(Integer maxCodeLength) {
        this.maxCodeLength = maxCodeLength;
    }

    @Override
    public String toString() {
        return "UserCheck{" +
                "maxAccountLength=" + maxAccountLength +
                ", minAccountLength=" + minAccountLength +
                ", emailRegex='" + emailRegex + '\'' +
                ", phoneRegex='" + phoneRegex + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", minNameLength=" + minNameLength +
                ", maxNameLength=" + maxNameLength +
                ", tag=" + tag +
                '}';
    }
}
