package com.wangzhen.login.facelogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/9 14:25
 */
@Component
public class FaceLoginValidDataFilter extends OncePerRequestFilter {
    @Autowired
    private CustomFaceLoginAuthenticationFailureHandler customFaceLoginAuthenticationFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/facelogin".equals(request.getRequestURI()) && "post".equalsIgnoreCase(request.getMethod())) {
            try {
                if(null == request.getPart("face")){
                    customFaceLoginAuthenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("没有上传照片"));
                }
            } catch (AuthenticationException e) {
                customFaceLoginAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        // 非手机验证码登录，则直接放行    
        filterChain.doFilter(request, response);
    }
}
