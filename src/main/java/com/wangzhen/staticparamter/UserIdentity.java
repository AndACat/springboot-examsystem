package com.wangzhen.staticparamter;

import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/14 15:24
 */
@Component
public class UserIdentity {
    public static final String SESSION_CURRENT_USER = "SESSION_CURRENT_USER";
    public static final String ADMIN = "ADMIN";
    public static final String STUDENT = "STUDENT";
    public static final String TEACHER = "TEACHER";
    public static final String MANAGER = "MANAGER";
}
