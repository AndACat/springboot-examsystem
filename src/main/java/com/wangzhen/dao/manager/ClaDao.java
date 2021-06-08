package com.wangzhen.dao.manager;

import org.apache.ibatis.annotations.*;
import com.wangzhen.models.Cla;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author wangzhen
 * @Description 班级类的数据库操作
 * @CreateDate 2020/1/9 18:00
 */
@Mapper
@Repository
public interface ClaDao {
    @Select("select * from cla where uuid = #{uuid}")
    Cla selectClaByUuid(@Param("uuid") String uuid);

    @Select("select * from cla order by sort desc")
    List<Cla> selectAllCla();

    @Delete("delete from cla where uuid = #{uuid}")
    void deleteClaByUuid(@Param("uuid")String uuid);





//    "<script>" +
//            "update cla set " +
//            "        <trim suffixOverrides=','>" +
////            "            <if test='cla.college != null'>college = #{cla.college},</if>" +
////            "            <if test='cla.profession != null'>profession = #{cla.profession},</if>" +
////            "            <if test='cla.className != null'>className = #{cla.className},</if>" +
//            "            <if test='cla.student_uuid_list != student_uuid_list'>student_uuid_list = #{cla.student_uuid_list},</if>" +
////            "            <if test='cla.course_uuid_list != null'>course_uuid_list = #{cla.course_uuid_list},</if>" +
//            "        </trim>" +
//            "        where uuid = #{cla.uuid}" +
//            "</script>"
    @Update("update cla set student_uuid_list = #{cla.student_uuid_list} where uuid = #{cla.uuid}")
    void updateClaInfoByUuid(@Param("cla") Cla cla);


//    "<script>" +
//            "insert into cla (" +
//            "           <trim suffixOverrides=','>" +
//            "           uuid,college,profession,claName," +
//            "           <if test='cla.student_uuid_list != null'>student_uuid_list,</if>" +
//            "           <if test='cla.course_uuid_list != null'>course_uuid_list,</if>" +
//            "           </trim>" +
//            "           )values(" +
//            "           <trim suffixOverrides=','>" +
//            "           #{cla.uuid}, #{cla.college}, #{cla.profession}, #{cla.claName}," +
//            "           <if test='cla.student_uuid_list != null'>#{cla.student_uuid_list},</if>" +
//            "           <if test='cla.course_uuid_list != null'>#{cla.course_uuid_list},</if>" +
//            "           </trim>)" +
//            "</script>"

    @Insert("insert into cla (uuid,college,profession,claName,)values(#{cla.uuid}, #{cla.college}, #{cla.profession}, #{cla.claName})")
    void insertCla(@Param("cla") Cla cla);


    @Select("select * from cla where college = #{college} order by sort desc")
    List<Cla> selectMyCollegeAllCla(@Param("college") String college);

    @Select("select * from cla where course_uuid_list like '%${uuid}%'")
    Cla selectClaByCourseUuid(@Param("uuid") String uuid);

    @Update("update cla set course_uuid_list = #{course_uuid_list } where uuid = #{cla_uuid}")
    void addCourse(@Param("cla_uuid") String cla_uuid, @Param("course_uuid_list") String course_uuid_list);
}
