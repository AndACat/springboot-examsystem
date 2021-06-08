package com.wangzhen.login.facelogin;

import com.wangzhen.utils.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/14 11:22
 */
@Component
public class CustomFaceLoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomFaceLoginAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logger.info("#人脸登录失败处理器运行-登录失败: "+e.getMessage()+" || "+e.getLocalizedMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ResponseMessage()
                                        .setCode(HttpStatus.FORBIDDEN.value())
                                        .setMsg("登录失败,"+e.getMessage())
                                        .toJsonString());
    }
}
