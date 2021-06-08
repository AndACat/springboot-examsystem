package com.wangzhen;

import com.wangzhen.configuration.MyWebAppConfig;
import com.wangzhen.services.InitService;
import com.wangzhen.services.teacher.PaperStrategyService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.*;

@SpringBootApplication
@MapperScan("com.wangzhen.dao")
public class ExamsystemApplication {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        SpringApplication.run(ExamsystemApplication.class, args);
    }
}
