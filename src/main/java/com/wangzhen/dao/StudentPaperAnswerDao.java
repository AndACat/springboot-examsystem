package com.wangzhen.dao;

import com.wangzhen.models.StudentPaperAnswer;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import com.wangzhen.utils.typehandler.MapIntegerObjectTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.sql.JDBCType;
import java.util.LinkedHashMap;
import java.util.List;

@Mapper
@Repository
public interface StudentPaperAnswerDao {
    @Select("select * from studentPaperAnswer where paper_uuid = #{paper_uuid} order by sort asc")
    @Results(id = "ioList",value =
            {
                    @Result(jdbcType = JdbcType.VARCHAR,javaType = List.class,typeHandler = ArrayStringTypeHandler.class,property = "zbList",column = "zbList"),
                    @Result(jdbcType = JdbcType.VARCHAR,javaType =LinkedHashMap.class,typeHandler = MapIntegerObjectTypeHandler.class,property = "answers",column = "answers")
            }
    )
    List<StudentPaperAnswer> selectAllStudentPaperAnswerOfSinglePaper(@Param("paper_uuid") String paper_uuid);

    @Select("select * from studentPaperAnswer where paper_uuid = #{paper_uuid} and student_uuid = #{student_uuid}")
    @ResultMap("ioList")
    StudentPaperAnswer selectStudentPaperAnswer(@Param("paper_uuid") String paper_uuid,@Param("student_uuid") String student_uuid);

    @Update("<script>" +
            "insert into studentPaperAnswer" +
            "        (uuid,paper_uuid,student_uuid," +
            "        <trim suffixOverrides=','>" +
            "            <if test='singleChoiceScore != -1f'>singleChoiceScore,</if>" +
            "            <if test='multipleChoiceScore != -1f'>multipleChoiceScore,</if>" +
            "            <if test='fillScore != -1f'>fillScore ,</if>" +
            "            <if test='judgeScore != -1f'>judgeScore,</if>" +
            "            <if test='shortScore != -1f'>shortScore,</if>" +
            "            <if test='programScore != -1f'>programScore ,</if>" +
            "            <if test='totalScore != -1f'>totalScore,</if>" +
            "            <if test='paperTotalScore != -1f'>paperTotalScore,</if>" +
            "            <if test='answers != null'>answers,</if>" +
            "            <if test='pass != null'>pass ,</if>" +
            "            <if test='complete != null'>complete ,</if>" +
            "        </trim>" +
            "        )values(" +
            "        #{uuid},#{paper_uuid},#{student_uuid}," +
            "        <trim suffixOverrides=','>" +
            "            <if test='singleChoiceScore != -1f'> #{singleChoiceScore},</if>" +
            "            <if test='multipleChoiceScore != -1f'> #{multipleChoiceScore},</if>" +
            "            <if test='fillScore != -1f'> #{fillScore},</if>" +
            "            <if test='judgeScore != -1f'> #{judgeScore},</if>" +
            "            <if test='shortScore != -1f'> #{shortScore},</if>" +
            "            <if test='programScore != -1f'> #{programScore},</if>" +
            "            <if test='totalScore != -1f'> #{totalScore},</if>" +
            "            <if test='paperTotalScore != -1f'> #{paperTotalScore},</if>" +
            "            <if test='answers != null'> #{answers},</if>" +
            "            <if test='pass != null'> #{pass},</if>" +
            "            <if test='complete != null'>#{complete},</if>" +
            "        </trim>" +
            "        )" +
            "</script>")
    Boolean insertStudentPaperAnswer(
            @Param("uuid") String uuid,
            @Param("paper_uuid")String paper_uuid,
            @Param("student_uuid")String student_uuid,
            @Param("singleChoiceScore") Float singleChoiceScore,
            @Param("multipleChoiceScore")Float multipleChoiceScore,
            @Param("fillScore")Float fillScore,
            @Param("shortScore")Float shortScore,
            @Param("judgeScore")Float judgeScore,
            @Param("programScore")Float programScore,
            @Param("totalScore")Float totalScore,
            @Param("paperTotalScore")Float paperTotalScore,
            @Param("answers")String answers,
            @Param("pass")Boolean pass,
            @Param("complete")Boolean complete
    );

    @ResultMap("ioList")
    @Select("select * from studentPaperAnswer where paper_uuid = #{paper_uuid} and student_uuid = #{student_uuid}")
    StudentPaperAnswer selectStudentPaperAnswerByPaper_uuidAndStudent_uuid(@Param("paper_uuid") String paper_uuid,@Param("student_uuid") String student_uuid);


    @Update("update studentPaperAnswer set zbList = #{zbList} where student_uuid = #{student_uuid} and paper_uuid = #{paper_uuid}")
    void addZbList(@Param("student_uuid") String student_uuid,@Param("paper_uuid") String paper_uuid, @Param("zbList") String zbList);

    @ResultMap("ioList")
    @Select("select * from StudentPaperAnswer where (${paper_uuid_string_list}) and complete = '0'")
    List<StudentPaperAnswer> selectMarkingPaper(String paper_uuid_string_list);

    @Update("update studentPaperAnswer set fillScore = #{fillTotalScore}, shortScore = #{shortTotalScore} where paper_uuid = #{paper_uuid} and student_uuid = #{student_uuid};")
    void submitScoreOfMark(@Param("paper_uuid") String paper_uuid, @Param("student_uuid")String student_uuid, @Param("fillTotalScore")Float fillTotalScore, @Param("shortTotalScore")Float shortTotalScore);

    @Update("update studentPaperAnswer set totalScore = fillscore + shortScore + judgeScore + singleChoiceScore + multipleChoiceScore + programScore where paper_uuid = #{paper_uuid} and student_uuid = #{student_uuid};")
    void autoChangeTotalScore(@Param("paper_uuid") String paper_uuid, @Param("student_uuid")String student_uuid);

    @Update("update studentPaperAnswer set pass = (totalScore > paperTotalScore * 0.6) , complete = 1  where paper_uuid = #{paper_uuid} and student_uuid = #{student_uuid}")
    void autoChangePass(@Param("paper_uuid") String paper_uuid, @Param("student_uuid")String student_uuid);

    @Select("select * from studentPaperAnswer where student_uuid = #{student_uuid} and complete = 1  order by sort asc")
    List<StudentPaperAnswer> getMyScore(@Param("student_uuid") String student_uuid);

}
