package com.wangzhen.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * @Author wangzhen
 * @Description 用于请求之后返回的json
 * @CreateDate 2020/1/14 15:07
 */
@Component
public class ResponseMessage {
    public static final ResponseMessage OK = new ResponseMessage().setCode(200).setMsg("执行成功");
    public static final ResponseMessage FAIL = new ResponseMessage().setCode(403).setMsg("执行失败");
    private Integer code;
    private String msg;
    private Object data;
    private String href;

    public ResponseMessage() {}

    public static ResponseMessage getInstance(){
        return new ResponseMessage();
    }

    public ResponseMessage(String msg) {
        this.msg = msg;
    }

    public ResponseMessage(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

    public ResponseMessage(String msg, Object data, String href) {
        this.msg = msg;
        this.data = data;
        this.href = href;
    }

    public ResponseMessage(Integer code, String msg, Object data, String href) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.href = href;
    }

    public ResponseMessage ok(String msg, Object data, String href){
        this.code = 200;
        this.msg = msg;
        this.data = data;
        this.href = href;
        return this;
    }
    public ResponseMessage ok(String msg, Object data){
        this.code = 200;
        this.msg = msg;
        this.data = data;
        return this;
    }
    public ResponseMessage ok(String msg){
        this.code = 200;
        this.msg = msg;
        return this;
    }
    public ResponseMessage setCode(Integer code){
        this.code = code;
        return this;
    }
    public ResponseMessage setMsg(String msg){
        this.msg = msg;
        return this;
    }
    public ResponseMessage setData(Object data){
        this.data = data;
        return this;
    }
    public ResponseMessage setHref(String href){
        this.href = href;
        return this;
    }
    public String toJsonString(){
        return JSON.toJSONString(this);
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public String getHref() {
        return href;
    }
}
