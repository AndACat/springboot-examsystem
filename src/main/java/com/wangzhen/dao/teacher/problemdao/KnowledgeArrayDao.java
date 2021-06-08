package com.wangzhen.dao.teacher.problemdao;
import com.wangzhen.models.problem.KnowledgeArray;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import java.util.List;
/**
 * @Description  知识点操作的Dao层
 * @date 2020/2/13 16:17
 */
@Component
@Mapper
public interface KnowledgeArrayDao {

    @Select("select * from knowledgeArray where name = #{name} and account = #{account}")
    KnowledgeArray selectKnowledgeArrayByName(@Param("account")String account,@Param("name")String name);

    @Select("select * from knowledgeArray where account = #{account} order by sort asc")
    List<KnowledgeArray> selectAllKnowledgeArray(@Param("account")String account);

    @Insert("insert into knowledgeArray (name,knowledgeList,account,sort) value (#{name}, #{knowledgeList},#{account},(select * from(select IFNULL((SELECT max(sort) + 1 FROM knowledgeArray),0)) as a ))")
//    @Insert("insert into knowledgeArray (uuid,name, knowledgeList,account) values (#{uuid},#{name}, #{knowledgeList},#{account})")
    boolean insertKnowledgeArray(@Param("account")String account,@Param("name") String name, @Param("knowledgeList")String knowledgeList);

//    @Update("update knowledgeArray set name = #{name}, knowledgeList = #{knowledgeList}, sort = #{sort} where account = #{account} and uuid = #{uuid}")
    @Update("update knowledgeArray set name = #{name}, knowledgeList = #{knowledgeList}, sort = (select * from(select IFNULL((SELECT max(sort) + 1 FROM knowledgeArray),0)) as a ) where account = #{account} and uuid = #{uuid}")
    boolean updateKnowledgeArrayByUuid(@Param("account")String account,@Param("sort")int sort, @Param("uuid")String uuid, @Param("name")String name, @Param("knowledgeList")String knowledgeList);

    @Update("update knowledgeArray set knowledgeList = #{knowledgeList} where account = #{account} and name = #{name}")
    boolean updateKnowledgeArrayByName(@Param("account")String account, @Param("name")String name, @Param("knowledgeList")String knowledgeList);

    @Delete("delete knowledgeArray where uuid = #{uuid} and account = #{account}")
    boolean deleteKnowledgeArrayByUuid(@Param("account") String account,@Param("uuid")String uuid);

    @Update("create table if not exists knowledgeArray(" +
            "uuid varchar(50) PRIMARY KEY," +
            "account varchar(50) not null," +
            "name varchar(64) not null," +
            "knowledgeList varchar(1024) not null default \"[]\"," +
            "sort int" +
            ")")
    boolean createKnowledgeArray();

    @Update("delete from knowledgeArray where account = #{account} and name != '默认分组'")
    boolean deleteAllKnowledgeArray(@Param("account") String account);


//    @Select("select * from knowledgeArray_${account} where name = #{name}")
//    KnowledgeArray selectKnowledgeArrayByName(String account,String name);
//
//    @Select("select * from knowledgeArray_${account} ")
//    List<KnowledgeArray> selectAllKnowledgeArray(String account);
//
//    @Insert("insert into knowledgeArray_${account} (name, knowledgeList) values (#{name}, #{knowledgeList})")
//    boolean insertKnowledgeArray(String account, String name, String knowledgeList);
//
//    @Update("update knowledgeArray_${account} set name = #{name}, knowledgeList = #{knowledgeList} where uuid = #{uuid}")
//    boolean updateKnowledgeArrayByUuid(String account, int uuid, String name, String knowledgeList);
//
//    @Update("update knowledgeArray_${account} set knowledgeList = #{knowledgeList} where name = #{name}")
//    boolean updateKnowledgeArrayByName(String account, String name, String knowledgeList);
//
//    @Delete("delete knowledgeArray_${account} where uuid = #{uuid}")
//    boolean deleteKnowledgeArrayByUuid(String account, int uuid);
//
//    @Update("create table if not exists knowledgeArray_${account}(" +
//            "uuid int(6) PRIMARY KEY AUTO_INCREMENT," +
//            "name varchar(64) not null," +
//            "knowledgeList varchar(1024) not null default \"[]\"" +
//            ")")
//    boolean createKnowledgeArray(String account);
//
//    @Update("delete from knowledgeArray_${account} where uuid != '0' ")
//    boolean deleteAllKnowledgeArray(String account);
}
