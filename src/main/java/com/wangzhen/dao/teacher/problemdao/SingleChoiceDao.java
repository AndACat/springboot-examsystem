package com.wangzhen.dao.teacher.problemdao;
import com.wangzhen.models.problem.SingleChoice;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 *  每个教师拥有自己的题库（单选题、多选题、填空题...）
 *  教师可将自己的私有题库上传到学院共享题库，也可从学院共享题库中选取自己喜欢的题目添加到自己的题库中去
 *      每个教师都拥有自己的题库是：考虑到各位老师的专业不同，题库不能放在一起，必须单独放（例如：计算机专业题库不能和化学题库放在一起）
 *  而设立学院题库的原因是：学院之间的专业相差不大，有很多专业联系，因此可以设立
 */
@Repository
@Mapper
public interface SingleChoiceDao {
    @Select("select * from singlechoice_${account} order by sort desc")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    List<SingleChoice> selectMyAllSingleChoice(@Param("account") String account);//查找自己题库中的单选题

    @Select("select * from singlechoice_${college_uuid} order by sort desc")
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    List<SingleChoice> selectMyCollegeAllSingleChoice(@Param("college_uuid") String college_uuid);//查找自己所在学院的单选题

    @Insert("<script>" +
            "insert into singlechoice_${account}(" +
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
    @Results({
            @Result(typeHandler = ArrayStringTypeHandler.class, javaType = List.class, jdbcType = JdbcType.VARCHAR, column = "knowledgeList", property = "knowledgeList")
    })
    boolean insertSingleChoice(@Param("uuid") String uuid, @Param("problem")String problem,
                               @Param("choice_a")String choice_a, @Param("choice_b")String choice_b,
                               @Param("choice_c")String choice_c, @Param("choice_d")String choice_d,
                               @Param("choice_e")String choice_e, @Param("choice_f")String choice_f,
                               @Param("choice_g")String choice_g, @Param("choice_h")String choice_h,
                               @Param("answer")String answer, @Param("analysis")String analysis,
                               @Param("difficultyVal")String difficultyVal,
                               @Param("knowledgeList")String knowledgeList,
                               @Param("videoPath")String videoPath,
                               @Param("account")String account);

    @Update("create table if not exists singlechoice_${account} (" +
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
    boolean createSingleChoiceDataTable(@Param("account") String account);

    @Update("update singleChoice_${account} set problem = #{problem}, choice_a = #{choice_a}, choice_b = #{choice_b}," +
            " choice_c = #{choice_c}, choice_d = #{choice_d}, choice_e = #{choice_e}, choice_f = #{choice_f}," +
            " choice_g = #{choice_g}, choice_h = #{choice_h}, answer = #{answer}, difficultyVal = #{difficultyVal}," +
            " analysis = #{analysis}, knowledgeList = #{knowledgeList} where uuid = #{uuid}")
    boolean updateSingleChoiceInfoByUuid(@Param("account") String account,@Param("problem") String problem,
                                         @Param("choice_a") String choice_a, @Param("choice_b")String choice_b,
                                         @Param("choice_c")String choice_c, @Param("choice_d")String choice_d,
                                         @Param("choice_e")String choice_e, @Param("choice_f")String choice_f,
                                         @Param("choice_g")String choice_g, @Param("choice_h")String choice_h,
                                         @Param("answer")String answer, @Param("difficultyVal")float difficultyVal,
                                         @Param("analysis")String analysis, @Param("knowledgeList")String knowledgeList,
                                         @Param("uuid")String uuid);


    @Delete("delete * from singlechoice_${account} where uuid = #{uuid}")
    boolean deleteSinglechoiceByUuid(@Param("account") String account,@Param("uuid") String uuid);


    @Update("update singleChoice_${account} set problem = #{problem}, choice_a = #{choice_a}, choice_b = #{choice_b}," +
            " choice_c = #{choice_c}, choice_d = #{choice_d}, choice_e = #{choice_e}, choice_f = #{choice_f}," +
            " choice_g = #{choice_g}, choice_h = #{choice_h}, answer = #{answer}, difficultyVal = #{difficultyVal}," +
            " analysis = #{analysis}, knowledgeList = #{knowledgeList}, videoPath = #{videoPath} where uuid = #{uuid}")
    boolean updateSingleChoiceInfoWithVideoByUuid(@Param("account")String account, @Param("problem")String problem,
                                                  @Param("choice_a")String choice_a,@Param("choice_b") String choice_b,
                                                  @Param("choice_c")String choice_c,@Param("choice_d")String choice_d,
                                                  @Param("choice_e")String choice_e, @Param("choice_f")String choice_f,
                                                  @Param("choice_g")String choice_g, @Param("choice_h")String choice_h,
                                                  @Param("answer")String answer, @Param("difficultyVal")Float difficultyVal,
                                                  @Param("analysis")String analysis, @Param("knowledgeList")String knowledgeList,
                                                  @Param("videoPath")String videoPath, @Param("uuid")String uuid);

    @Update("update singleChoice_${account} set useNum = useNum + 1 where uuid = #{uuid}")
    void addUseNum(@Param("account") String account,@Param("uuid") String uuid);

    @Select("select * from singleChoice_${account} ${condition} order by rand() limit 1")
    SingleChoice selectSingleChoiceUseCondition(@Param("account") String account,@Param("condition") String condition);

    @Select("select * from singleChoice_${account} order by rand() limit #{problemNum}")
    List<SingleChoice> selectProblemWithNum(@Param("account")String account, @Param("problemNum")int problemNum);

    @Select("select * from singleChoice_${account} where uuid = #{uuid}")
    SingleChoice selectProblemByUuid(@Param("account") String account, @Param("uuid") String uuid);
}
