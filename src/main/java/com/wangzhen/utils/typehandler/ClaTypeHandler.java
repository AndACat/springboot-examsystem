//package com.wangzhen.utils.typehandler;
//
//import com.alibaba.fastjson.JSON;
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
//
///**
// * @Author wangzhen
// * @Description
// * @CreateDate 2020/3/18 23:03
// */
//@MappedTypes(value = {Paper.class})
//@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
//public class ClaTypeHandler<T extends Object> extends BaseTypeHandler<Paper> {
//
//
//    @Override
//    public void setNonNullParameter(PreparedStatement preparedStatement, int columnIndex, Paper t, JdbcType jdbcType) throws SQLException {
//        preparedStatement.setString(columnIndex, JSON.toJSONString(t));
//    }
//
//    @Override
//    public Paper getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
//        String str = resultSet.getString(columnName);
//        return JSON.parseObject(str, Paper.class);
//    }
//
//    @Override
//    public Paper getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
//        String str = resultSet.getString(columnIndex);
//        return JSON.parseObject(str, Paper.class);
//    }
//
//    @Override
//    public Paper getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
//        String str = callableStatement.getString(columnIndex);
//        return JSON.parseObject(str, Paper.class);
//    }
//}
