package com.wangzhen.staticparamter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/13 16:10
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@Component
@ConfigurationProperties("authentication-static-paramter")
public class AuthenticationStaticParamter {
    private String loginPage;
    private String usernameParameter;
    private String passwordParameter;
    private String loginProcessingUrl;
    private String defaultSuccessUrl;
    private String failureForwardUrl;

    private String[] permitHtmls;
    private String[] permitStaticResources;

    private String[] requestUrlAdmin;//必须认证的，且是管理员才能访问的
    private String[] requestUrlTeacher;
    private String[] requestUrlStudent;
    private String[] requestUrlManager;
    private String[] requestUrl;//必须认证才能让问的

}
