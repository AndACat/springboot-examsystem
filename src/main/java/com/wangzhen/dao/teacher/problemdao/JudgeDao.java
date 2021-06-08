package com.wangzhen.dao.teacher.problemdao;
import com.wangzhen.models.problem.Fill;
import com.wangzhen.models.problem.Judge;
import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface JudgeDao {
    @Select("select * from judge_${account} order by sort desc")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    List<Judge> selectMyAllJudge(@Param("account") String account);
    @Insert("<script>" +
            "insert into judge_${account}(" +
            "        uuid," +
            "        problem," +
            "        <trim suffixOverrides=','>" +
            "            <if test='judge.answer != null'>answer,</if>" +
            "            <if test='judge.videoPath != null'>videoPath,</if>" +
            "            <if test='judge.analysis != null'>analysis,</if>" +
            "            <if test='judge.difficultyVal != null'>difficultyVal,</if>" +
            "            <if test='judge.knowledgeList != null'>knowledgeList,</if>" +
            "        </trim>" +
            "        )values(" +
            "        <trim suffixOverrides=','>" +
            "            #{judge.uuid}," +
            "            #{judge.problem}," +
            "            <if test='judge.answer != null'>#{judge.answer},</if>" +
            "            <if test='judge.videoPath != null'>#{judge.videoPath},</if>" +
            "            <if test='judge.analysis != null'>#{judge.analysis},</if>" +
            "            <if test='judge.difficultyVal != null'>#{judge.difficultyVal},</if>" +
            "            <if test='judge.knowledgeList != null'>#{judge.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        )" +
            "</script>")
    Boolean insertJudge(@Param("account")String account,@Param("judge")Judge judge);
    
    @Update("create table if not exists judge_${account}(" +
            "uuid varchar(50) PRIMARY KEY," +
            "problem varchar(255) not null," +
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
    Boolean createJudgeTable(@Param("account")String account);

    @Delete("delete judge_${account} where uuid = #{uuid}")
    Boolean deleteJudgeByUuid(@Param("account")String account, @Param("uuid")String uuid);

    @Update("<script>" +
            "update judge_${account} set" +
            "        <trim suffixOverrides=','>" +
            "            <if test='judge.problem != null'>problem = #{judge.problem},</if>" +
            "            <if test='judge.answer != null'>answer = #{judge.answer},</if>" +
            "            <if test='judge.videoPath != null'>videoPath = #{judge.videoPath},</if>" +
            "            <if test='judge.analysis != null'>analysis = #{judge.analysis},</if>" +
            "            <if test='judge.useNum != null'>useNum = #{judge.useNum},</if>" +
            "            <if test='judge.allNum != null'>allNum = #{judge.allNum},</if>" +
            "            <if test='judge.correctNum != null'>correctNum = #{judge.correctNum},</if>" +
            "            <if test='judge.correctRate != null'>correctRate = #{judge.correctRate},</if>" +
            "            <if test='judge.difficultyVal != null'>difficultyVal = #{judge.difficultyVal},</if>" +
            "            <if test='judge.knowledgeList != null'>knowledgeList = #{judge.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        where uuid = #{judge.uuid}" +
            "</script>")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    Boolean updateJudgeInfo(@Param("account")String account,@Param("judge")Judge judge);

    @Update("update judge_${account} set useNum = useNum + 1 where uuid = #{uuid}")
    void addUseNum(@Param("account") String account, @Param("uuid") String uuid);

    @Select("select * from judge_${account} ${condition} order by rand() limit 1")
    Judge selectJudgeUseCondition(@Param("account") String account, @Param("condition") String condition);

    @Select("select * from judge_${account} order by rand() limit #{problemNum}")
    List<Judge> selectProblemWithNum(@Param("account")String account, @Param("problemNum")int problemNum);

    @Select("select * from judge_${account} where uuid = #{uuid}")
    Judge selectProblemByUuid(@Param("account") String account, @Param("uuid") String uuid);
}
