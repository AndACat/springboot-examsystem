package com.wangzhen.dao.teacher.problemdao;
import com.wangzhen.models.problem.Fill;
import com.wangzhen.models.problem.Judge;
import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FillDao {
    @Insert("<script>" +
            "insert into fill_${account}(" +
            "        uuid," +
            "        problem," +
            "        <trim suffixOverrides=','>" +
            "            <if test='fill.answer != null'>answer,</if>" +
            "            <if test='fill.videoPath != null'>videoPath,</if>" +
            "            <if test='fill.analysis != null'>analysis,</if>" +
            "            <if test='fill.difficultyVal != null'>difficultyVal,</if>" +
            "            <if test='fill.knowledgeList != null'>knowledgeList,</if>" +
            "        </trim>" +
            "        )values(" +
            "        <trim suffixOverrides=','>" +
            "            #{fill.uuid}," +
            "            #{fill.problem}," +
            "            <if test='fill.answer != null'>#{fill.answer},</if>" +
            "            <if test='fill.videoPath != null'>#{fill.videoPath},</if>" +
            "            <if test='fill.analysis != null'>#{fill.analysis},</if>" +
            "            <if test='fill.difficultyVal != null'>#{fill.difficultyVal},</if>" +
            "            <if test='fill.knowledgeList != null'>#{fill.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        )" +
            "</script>")
    @Results({
                    @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "answer", property = "answer"),
                    @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
            }
    )
    void insertFill(@Param("account")String account,@Param("fill") Fill fill);
    
    @Delete("delete from fill_${account} where uuid = #{uuid}")
    void deleteFillByUuid(String account,String uuid);
    
    @Select("select * from fill_${account} order by sort desc")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "answer", property = "answer"),
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    }
    )
    List<Fill> selectMyAllFill(@Param("account") String account);
    
    @Update("<script>" +
            "update fill_${account} set" +
            "        <trim suffixOverrides=','>" +
            "            <if test='fill.problem != null'>problem = #{fill.problem},</if>" +
            "            <if test='fill.answer != null'>answer = #{fill.stringAnswer},</if>" +
            "            <if test='fill.videoPath != null'>videoPath = #{fill.videoPath},</if>" +
            "            <if test='fill.analysis != null'>analysis = #{fill.analysis},</if>" +
            "            <if test='fill.useNum != null'>useNum = #{fill.useNum},</if>" +
            "            <if test='fill.allNum != null'>allNum = #{fill.allNum},</if>" +
            "            <if test='fill.correctNum != null'>correctNum = #{fill.correctNum},</if>" +
            "            <if test='fill.correctRate != null'>correctRate = #{fill.correctRate},</if>" +
            "            <if test='fill.difficultyVal != null'>difficultyVal = #{fill.difficultyVal},</if>" +
            "            <if test='fill.knowledgeList != null'>knowledgeList = #{fill.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        where uuid = #{fill.uuid}" +
            "</script>")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "answer", property = "answer"),
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    void updateFillInfo(@Param("account") String account,@Param("fill") Fill fill);
    
    @Update("create table if not exists fill_${account}(" +
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
    void createFillTable(@Param("account") String account);

    @Update("update fill_${account} set useNum = useNum + 1 where uuid = #{uuid}")
    void addUseNum(@Param("account") String account,@Param("uuid") String uuid);

    @Select("select * from fill_${account} ${condition} order by rand() limit 1")
    Fill selectFillUseCondition(@Param("account") String account, @Param("condition") String condition);

    @Select("select * from fill_${account} order by rand() limit #{problemNum}")
    List<Fill> selectProblemWithNum(@Param("account")String account, @Param("problemNum")int problemNum);

    @Select("select * from fill_${account} where uuid = #{uuid}")
    Fill selectProblemByUuid(@Param("account") String account, @Param("uuid") String uuid);
}
