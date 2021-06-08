package com.wangzhen.models.problem;

/**
 * @Author wangzhen
 * @Description 编程题答案类型
 * @CreateDate 2020/1/8 18:00
 */
public class ProgramAnswer {
    private Option option_1;
    private Option option_2;
    private Option option_3;
    private Option option_4;
    private Option option_5;
    private Option option_6;
    private Option option_7;
    private Option option_8;
    private Option option_9;
    private Option option_10;

    public Option getOption_1() {
        return option_1;
    }

    public void setOption_1(Option option_1) {
        this.option_1 = option_1;
    }

    public Option getOption_2() {
        return option_2;
    }

    public void setOption_2(Option option_2) {
        this.option_2 = option_2;
    }

    public Option getOption_3() {
        return option_3;
    }

    public void setOption_3(Option option_3) {
        this.option_3 = option_3;
    }

    public Option getOption_4() {
        return option_4;
    }

    public void setOption_4(Option option_4) {
        this.option_4 = option_4;
    }

    public Option getOption_5() {
        return option_5;
    }

    public void setOption_5(Option option_5) {
        this.option_5 = option_5;
    }

    public Option getOption_6() {
        return option_6;
    }

    public void setOption_6(Option option_6) {
        this.option_6 = option_6;
    }

    public Option getOption_7() {
        return option_7;
    }

    public void setOption_7(Option option_7) {
        this.option_7 = option_7;
    }

    public Option getOption_8() {
        return option_8;
    }

    public void setOption_8(Option option_8) {
        this.option_8 = option_8;
    }

    public Option getOption_9() {
        return option_9;
    }

    public void setOption_9(Option option_9) {
        this.option_9 = option_9;
    }

    public Option getOption_10() {
        return option_10;
    }

    public void setOption_10(Option option_10) {
        this.option_10 = option_10;
    }

    class Option{
        private String input;
        private String output;

        @Override
        public String toString() {
            return "Option{" +
                    "input='" + input + '\'' +
                    ", output='" + output + '\'' +
                    '}';
        }

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
    }
}
