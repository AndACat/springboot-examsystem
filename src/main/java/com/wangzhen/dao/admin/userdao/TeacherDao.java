package com.wangzhen.dao.admin.userdao;

import com.wangzhen.models.users.Teacher;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/8 18:00
 */
@Mapper@Component
public interface TeacherDao {
    @Insert("<script>" +
            "insert into teacher (" +
            "<trim suffixOverrides=','>"+
            "uuid,name,account,code,birthday,sex,tno,email,phone,college,profession," +
            "<if test='faceImg != null'>faceImg,</if>"+
            "<if test=\"faceFeatureData != null\">faceFeatureData</if>"+
            "</trim>"+
            ")" +
            "values (" +
            "<trim suffixOverrides=','>"+
            "#{uuid},#{name},#{account},#{code},#{birthday},#{sex},#{tno},#{email},#{phone},#{college},#{profession}," +
            "<if test='faceImg != null'>#{faceImg},</if>"+
            "<if test='faceFeatureData != null'>#{faceFeatureData},</if>"+
            "</trim>"+
            ")" +
            "</script>")
    boolean insertTeacher(@Param("uuid") String uuid,
                          @Param("name")String name,
                          @Param("account") String account,
                          @Param("code")String code,
                          @Param("birthday") Date birthday,
                          @Param("sex") boolean sex,
                          @Param("tno")String tno,
                          @Param("email") String email,
                          @Param("phone")String phone,
                          @Param("college")String college,
                          @Param("profession")String profession,
                          @Param("faceImg")String faceImg,
                          @Param("faceFeatureData")String faceFeatureData);

    @Delete("delete from teacher where account = #{account}")
    boolean deleteTeacherByAccount(@Param("account") String account);

    @Select("select name, account, birthday, sex, faceImg, college, profession, tno, email, phone, classAndCourseLists, enabled from teacher")
    List<Teacher> selectAllTeacher();

    @Select("select * from teacher where (account = #{account} or email = #{email})")
    Teacher selectTeacherByAccount(@Param("account") String account,
                                   @Param("email")String email);



    @Update("<script>" +
            "update teacher set " +
            "<trim suffixOverrides=','>" +
            "name = #{name}," +
            "college = #{college}," +
            "profession = #{profession}," +
            "birthday = #{birthday}," +
            "sex = #{sex}," +
            "email = #{email}," +
            "phone = #{phone}," +
            "tno = #{tno}," +
            "enabled = #{enabled}," +
            "<if test='faceImg != null'>" +
            "   faceImg = #{faceImg}," +
            "</if>" +
            "<if test='faceFeatureData != null'>" +
            "   faceFeatureData = #{faceFeatureData}" +
            "</if>" +
            "</trim>"+
            "where account = #{account}" +
            "</script>")
    boolean updateTeacherInfo(@Param("account") String account,
                                      @Param("name")String name,
                                      @Param("college")String college,
                                      @Param("profession")String profession,
                                      @Param("birthday")Date birthday,
                                      @Param("sex")boolean sex,
                                      @Param("email")String email,
                                      @Param("phone")String phone,
                                      @Param("faceImg")String faceImg,
                                      @Param("faceFeatureData")String faceFeatureData,
                                      @Param("tno")String tno,
                                      @Param("enabled")boolean enabled);

    @Update("update teacher set code = #{code} where account = #{account}")
    boolean changeCode(@Param("account") String account,
                       @Param("code") String code);
    @Update("update teacher set " +
            "name = #{name}, code = #{encode}" +
            "where account = #{account}")
    boolean updateTeacherInfo1(@Param("account") String account, @Param("name")String name, @Param("encode")String encode);
}
