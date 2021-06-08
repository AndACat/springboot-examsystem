//package com.wangzhen.utils.typehandler;
//
//import com.alibaba.fastjson.JSON;
//import com.wangzhen.models.Course;
//import com.wangzhen.models.Paper;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedJdbcTypes;
//import org.apache.ibatis.type.MappedTypes;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author wangzhen
// * @Description
// * @CreateDate 2020/3/18 23:03
// */
//@MappedTypes(value = {List.class})
//@MappedJdbcTypes(value = {JdbcType.VARCHAR,JdbcType.OTHER}, includeNullJdbcType = true)
//public class ArrayCourseTypeHandler<T extends Object> extends BaseTypeHandler<List<Course>> {
//
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, List<Course> parameter, JdbcType jdbcType) throws SQLException {
//        List<String> course_uuid_list = new ArrayList<>();
//        for (Course course : parameter) {
//            course_uuid_list.add(course.getUuid());
//        }
//    }
//
//    @Override
//    public List<Course> getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public List<Course> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        return null;
//    }
//
//    @Override
//    public List<Course> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        return null;
//    }
//}
