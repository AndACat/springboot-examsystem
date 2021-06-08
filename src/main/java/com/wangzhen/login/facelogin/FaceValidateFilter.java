package com.wangzhen.login.facelogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 18:06
 */
public class FaceValidateFilter extends OncePerRequestFilter {
    @Autowired
    private CustomFaceLoginAuthenticationFailureHandler customFaceLoginAuthenticationFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if("/facelogin".equals(request.getRequestURI()) && "post".equalsIgnoreCase(request.getMethod()) ){
            try {
                Part face = request.getPart("face");
                if(face == null){
                    customFaceLoginAuthenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationException("照片不能为空") {
                        @Override
                        public String getMessage() {
                            return super.getMessage();
                        }
                    });
                    return;
                }
            }catch (AuthenticationException e){
                customFaceLoginAuthenticationFailureHandler.onAuthenticationFailure(request, response,e);
            }
            filterChain.doFilter(request,response);
        }
    }
}
