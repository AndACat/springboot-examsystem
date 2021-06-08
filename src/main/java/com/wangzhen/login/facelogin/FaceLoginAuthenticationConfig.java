package com.wangzhen.login.facelogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 21:03
 */
@Component
public class FaceLoginAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private FaceLoginUserDetailService faceLoginUserDetailService;
    @Autowired
    private CustomFaceLoginAuthenticationSuccessHandler customFaceLoginAuthenticationSuccessHandler;
    @Autowired
    private CustomFaceLoginAuthenticationFailureHandler customFaceLoginAuthenticationFailureHandler;

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        FaceLoginAuthenticationFilter faceLoginAuthenticationFilter = new FaceLoginAuthenticationFilter();
        faceLoginAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        faceLoginAuthenticationFilter.setAuthenticationFailureHandler(customFaceLoginAuthenticationFailureHandler);
        faceLoginAuthenticationFilter.setAuthenticationSuccessHandler(customFaceLoginAuthenticationSuccessHandler);

        FaceLoginAuthenticationProvider faceLoginAuthenticationProvider = new FaceLoginAuthenticationProvider(faceLoginUserDetailService);
        builder.authenticationProvider(faceLoginAuthenticationProvider)
                .addFilterAfter(faceLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);//将faceLoginAuthenticationFilter加入到密码认证之后

    }
}
