package com.wangzhen.login.formlogin;

import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.models.users.Admin;
import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Student;
import com.wangzhen.models.users.Teacher;
import com.wangzhen.utils.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author wangzhen
 * @Description 登录成功的认证处理器
 * @CreateDate 2020/1/13 17:45
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
//        logger.info("#authentication的json内容："+JSON.toJSONString(authentication));
        if(principal instanceof Student) {
            request.getSession().setAttribute(UserIdentity.STUDENT,principal);
            request.getSession().setAttribute(UserIdentity.SESSION_CURRENT_USER,UserIdentity.STUDENT);
        }
        if(principal instanceof Teacher) {
            request.getSession().setAttribute(UserIdentity.TEACHER, principal);
            request.getSession().setAttribute(UserIdentity.SESSION_CURRENT_USER,UserIdentity.TEACHER);
        }
        if(principal instanceof Manager) {
            request.getSession().setAttribute(UserIdentity.MANAGER, principal);
            request.getSession().setAttribute(UserIdentity.SESSION_CURRENT_USER,UserIdentity.MANAGER);
        }
        if(principal instanceof Admin) {
            request.getSession().setAttribute(UserIdentity.ADMIN, principal);
            request.getSession().setAttribute(UserIdentity.SESSION_CURRENT_USER,UserIdentity.ADMIN);
        }

//        Enumeration<String> names = request.getSession().getAttributeNames();
//        while (names.hasMoreElements()){
//            String s = names.nextElement();
//            System.out.println("#"+s+"内容--->"+request.getSession().getAttribute(s));
//        }


        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ResponseMessage()
                                        .setCode(HttpStatus.OK.value())
                                        .setMsg("登录成功,请重新请求 href 路径")
                                        .setHref("/home")
                                        .toJsonString());
        ;
    }
}
