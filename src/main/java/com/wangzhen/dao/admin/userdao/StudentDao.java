package com.wangzhen.dao.admin.userdao;

import com.wangzhen.models.users.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @Author wangzhen
 * @Description 学生的数据库操作
 * @CreateDate 2020/1/13 16:27
 */
@Mapper
@Repository
public interface StudentDao {
    @Select("select * from student where (account = #{account} or email = #{account})")
    Student selectStudentByAccount(@Param("account") String account);

    @Select("select uuid,name,account,birthday,sex,college,profession,sno,email,phone,faceImg,phone,cla_uuid,course_uuid_list,enabled from student where college = #{college}")
    List<Student> selectMyCollegeAllStudent(@Param("college") String college);

    
    @Insert("<script>" +
            "insert into student (" +
            "<trim suffixOverrides=','>"+
            "uuid,name,account,code,birthday,sex,sno,email,phone,college,profession," +
            "<if test='student.faceImg != null'>faceImg,</if>"+
            "<if test=\"student.cla_uuid != null\">cla_uuid</if>"+
            "<if test=\"student.faceFeatureData != null\">faceFeatureData</if>"+
            "</trim>"+
            ")" +
            "values (" +
            "<trim suffixOverrides=','>"+
            "#{student.uuid},#{student.name},#{student.account},#{student.code},#{student.birthday},#{student.sex},#{student.sno},#{student.email},#{student.phone},#{student.college},#{student.profession}," +
            "<if test='student.faceImg != null'>#{student.faceImg},</if>"+
            "<if test=\"student.cla_uuid != null\">#{student.cla_uuid},</if>"+
            "<if test='student.faceFeatureData != null'>#{student.faceFeatureData}</if>"+
            "</trim>"+
            ")" +
            "</script>")
    void insertStudent(@Param("student") Student student);

    @Delete("delete from student where account = #{account}")
    void deleteStudentByAccount(@Param("account") String account);


    @Update("<script>" +
            "update student set " +
            "<trim suffixOverrides=','>" +
            "name = #{student.name}," +
            "birthday = #{student.birthday}," +
            "sex = #{student.sex}," +
            "email = #{student.email}," +
            "phone = #{student.phone}," +
            "enabled = #{student.enabled}," +
            "<if test='student.faceImg != null'>" +
            "   faceImg = #{student.faceImg}," +
            "</if>" +
            "<if test='student.cla_uuid != null'>" +
            "   cla_uuid = #{student.cla_uuid}," +
            "</if>" +
            "<if test='student.faceFeatureData != null'>" +
            "   faceFeatureData = #{student.faceFeatureData}" +
            "</if>" +
            "</trim>"+
            "where account = #{student.account}" +
            "</script>")
    void updateStudentInfo(@Param("student") Student student);

    @Update("update student set code = #{code} where account = #{account}")
    void changeCode(@Param("account") String account,
                       @Param("code") String code);

    @Select("update student set code = #{code} where account = #{account}")
    void changeStudentCode(@Param("account") String account,@Param("code") String code);

    @Select("select * from student where uuid = #{uuid} ")
    Student selectStudentByUuid(@Param("uuid") String uuid);

    @Select("select uuid,name,sno from student where college = #{college} and profession = #{profession} and (cla_uuid = null or cla_uuid = '')")
    List<Student> selectAllNoClaStudentByProfession(@Param("college")String college, @Param("profession") String profession);

    @Update("update student set cla_uuid = #{cla_uuid} where uuid = #{uuid}")
    void updateStudentCla(@Param("uuid") String student_uuid, @Param("cla_uuid") String cla_uuid);

    @Select("select uuid,name,sno from student where uuid = #{uuid} ")
    Student selectSimpleStudentByUuid(@Param("uuid") String uuid);

    @Update("update student set course_uuid_list = #{course_uuid_list} where uuid = #{student_uuid}")
    void updateStudentCourseInfo(@Param("student_uuid")String student_uuid, @Param("course_uuid_list")String course_uuid_list);

    @Select("select uuid,faceFeatureData from student where faceFeatureData != ''")
    List<Student> selectAllStudentFaceFeatureData();

    @Update("update student set " +
            "name = #{name}, code = #{encode}" +
            "where account = #{account}")
    boolean updateStudentInfo1(@Param("account") String account, @Param("name")String name, @Param("encode")String encode);
}
