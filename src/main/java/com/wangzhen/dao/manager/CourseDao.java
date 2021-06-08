package com.wangzhen.dao.manager;

import com.wangzhen.models.Course;
import com.wangzhen.utils.typehandler.ArrayStringTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CourseDao {
    @Select("select * from course where teacher_uuid = #{teacher_uuid} order by sort desc")
    @Results(id = "student_uuid_list_result",
            value = @Result(typeHandler = ArrayStringTypeHandler.class,javaType = List.class,jdbcType = JdbcType.VARCHAR,column = "student_uuid_list",property = "student_uuid_list")
    )
    List<Course> selectMyAllCourse(@Param("teacher_uuid")String teacher_uuid);

    @Select("select * from course where uuid = #{uuid}")
    @ResultMap("student_uuid_list_result")
    Course selectCourseByUuid(@Param("uuid") String uuid);

    @Insert("insert into course(uuid,courseName,teacher_uuid,cla_uuid) values (#{course.uuid}, #{course.courseName}, #{course.teacher_uuid}, #{course.cla_uuid})")
    void insertCourse(@Param("course") Course course);

    @Delete("delete from course where uuid = uuid")
    void deleteCourseByUuid(@Param("uuid")String uuid);

    @Update("update course set student_uuid_list = #{student_uuid_list} where uuid = #{uuid}")
    void updateCourseInfo(@Param("student_uuid_list")String student_uuid_list,@Param("uuid")String uuid);


}
