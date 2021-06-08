package com.wangzhen.configuration;
import com.wangzhen.login.facelogin.FaceLoginAuthenticationConfig;
import com.wangzhen.login.facelogin.FaceLoginAuthenticationFilter;
import com.wangzhen.login.facelogin.FaceLoginValidDataFilter;
import com.wangzhen.staticparamter.AuthenticationStaticParamter;
import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.login.formlogin.CustomAuthenticationFailureHandler;
import com.wangzhen.login.formlogin.CustomAuthenticationSuccessHandler;
import com.wangzhen.login.formlogin.LoginUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author wangzhen
 * @Description ctrl+o覆盖父类方法
 * @CreateDate 2020/1/11 18:00
 */
@Configurable
@EnableWebSecurity//开启springSecurity过滤链
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired private LoginUserDetailService customUserDetailService;
    @Autowired private AuthenticationStaticParamter authenticationStaticParamter;
    @Autowired private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;



    /**
     * @Description 认证管理器
     *          1.认证信息（用户名，密码）
     * @date 2020/1/11 18:00
     * @param auth
     * @return void
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //1.SpringSecurity的内存模式认证
//        String password = passwordEncoder().encode("1234");
//        auth.inMemoryAuthentication()
//                .withUser("wangzhen")
//                .password(password)
//                .authorities(UserIdentity.ADMIN.name());
//        logger.info("#加密之后的密码是："+password);
        //2.SpringSecurity的结合数据库的认证
        auth.userDetailsService(customUserDetailService);
    }

    @Autowired
    private FaceLoginAuthenticationConfig faceLoginAuthenticationConfig;
    @Autowired
    private FaceLoginValidDataFilter faceLoginValidDataFilter;
    /**
     * @Description 资源权限配置
     *          1.被拦截的资源
     *
     * @date 2020/1/11 18:00
     * @param http
     * @return void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(faceLoginValidDataFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin()//表单登录方式
                .loginPage(authenticationStaticParamter.getLoginPage())
                .usernameParameter(authenticationStaticParamter.getUsernameParameter())
                .passwordParameter(authenticationStaticParamter.getPasswordParameter())
                .loginProcessingUrl(authenticationStaticParamter.getLoginProcessingUrl())
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
            .and()
                //应用人脸登录配置
                .apply(faceLoginAuthenticationConfig)
            .and()
                .authorizeRequests()    //请求认证

                .antMatchers(authenticationStaticParamter.getRequestUrlStudent()).hasAuthority(UserIdentity.STUDENT)
                .antMatchers(authenticationStaticParamter.getRequestUrlAdmin()).hasAuthority(UserIdentity.ADMIN)
                .antMatchers(authenticationStaticParamter.getRequestUrlTeacher()).hasAuthority(UserIdentity.TEACHER)
                .antMatchers(authenticationStaticParamter.getRequestUrlManager()).hasAuthority(UserIdentity.MANAGER)
                .antMatchers(authenticationStaticParamter.getRequestUrl()).authenticated()
                .antMatchers(authenticationStaticParamter.getPermitHtmls()).permitAll()
                   //所有访问该应用的http请求都要经过身份认证才可以访问
            .and()
                .csrf()
                .disable()
                ;
    }
    /**
     * @Description  放行静态资源
     * @date 2020/1/11 18:00
     * @param web
     * @return void
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
           .antMatchers(authenticationStaticParamter.getPermitStaticResources());
    }
}
