package com.wangzhen.login.facelogin;

import com.wangzhen.configuration.MyWebAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 18:13
 */
public class FaceLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String faceParameter = "face";
    private boolean postOnly = true;

    private CustomFaceLoginAuthenticationFailureHandler customFaceLoginAuthenticationFailureHandler = MyWebAppConfig.context.getBean(CustomFaceLoginAuthenticationFailureHandler.class);

    public FaceLoginAuthenticationFilter() {
        super(new AntPathRequestMatcher("/facelogin", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,

                                                HttpServletResponse response) throws IOException, ServletException {
        try{
            if (postOnly && !"POST".equals(request.getMethod())) {
                throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
            }
            Part face = obtainFace(request);


            if (face == null) {
                throw new IllegalArgumentException("照片不能为空");
            }

            FaceLoginAuthenticationToken authRequest = new FaceLoginAuthenticationToken(face);

            authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
            return this.getAuthenticationManager().authenticate(authRequest);
        }catch (Exception e){
            customFaceLoginAuthenticationFailureHandler.onAuthenticationFailure(request,response,new AuthenticationServiceException(e.getMessage()));
        }
        return null;
    }


    protected Part obtainFace(HttpServletRequest request) throws IOException, ServletException {
        return request.getPart(faceParameter);
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }


    public String getFaceParameter() {
        return faceParameter;
    }

    public void setFaceParameter(String faceParameter) {
        this.faceParameter = faceParameter;
    }
}
