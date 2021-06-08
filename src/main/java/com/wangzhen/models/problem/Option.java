package com.wangzhen.models.problem;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/3/17 19:14
 */

public class Option{
    public Option() { }
    public Option(String input, String output) {
        this.input = input;
        this.output = output;
    }

    private String input;
    private String output;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "OptionOption的toString方法{" +
                "input='" + input + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}