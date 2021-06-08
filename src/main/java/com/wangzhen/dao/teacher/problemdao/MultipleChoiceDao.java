package com.wangzhen.dao.teacher.problemdao;

import com.wangzhen.models.problem.MultipleChoice;
import com.wangzhen.models.problem.SingleChoice;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MultipleChoiceDao {
    @Select("select * from MultipleChoice_${account} order by sort desc")
    @Results(id = "problem_basic",value = {
            @Result(typeHandler = ArrayStringTypeHandler.class,column = "knowledgeList",property = "knowledgeList",javaType = List.class,jdbcType = JdbcType.VARCHAR),
            @Result(typeHandler = ArrayStringTypeHandler.class,column = "answer",property = "answer",javaType = List.class,jdbcType = JdbcType.VARCHAR)}
    )
    List<MultipleChoice> selectMyAllMultipleChoice(@Param("account") String account);//查找自己题库中的单选题
    @Select("select * from MultipleChoice_${college_uuid} order by sort desc")
    @ResultMap(value = "problem_basic")
    List<MultipleChoice> selectMyCollegeAllMultipleChoice(@Param("college_uuid") String college_uuid);//查找自己所在学院的单选题



    @Insert("<script>" +
            "insert into multiplechoice_${account}(" +
            "        uuid," +
            "        problem," +
            "        <trim suffixOverrides=\",\">" +
            "            <if test=\"choice_a != null\">choice_a,</if>" +
            "            <if test=\"choice_b != null\">choice_b,</if>" +
            "            <if test=\"choice_c != null\">choice_c,</if>" +
            "            <if test=\"choice_d != null\">choice_d,</if>" +
            "            <if test=\"choice_e != null\">choice_e,</if>" +
            "            <if test=\"choice_f != null\">choice_f,</if>" +
            "            <if test=\"choice_g != null\">choice_g,</if>" +
            "            <if test=\"choice_h != null\">choice_h,</if>" +
            "            <if test=\"answer != null\">answer,</if>" +
            "            <if test=\"analysis != null &amp;&amp; analysis != 'null'\">analysis,</if>" +
            "            <if test=\"difficultyVal != null &amp;&amp; difficultyVal != 'null'\">difficultyVal,</if>" +
            "            <if test=\"knowledgeList != null &amp;&amp; knowledgeList != 'null'\">knowledgeList,</if>" +
            "            <if test=\"videoPath != null &amp;&amp; videoPath != 'null'\">videoPath,</if>" +
            "        </trim>" +
            "            )"+
            "        values(" +
            "        <trim suffixOverrides=\",\">" +
            "            #{uuid}," +
            "            #{problem}," +
            "            <if test=\"choice_a != null\">#{choice_a},</if>" +
            "            <if test=\"choice_b != null\">#{choice_b},</if>" +
            "            <if test=\"choice_c != null\">#{choice_c},</if>" +
            "            <if test=\"choice_d != null\">#{choice_d},</if>" +
            "            <if test=\"choice_e != null\">#{choice_e},</if>" +
            "            <if test=\"choice_f != null\">#{choice_f},</if>" +
            "            <if test=\"choice_g != null\">#{choice_g},</if>" +
            "            <if test=\"choice_h != null\">#{choice_h},</if>" +
            "            <if test=\"answer != null\">#{answer},</if>" +
            "            <if test=\"analysis != null &amp;&amp; analysis != 'null'\">#{analysis},</if>" +
            "            <if test=\"difficultyVal != null &amp;&amp; difficultyVal != 'null'\">#{difficultyVal},</if>" +
            "            <if test=\"knowledgeList != null &amp;&amp; knowledgeList != 'null'\">#{knowledgeList},</if>" +
            "            <if test=\"videoPath != null &amp;&amp; videoPath != 'null'\">#{videoPath},</if>" +
            "        </trim>" +
            "        )" +
            "</script>")
    boolean insertMultipleChoice(@Param("uuid") String uuid, @Param("problem")String problem,
                               @Param("choice_a")String choice_a, @Param("choice_b")String choice_b,
                               @Param("choice_c")String choice_c, @Param("choice_d")String choice_d,
                               @Param("choice_e")String choice_e, @Param("choice_f")String choice_f,
                               @Param("choice_g")String choice_g, @Param("choice_h")String choice_h,
                               @Param("answer")String answer, @Param("analysis")String analysis,
                               @Param("difficultyVal")String difficultyVal,
                               @Param("knowledgeList")String knowledgeList,
                               @Param("videoPath")String videoPath,
                               @Param("account")String account);

    @Update("create table if not exists MultipleChoice_${account} (" +
            "uuid varchar(50) PRIMARY KEY," +
            "problem varchar(255) not null," +
            "choice_a varchar(255) DEFAULT null," +
            "choice_b varchar(255) DEFAULT null," +
            "choice_c varchar(255) DEFAULT null," +
            "choice_d varchar(255) DEFAULT null," +
            "choice_e varchar(255) DEFAULT null," +
            "choice_f varchar(255) DEFAULT null," +
            "choice_g varchar(255) DEFAULT null," +
            "choice_h varchar(255) DEFAULT null," +
            "answer varchar(255) DEFAULT null," +
            "analysis varchar(1024) DEFAULT null," +
            "videopath varchar(255) DEFAULT null," +
            "usenum int DEFAULT 0," +
            "allnum int DEFAULT 0," +
            "correctnum int DEFAULT 0," +
            "correctrate float DEFAULT 0," +
            "difficultyval float DEFAULT 0," +
            "knowledgelist varchar(255) DEFAULT null," +
            "sort timestamp(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)"+
            ")")
    boolean createMultipleChoiceDataTable(@Param("account") String account);

    @Update("update MultipleChoice_${account} set problem = #{problem}, choice_a = #{choice_a}, choice_b = #{choice_b}," +
            " choice_c = #{choice_c}, choice_d = #{choice_d}, choice_e = #{choice_e}, choice_f = #{choice_f}," +
            " choice_g = #{choice_g}, choice_h = #{choice_h}, answer = #{answer}, difficultyVal = #{difficultyVal}," +
            " analysis = #{analysis}, knowledgeList = #{knowledgeList} where uuid = #{uuid}")
    boolean updateMultipleChoiceInfoByUuid(@Param("account") String account,@Param("problem") String problem,
                                         @Param("choice_a") String choice_a, @Param("choice_b")String choice_b,
                                         @Param("choice_c")String choice_c, @Param("choice_d")String choice_d,
                                         @Param("choice_e")String choice_e, @Param("choice_f")String choice_f,
                                         @Param("choice_g")String choice_g, @Param("choice_h")String choice_h,
                                         @Param("answer")String answer, @Param("difficultyVal")float difficultyVal,
                                         @Param("analysis")String analysis, @Param("knowledgeList")String knowledgeList,
                                         @Param("uuid")String uuid);


    @Delete("delete * from MultipleChoice_${account} where uuid = #{uuid}")
    boolean deleteMultipleChoiceByUuid(@Param("account") String account,@Param("uuid") String uuid);


    @Update("update MultipleChoice_${account} set problem = #{problem}, choice_a = #{choice_a}, choice_b = #{choice_b}," +
            " choice_c = #{choice_c}, choice_d = #{choice_d}, choice_e = #{choice_e}, choice_f = #{choice_f}," +
            " choice_g = #{choice_g}, choice_h = #{choice_h}, answer = #{answer}, difficultyVal = #{difficultyVal}," +
            " analysis = #{analysis}, knowledgeList = #{knowledgeList}, videoPath = #{videoPath} where uuid = #{uuid}")
    boolean updateMultipleChoiceInfoWithVideoByUuid(@Param("account")String account, @Param("problem")String problem,
                                                  @Param("choice_a")String choice_a,@Param("choice_b") String choice_b,
                                                  @Param("choice_c")String choice_c,@Param("choice_d")String choice_d,
                                                  @Param("choice_e")String choice_e, @Param("choice_f")String choice_f,
                                                  @Param("choice_g")String choice_g, @Param("choice_h")String choice_h,
                                                  @Param("answer")String answer, @Param("difficultyVal")Float difficultyVal,
                                                  @Param("analysis")String analysis, @Param("knowledgeList")String knowledgeList,
                                                  @Param("videoPath")String videoPath, @Param("uuid")String uuid);

    @Update("update multipleChoice_${account} set useNum = useNum + 1 where uuid = #{uuid}")
    void addUseNum(@Param("account") String account, @Param("uuid") String uuid);

    @Select("select * from multipleChoice_${account} ${condition} order by rand() limit 1")
    MultipleChoice selectMultipleChoiceUseCondition(@Param("account") String account, @Param("condition") String condition);

    @Select("select * from multipleChoice_${account} order by rand() limit #{problemNum}")
    List<MultipleChoice> selectProblemWithNum(@Param("account")String account, @Param("problemNum")int problemNum);

    @Select("select * from multipleChoice_${account} where uuid = #{uuid}")
    MultipleChoice selectProblemByUuid(@Param("account") String account, @Param("uuid") String uuid);
}
