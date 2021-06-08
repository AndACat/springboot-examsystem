package com.wangzhen.utils.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/24 14:49
 */
@MappedJdbcTypes(value = JdbcType.VARCHAR,includeNullJdbcType = true)
@MappedTypes(value = {LinkedHashMap.class, Map.class})
public class MapIntegerObjectTypeHandler implements TypeHandler<LinkedHashMap<Integer,Object>> {
    public MapIntegerObjectTypeHandler() {

    }

    @Override
    public void setParameter(PreparedStatement ps, int i, LinkedHashMap<Integer, Object> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(parameter));
    }

    @Override
    public LinkedHashMap<Integer, Object> getResult(ResultSet rs, String columnName) throws SQLException {
        return map(rs.getString(columnName));
    }

    @Override
    public LinkedHashMap<Integer, Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return map(rs.getString(columnIndex));
    }

    @Override
    public LinkedHashMap<Integer, Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return map(cs.getString(columnIndex));
    }

    private LinkedHashMap<Integer,Object> map(String str){
        JSONObject jsonObject = JSON.parseObject(str);
        LinkedHashMap<Integer,Object> map = new LinkedHashMap<>();
        if(jsonObject != null){
            for (String s : jsonObject.keySet()) {
                map.put(Integer.parseInt(s),jsonObject.get(s));
            }
        }
        return map;
    }
}
