package com.wangzhen.dao.teacher.problemdao;

import com.wangzhen.models.problem.Fill;
import com.wangzhen.models.problem.Judge;
import com.wangzhen.models.problem.Program;
import com.wangzhen.models.problem.Short;
import com.wangzhen.utils.typehandler.ArrayOptionTypeHandler;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper@Repository
public interface ProgramDao {
    @Insert("<script>" +
            "insert into program_${account}(" +
            "        uuid," +
            "        problem," +
            "        <trim suffixOverrides=','>" +
            "            <if test='program.ioList != null'>ioList,</if>" +
            "            <if test='program.videoPath != null'>videoPath,</if>" +
            "            <if test='program.analysis != null'>analysis,</if>" +
            "            <if test='program.difficultyVal != null'>difficultyVal,</if>" +
            "            <if test='program.knowledgeList != null'>knowledgeList,</if>" +
            "        </trim>" +
            "        )values(" +
            "        <trim suffixOverrides=','>" +
            "            #{program.uuid}," +
            "            #{program.problem}," +
            "            <if test='program.ioList != null'>#{program.ioList},</if>" +
            "            <if test='program.videoPath != null'>#{program.videoPath},</if>" +
            "            <if test='program.analysis != null'>#{program.analysis},</if>" +
            "            <if test='program.difficultyVal != null'>#{program.difficultyVal},</if>" +
            "            <if test='program.knowledgeList != null'>#{program.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        )" +
            "</script>")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList"),
            @Result(typeHandler = ArrayOptionTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "ioList", property = "ioList")
    })
    void insertProgram(@Param("account") String account,@Param("program")Program program);

    @Delete("delete from program_${account} where uuid = #{uuid}")
    void deleteProgramByUuid(@Param("account") String account,@Param("uuid")String uuid);

    @Select("select * from program_${account} order by sort desc")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList"),
            @Result(typeHandler = ArrayOptionTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "ioList", property = "ioList")
    })
    List<Program> selectMyAllProgram(@Param("account") String account);

    @Update("<script>" +
            "update program_${account} set" +
            "        <trim suffixOverrides=','>" +
            "            <if test='program.problem != null'>problem = #{program.problem},</if>" +
            "            <if test='program.ioList != null'>ioList = #{program.stringIoList},</if>" +
            "            <if test='program.videoPath != null'>videoPath = #{program.videoPath},</if>" +
            "            <if test='program.analysis != null'>analysis = #{program.analysis},</if>" +
            "            <if test='program.useNum != null'>useNum = #{program.useNum},</if>" +
            "            <if test='program.allNum != null'>allNum = #{program.allNum},</if>" +
            "            <if test='program.correctNum != null'>correctNum = #{program.correctNum},</if>" +
            "            <if test='program.correctRate != null'>correctRate = #{program.correctRate},</if>" +
            "            <if test='program.difficultyVal != null'>difficultyVal = #{program.difficultyVal},</if>" +
            "            <if test='program.knowledgeList != null'>knowledgeList = #{program.stringKnowledgeList},</if>" +
            "        </trim>" +
            "        where uuid = #{program.uuid}" +
            "</script>")
    void updateProgramInfo(@Param("account") String account,@Param("program")Program program);

    @Update("create table if not exists program_${account}(" +
            "uuid varchar(50) PRIMARY KEY," +
            "problem varchar(1024) not null," +
            "type varchar(255) default null," +
            "ioList varchar(255) default null," +
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
    void createProgramTable(@Param("account") String account);

    @Update("update program_${account} set useNum = useNum + 1 where uuid = #{uuid}")
    void addUseNum(@Param("account") String account, @Param("uuid") String uuid);

    @Select("select * from program_${account} ${condition} order by rand() limit 1")
    Program selectProgramUseCondition(@Param("account") String account, @Param("condition") String condition);

    @Select("select * from program_${account} order by rand() limit #{problemNum}")
    List<Program> selectProblemWithNum(@Param("account")String account, @Param("problemNum")int problemNum);

    @Select("select * from program_${account} where uuid = #{uuid}")
    Program selectProblemByUuid(@Param("account") String account, @Param("uuid") String uuid);
}
