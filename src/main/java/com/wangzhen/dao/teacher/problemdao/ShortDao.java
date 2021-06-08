package com.wangzhen.dao.teacher.problemdao;
import com.wangzhen.models.problem.Judge;
import com.wangzhen.models.problem.Program;
import com.wangzhen.models.problem.Short;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ShortDao {
    @Insert("<script>" +
            "insert into short_${account}(" +
            "        uuid," +
            "        problem," +
            "        <trim suffixOverrides=','>" +
            "            <if test='short.answer != null'>answer,</if>" +
            "            <if test='short.videoPath != null'>videoPath,</if>" +
            "            <if test='short.analysis != null'>analysis,</if>" +
            "            <if test='short.difficultyVal != null'>difficultyVal,</if>" +
            "            <if test='short.knowledgeList != null'>knowledgeList,</if>" +
            "        </trim>" +
            "        )values(" +
            "        <trim suffixOverrides=','>" +
            "            #{short.uuid}," +
            "            #{short.problem}," +
            "            <if test='short.answer != null'>#{short.answer},</if>" +
            "            <if test='short.videoPath != null'>#{short.videoPath},</if>" +
            "            <if test='short.analysis != null'>#{short.analysis},</if>" +
            "            <if test='short.difficultyVal != null'>#{short.difficultyVal},</if>" +
            "            <if test='short.knowledgeList != null'>#{short.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        )" +
            "</script>")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    void insertShort(@Param("account") String account, @Param("short") Short sho);
    
    @Delete("delete from short_${account} where uuid = #{uuid}")
    void deleteShortByUuid(String account, String uuid);
    
    @Select("select * from short_${account} order by sort desc")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    List<Short> selectMyAllShort(@Param("account") String account);
    
    @Update("<script>" +
            "update short_${account} set" +
            "        <trim suffixOverrides=','>" +
            "            <if test='short.problem != null'>problem = #{short.problem},</if>" +
            "            <if test='short.answer != null'>answer = #{short.answer},</if>" +
            "            <if test='short.videoPath != null'>videoPath = #{short.videoPath},</if>" +
            "            <if test='short.analysis != null'>analysis = #{short.analysis},</if>" +
            "            <if test='short.useNum != null'>useNum = #{short.useNum},</if>" +
            "            <if test='short.allNum != null'>allNum = #{short.allNum},</if>" +
            "            <if test='short.correctNum != null'>correctNum = #{short.correctNum},</if>" +
            "            <if test='short.correctRate != null'>correctRate = #{short.correctRate},</if>" +
            "            <if test='short.difficultyVal != null'>difficultyVal = #{short.difficultyVal},</if>" +
            "            <if test='short.knowledgeList != null'>knowledgeList = #{short.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        where uuid = #{short.uuid}" +
            "</script>")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    void updateShortInfo(@Param("account") String account, @Param("short") Short sho);
    
    @Update("create table if not exists short_${account}(" +
            "uuid varchar(50) PRIMARY KEY," +
            "problem varchar(1024) not null," +
            "answer varchar(255) default null," +
            "videoPath varchar(255) default null," +
            "useNum int default 0," +
            "allNum int default 0," +
            "correctNum int default 0," +
            "correctRate FLOAT default 0," +
            "analysis varchar(1024) default null," +
            "difficultyVal  varchar(255) default null," +
            "knowledgeList varchar(255) default null," +
            "sort TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
            ")")
    void createShortTable(@Param("account") String account);

    @Update("update short_${account} set useNum = useNum + 1 where uuid = #{uuid}")
    void addUseNum(@Param("account") String account, @Param("uuid") String uuid);

    @Select("select * from short_${account} ${condition} order by rand() limit 1")
    Short selectShortUseCondition(@Param("account") String account, @Param("condition") String condition);

    @Select("select * from short_${account} order by rand() limit #{problemNum}")
    List<Short> selectProblemWithNum(@Param("account")String account, @Param("problemNum")int problemNum);

    @Select("select * from program_${account} where uuid = #{uuid}")
    Short selectProblemByUuid(@Param("account") String account, @Param("uuid") String uuid);
}
