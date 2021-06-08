package com.wangzhen.utils.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangzhen.models.Paper;
import com.wangzhen.models.problem.*;
import com.wangzhen.models.problem.Short;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/18 23:03
 */
@MappedTypes(value = {Paper.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR}, includeNullJdbcType = true)
public class PaperInfoTypeHandler<T extends Object> extends BaseTypeHandler<LinkedHashMap<String,Object>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LinkedHashMap<String, Object> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,JSON.toJSONString(parameter));
    }

    @Override
    public LinkedHashMap<String, Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return getProblemMap(rs.getString(columnName));
    }

    @Override
    public LinkedHashMap<String, Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return getProblemMap(rs.getString(columnIndex));
    }

    @Override
    public LinkedHashMap<String, Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getProblemMap(cs.getString(columnIndex));
    }

    private LinkedHashMap<String, Object> getProblemMap(String problemMap){
        Map<String, Object> map = new LinkedHashMap<>();
        LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>();
        JSONObject jsonObject = JSON.parseObject(problemMap);
        for (String problemType : jsonObject.keySet()) {
            String problemList = jsonObject.getString(problemType);
            switch (problemType){
                case "singleChoiceList":{
                    List<SingleChoice> singleChoiceList = JSON.parseArray(problemList, SingleChoice.class);
                    map.put(problemType,singleChoiceList);
                    break;
                }
                case "multipleChoiceList":{
                    List<MultipleChoice> multipleChoiceList = JSON.parseArray(problemList, MultipleChoice.class);
                    map.put(problemType,multipleChoiceList);
                    break;
                }
                case "fillList":{
                    List<Fill> fillList = JSON.parseArray(problemList, Fill.class);
                    map.put(problemType,fillList);
                    break;
                }
                case "shortList":{
                    List<Short> shortList = JSON.parseArray(problemList, Short.class);
                    map.put(problemType,shortList);
                    break;
                }
                case "judgeList":{
                    List<Judge> judgeList = JSON.parseArray(problemList, Judge.class);
                    map.put(problemType,judgeList);
                    break;
                }
                case "programList":{
                    List<Program> programList = JSON.parseArray(problemList, Program.class);
                    map.put(problemType,programList);
                    break;
                }
            }

        }
        int singleChoiceListIdx = problemMap.indexOf("singleChoiceList");
        int multipleChoiceListIdx = problemMap.indexOf("multipleChoiceList");
        int fillListIdx = problemMap.indexOf("fillList");
        int shortListIdx = problemMap.indexOf("shortList");
        int programListIdx = problemMap.indexOf("programList");
        int judgeListIdx = problemMap.indexOf("judgeList");
        LinkedHashMap<Integer, String> sortIdx = getSortIdx(singleChoiceListIdx, multipleChoiceListIdx, fillListIdx, shortListIdx, judgeListIdx, programListIdx);
        for (String value : sortIdx.values()) {
            returnMap.put(value,map.get(value));
        }
        return returnMap;
    }
    private LinkedHashMap<Integer,String> getSortIdx(int singleChoiceListIdx,int multipleChoiceListIdx,int fillListIdx,int shortListIdx,int judgeListIdx,int programListIdx){
        LinkedHashMap<Integer,String> map = new LinkedHashMap<>();
        for (int i = 0; i < 6; i++) {
            int minIdx = Integer.MAX_VALUE;
            String problemType = null;
            if(singleChoiceListIdx < minIdx && singleChoiceListIdx != -1){
                minIdx = singleChoiceListIdx;
                problemType = "singleChoiceList";
            }
            if(multipleChoiceListIdx < minIdx && multipleChoiceListIdx != -1){
                minIdx = multipleChoiceListIdx;
                problemType = "multipleChoiceList";
            }
            if(fillListIdx < minIdx && fillListIdx != -1){
                minIdx = fillListIdx;
                problemType = "fillList";
            }
            if(shortListIdx < minIdx && shortListIdx != -1){
                minIdx = shortListIdx;
                problemType = "shortList";
            }
            if(judgeListIdx <minIdx && judgeListIdx != -1){
                minIdx = judgeListIdx;
                problemType = "judgeList";
            }
            if(programListIdx < minIdx && programListIdx != -1){
                minIdx = programListIdx;
                problemType = "programList";
            }
            if(problemType != null){
                map.put(minIdx,problemType);
                if(minIdx == singleChoiceListIdx) singleChoiceListIdx = -1;
                else if(minIdx == multipleChoiceListIdx) multipleChoiceListIdx = -1;
                else if(minIdx == fillListIdx) fillListIdx = -1;
                else if(minIdx == shortListIdx) shortListIdx = -1;
                else if(minIdx == judgeListIdx) judgeListIdx = -1;
                else if(minIdx == programListIdx) programListIdx = -1;
            }
        }
        return map;
    }
}
