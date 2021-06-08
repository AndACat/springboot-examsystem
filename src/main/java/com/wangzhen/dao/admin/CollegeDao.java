package com.wangzhen.dao.admin;

import com.wangzhen.models.College;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/7 21:19
 */
@Component
@Mapper
public interface CollegeDao {
    @Insert("insert into college (uuid,name,professionList) values (#{uuid} , #{name} , #{professionList})")
    boolean insertCollege(String uuid, String name, String professionList);

    @Update("update college set name = #{name} , professionList = #{professionList} where uuid = #{uuid}")
    boolean updateCollege(String name,String professionList,String uuid);

    @Select("select * from college")
    List<College> selectAllCollege();

    @Select("select * from college where name = #{name}")
    College selectCollegeByName(String name);

    @Select("select * from college where profession like #{profession}")
    College selectCollegeByProfession(String profession);

    @Delete("delete from college where uuid = #{uuid}")
    boolean deleteCollegeByUuid(String uuid);

    @Delete("delete from college where name = #{name}")
    boolean deleteCollegeByName(String name);
}
