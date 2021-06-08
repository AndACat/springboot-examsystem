package com.wangzhen.login.facelogin;

import com.wangzhen.models.users.Student;
import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/4/4 17:58
 */
@Component
public class CustomFaceLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getDetails();
        if(principal instanceof Student) {
            request.getSession().setAttribute(UserIdentity.STUDENT,principal);
            request.getSession().setAttribute(UserIdentity.SESSION_CURRENT_USER,UserIdentity.STUDENT);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ResponseMessage()
                .setCode(HttpStatus.OK.value())
                .setMsg("登录成功,请重新请求 href 路径")
                .setHref("/home")
                .toJsonString());
    }
}
