package com.wangzhen.dao.admin.userdao;

import com.wangzhen.models.users.Manager;
import com.wangzhen.models.users.Manager;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/1/12 18:00
 */
@Mapper
@Repository
public interface ManagerDao {
    @Select("select * from manager where (account = #{account} or email = #{email})")
    Manager selectManagerByAccount(@Param("account") String account, @Param("email") String email);

    @Insert("<script>" +
            "insert into manager (" +
            "<trim suffixOverrides=','>"+
            "uuid,name,account,code,birthday,sex,email,phone,mno,college," +
            "<if test='manager.faceImg != null'>faceImg,</if>"+
            "<if test=\"manager.faceFeatureData != null\">faceFeatureData</if>"+
            "</trim>"+
            ")" +
            "values (" +
            "<trim suffixOverrides=','>"+
            "#{manager.uuid},#{manager.name},#{manager.account},#{manager.code},#{manager.birthday},#{manager.sex},#{manager.email},#{manager.phone},#{manager.mno},#{manager.college}" +
            "<if test='manager.faceImg != null'>#{manager.faceImg},</if>"+
            "<if test='manager.faceFeatureData != null'>#{manager.faceFeatureData},</if>"+
            "</trim>"+
            ")" +
            "</script>")
    void insertManager(@Param("manager")Manager manager);

    @Delete("delete from manager where account = #{account}")
    void deleteManagerByAccount(@Param("account") String account);

    @Select("select name, account, birthday, sex, faceImg, email, phone, mno, college, enabled from manager")
    List<Manager> selectAllManager();


    @Update("<script>" +
            "update manager set " +
            "<trim suffixOverrides=','>" +
            "name = #{manager.name}," +
            "birthday = #{manager.birthday}," +
            "sex = #{manager.sex}," +
            "email = #{manager.email}," +
            "phone = #{manager.phone}," +
            "mno = #{manager.mno}," +
            "enabled = #{manager.enabled}," +
            "mno = #{manager.mno}," +
            "college = #{manager.college},"+
            "<if test='manager.faceImg != null'>" +
            "   faceImg = #{manager.faceImg}," +
            "</if>" +
            "<if test='manager.faceFeatureData != null'>" +
            "   faceFeatureData = #{manager.faceFeatureData}" +
            "</if>" +
            "</trim>"+
            "where account = #{manager.account}" +
            "</script>")
    void updateManagerInfo(@Param("manager") Manager manager);


    @Update("update manager set code = #{code} where account = #{account}")
    void changeCode(@Param("account") String account,
                       @Param("code") String code);
}
