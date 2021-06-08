package com.wangzhen.dao.teacher;

import com.wangzhen.models.Paper;
import com.wangzhen.utils.typehandler.PaperInfoTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/8 18:00
 */
@Mapper
@Repository
public interface PaperDao {
    @Insert("insert into paper (uuid,paperName,sort,openFaceIdentity,course_uuid,teacher_uuid,begin,end,during,visible,totalScore,paperStrategy_uuid,paperInfo) values" +
            "(#{paper.uuid},#{paper.paperName},#{paper.sort},#{paper.openFaceIdentity},#{paper.course_uuid},#{paper.teacher_uuid},#{paper.begin},#{paper.end},#{paper.during},#{paper.visible},#{paper.totalScore},#{paper.paperStrategy_uuid},#{paper.stringPaperInfo})")
    boolean insertPaper(@Param("paper") Paper paper);

    @Delete("delete from paper where uuid = #{uuid}")
    void deletePaperByUuid(@Param("uuid") String uuid);

    @Results(
            @Result(column = "paperInfo",property = "paperInfo",javaType = List.class, jdbcType = JdbcType.VARCHAR,typeHandler = PaperInfoTypeHandler.class)
    )
    @Select("select * from paper where teacher_uuid = #{teacher_uuid} order by begin asc")
    List<Paper> selectMyAllPaper(@Param("teacher_uuid") String teacher_uuid);

    @Results(
            @Result(column = "paperInfo",property = "paperInfo",javaType = List.class, jdbcType = JdbcType.VARCHAR,typeHandler = PaperInfoTypeHandler.class)
    )
    @Select("select * from paper where uuid = #{uuid}")
    Paper selectPaperByPaper_uuid(@Param("uuid") String uuid);

    @Select("select * from paper where teacher_uuid = #{teacher_uuid} order by begin asc")
    List<Paper> selectPaperByTeacher_uuid(@Param("teacher_uuid") String teacher_uuid);


    @Update("update paper set openFaceIdentity = #{openFaceIdentity}, sort = #{sort}, visible = #{visible}, begin = #{begin},end = #{end}, during = #{during} where uuid = #{uuid}")
    void updatePaperInfo(@Param("sort") boolean sort, @Param("openFaceIdentity") boolean openFaceIdentity,@Param("visible") boolean visible, @Param("begin") Timestamp begin, @Param("end") Timestamp end, @Param("during")int during, @Param("uuid")String uuid);

    @Results(
            @Result(column = "paperInfo",property = "paperInfo",javaType = List.class, jdbcType = JdbcType.VARCHAR,typeHandler = PaperInfoTypeHandler.class)
    )
    @Select("select * from paper")
    public List<Paper> selectAllPaper();

    @Results(
            @Result(column = "paperInfo",property = "paperInfo",javaType = List.class, jdbcType = JdbcType.VARCHAR,typeHandler = PaperInfoTypeHandler.class)
    )
    @Select("select * from paper where paperName = #{paperName} and teacher_uuid = #{teacher_uuid}")
    Paper selectPaperByPaperName(@Param("paperName") String paperName,@Param("teacher_uuid")String teacher_uuid);

    @Select("<script>" +
            "select uuid,paperName,begin,end,during,course_uuid from paper " +
            "where visible = '1' and " +
            "(" +
            "<trim suffixOverrides='or'><foreach collection=\"course_uuid_list\" item=\"course_uuid\">" +
            "course_uuid = #{course_uuid} or" +
            "        </foreach></trim>" +
            ")" +
            " order by begin desc</script>")
    List<Paper> selectSimplePaperForStudentToDisplay(@Param("course_uuid_list") List<String> course_uuid_list);

    @Results(
            @Result(column = "paperInfo",property = "paperInfo",javaType = List.class, jdbcType = JdbcType.VARCHAR,typeHandler = PaperInfoTypeHandler.class)
    )
    @Select("select *  from paper where visible = '1' and uuid = #{uuid}")
    Paper selectPaperForStudentToExam(@Param("uuid") String uuid);

    @Select("select a.uuid,a.paperName from paper a left join studentpaperanswer b on (a.uuid = b.paper_uuid) where teacher_uuid = #{teacher_uuid} and b.complete = 1")
    List<Paper> getScoreOfMyAllPaper(@Param("teacher_uuid") String teacher_uuid);

    @Select("select count(*) from paper where paperStrategy_uuid = #{uuid}")
    int selectCountOfPaperByPaperStrategy_uuid(@Param("uuid") String uuid);
}
