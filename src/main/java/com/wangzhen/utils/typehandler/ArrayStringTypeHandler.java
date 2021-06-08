package com.wangzhen.utils.typehandler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/18 23:03
 */
@MappedTypes(value = {List.class, ArrayList.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR,JdbcType.OTHER}, includeNullJdbcType = true)
public class ArrayStringTypeHandler<T extends Object> extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JSON.parseArray(rs.getString(columnName),String.class);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JSON.parseArray(rs.getString(columnIndex),String.class);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JSON.parseArray(cs.getString(columnIndex),String.class);
    }
}
