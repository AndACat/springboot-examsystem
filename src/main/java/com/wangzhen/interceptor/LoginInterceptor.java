package com.wangzhen.interceptor;
import com.wangzhen.staticparamter.UserIdentity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/23 17:17
 */
//@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object teacher = request.getSession().getAttribute(UserIdentity.TEACHER);
        Object student = request.getSession().getAttribute(UserIdentity.STUDENT);
        Object admin = request.getSession().getAttribute(UserIdentity.ADMIN);
        Object manager = request.getSession().getAttribute(UserIdentity.MANAGER);
        boolean hasLogin = teacher!=null || student!=null || admin!=null || manager!=null;
        log.debug("当前用户是否登录"+hasLogin);
        if(!hasLogin) response.sendRedirect("/login.html");
        return hasLogin;
    }
}
