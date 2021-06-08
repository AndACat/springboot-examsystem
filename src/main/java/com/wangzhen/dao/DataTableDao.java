package com.wangzhen.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/4 16:01
 */
@Mapper
@Repository
public interface DataTableDao {
    @Update("create table if not exists cla(" +
            "        uuid varchar(50) primary key," +
            "        college varchar(50) not null," +
            "        profession varchar(50) not null," +
            "        claName varchar(50) not null," +
            "        student_uuid_list varchar(2048) default null," +
            "        course_uuid_list varchar(2048) default null ," +
            "        sort timestamp default current_timestamp on update current_timestamp " +
            "    )")
    void createClaTable();

    @Update("create table if not exists paperStrategy(" +
            "uuid varchar(50) primary key," +
            "account varchar(50) not null," +
            "paperStrategyName varchar(50) not null," +
            "allScore float not null," +
            "problemStrategyList varchar(1024) not null," +
            "sort timestamp default current_timestamp on update current_timestamp" +
            ")")
    void createPaperStrategyTable();

    @Update("create table if not exists course(" +
            "    uuid varchar(50) primary key," +
            "    courseName varchar(50) not null ," +
            "    code varchar (50) not null ," +
            "    tag bit not null default true," +
            "    teacher_uuid varchar (50) not null," +
            "    cla_uuid varchar (50) not null," +
            "    sort timestamp default current_timestamp  on update current_timestamp " +
            "    )")
    void createCourseTable();

    @Update("create table if not exists knowledgeArray(" +
            "uuid varchar(50) PRIMARY KEY," +
            "account varchar(50) not null," +
            "name varchar(64) not null," +
            "knowledgeList varchar(1024) not null default \"[]\"," +
            "sort int" +
            ")")
    boolean createKnowledgeArray();

    @Update("set global time_zone = '+8:00'")
    void initServerTimeZone();
}
