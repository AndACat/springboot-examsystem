package com.wangzhen.utils.typehandler;

import com.alibaba.fastjson.JSON;
import com.wangzhen.models.problem.Option;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/18 23:03
 */
@MappedTypes(value = {List.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR,JdbcType.OTHER}, includeNullJdbcType = true)
public class ArrayOptionTypeHandler<T extends Object> extends BaseTypeHandler<List<Option>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Option> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public List<Option> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JSON.parseArray(rs.getString(columnName),Option.class);
    }

    @Override
    public List<Option> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JSON.parseArray(rs.getString(columnIndex),Option.class);
    }

    @Override
    public List<Option> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JSON.parseArray(cs.getString(columnIndex),Option.class);
    }
}
