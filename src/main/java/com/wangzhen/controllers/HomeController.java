package com.wangzhen.controllers;
import com.wangzhen.configuration.MyWebAppConfig;
import com.wangzhen.staticparamter.UserIdentity;
import com.wangzhen.utils.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/11 18:00
 */
@Controller
@SuppressWarnings("all")
@Slf4j
public class HomeController {
    @RequestMapping({"/","/index","/index.html","/home","/home.index",""})
    public String home(HttpSession session){
//        Object session_current_user_student = request.getSession().getAttribute(UserIdentity.STUDENT);
//        Object session_current_user_admin = request.getSession().getAttribute(UserIdentity.ADMIN);
//        Object session_current_user_manager = request.getSession().getAttribute(UserIdentity.MANAGER);
//        Object session_current_user_teacher = request.getSession().getAttribute(UserIdentity.TEACHER);
//        String session_current_user = null;
//        if(session_current_user_teacher != null) session_current_user = UserIdentity.TEACHER;
//        if(session_current_user_manager != null) session_current_user = UserIdentity.MANAGER;
//        if(session_current_user_admin != null) session_current_user = UserIdentity.ADMIN;
//        if(session_current_user_student != null) session_current_user = UserIdentity.STUDENT;
        String session_current_user = (String)session.getAttribute(UserIdentity.SESSION_CURRENT_USER);
        if(session_current_user == null) return "/login.html";
        switch (session_current_user){
            case UserIdentity.STUDENT:
                return "/student/home.html";
            case UserIdentity.TEACHER:
                return "/teacher/home.html";
            case UserIdentity.MANAGER:
                return "/manager/home.html";
            case UserIdentity.ADMIN:
                return "/admin/home.html";
        }
        return "redirect:/login.html";
    }
//    @RequestMapping("/goToPage/{user}/{pageName}/{data}")
//    public String goToPage(@PathVariable(name="user",required = true)String user, @PathVariable(name="pageName",required = true)String pageName,@PathVariable(name="data",required = false) String data,HttpServletRequest request){
//        String session_current_user = (String) request.getSession().getAttribute(FinalString.SESSION_CURRENT_USER);
//        if(session_current_user.equalsIgnoreCase(user)){
//            //有权限访问对应的页面
//            return "/"+user+"/"+pageName+".html";
//        }else
//            return "/"+user+"/"+"404.html";
//    }

    @RequestMapping("/quit")
    public String quit(HttpSession session,HttpServletRequest request){
        session.invalidate();

        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String headName = headerNames.nextElement();
            String head = request.getHeader(headName);
            System.out.println("HeadName:"+headName+"    head"+head);
        }


//      <c:forEach items="${topMenuList }" var="topMenu">
        List<String> topMenuList = new ArrayList<>();
        for (String topMenu : topMenuList) {

        }
        return "redirect:/login.html";
    }

//    @RequestMapping("/errore")
//    public String error(HttpServletRequest request){
//        Enumeration<String> attributeNames = request.getAttributeNames();
//        String s;
//        while ((attributeNames.hasMoreElements())){
//            s = attributeNames.nextElement();
//            log.warn(s+"#---#"+request.getAttribute(s));
//        }
//        return "redirect:/login.html";
//    }


    @RequestMapping("/init")
    public ResponseMessage init() throws FileNotFoundException, SQLException {
        String path = HomeController.class.getResource("/com/wangzhen/configuration/examsystem_最终版.sql").getPath();
        String sql = readFileContent(path);
        DataSource bean = MyWebAppConfig.context.getBean(DataSource.class);
        Connection connection = bean.getConnection();
        Statement stmt = connection.createStatement() ;
        PreparedStatement pstmt = connection.prepareStatement(sql);
        int i = pstmt.executeUpdate();
        return new ResponseMessage().setCode(i>0?200:403);
    }

    public String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

}
