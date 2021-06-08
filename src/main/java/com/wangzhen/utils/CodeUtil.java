package com.wangzhen.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Author wangzhen
 * @Description
 * @CreateDate 2020/2/2 16:09
 */
@Component
public class CodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtil.class);
    public CodeUtil() {
    }

    // 提供强度的构造方法
    public CodeUtil(int strengthLevel, int length) {
        this.strengthLevel = strengthLevel;
        this.length = length;
    }



    /**
     * Password level config
     * 1 level : only number
     * 2 level : number and lowercase letters
     * 3 level : number , lowercase letters , capital letters
     * 4 level : number , lowercase letters , capital letters , special characters
     */
    private int strengthLevel = 3;
    // 需要的密码长度
    private int length = 6;
    private static final Random RANDOM = new Random();

    // getter and setter
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(int strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    // 用char类型是因为char类型效率比数组方式要高

    // 因为伪随机数random在nextInt()的时候会出现向更小的数偏离的情况，所以用一个randomMax来修正
    private static final int randomMax = 100000000;
    // 因为数组下标从0开始
    private static final int index = 1;
    // 一共26个字母
    private static final int numberOfLetter = 26;
    // ascii码表数字从48开始
    private static final int nunberIndex = 48;
    // 数组下标0-9代表10个数字
    private static final int numberMax = 9;
    // 大写字母ascii码表从65开始
    private static final int capitalIndex = 65;
    // 小写字母ascii码表从65开始
    private static final int lowercaseIndex = 97;
    // 特殊字符的起始ascii码表序号取第一个数字
    private static final int special = 0;
    // 特殊字符集，第一个数字代表ascii码表序号，第二个代表从这里开始一共使用多少个字符
    private static final int[][] specialCharacter = {{33, 14}, {58, 6}, {91, 5}, {123, 3}};

    // 取随机密码
    public String getRandomCode(int length) {
        this.length = length;
        StringBuffer buffer = new StringBuffer();
        if (length <= 0){
            this.length = 6;
        };
        for (int i = 0; i < length; i++)
            buffer.append((char) getNextChar());
        return buffer.toString();
    }

    // 取得下一个ascii编码
    private int getNextChar(){
        if (strengthLevel < 1 || strengthLevel > 4){
            logger.error("密码工具登录错误，自动设为3级");
            strengthLevel = 3;
        }
        // ascii编码
        int x = 0;
        // 伪字符ascii编码
        final int puppetLetter = RANDOM.nextInt(randomMax) % numberOfLetter;
        // 伪数字ascii编码
        final int puppetNumber = RANDOM.nextInt(randomMax) % numberMax + nunberIndex;
        // 按照字符等级强度取ascii值
        final int levelIndex = RANDOM.nextInt(randomMax) % strengthLevel;
        // 特殊字符的随机集合下标（数组第一维）
        final int specialType = RANDOM.nextInt(randomMax) % specialCharacter.length;
        // 特殊字符的ascii编码
        final int specialInt = RANDOM.nextInt(randomMax) % specialCharacter[specialType][index] + specialCharacter[specialType][special];
        // 根据密码强度等级获取随机ascii编码
        switch (strengthLevel) {
            case 1:
                x = puppetNumber;
                break;
            case 2:
                if (levelIndex == index)
                    x = puppetNumber;
                else
                    x = puppetLetter + lowercaseIndex;
                break;
            case 3:
                if (levelIndex == 0)
                    x = puppetNumber;
                else if (levelIndex == index)
                    x = puppetLetter + lowercaseIndex;
                else
                    x = puppetLetter + capitalIndex;
                break;
            case 4:
                if (levelIndex == 0)
                    x = puppetNumber;
                else if (levelIndex == index)
                    x = puppetLetter + lowercaseIndex;
                else if (levelIndex == index * 2)
                    x = puppetLetter + capitalIndex;
                else
                    x = specialInt;
                break;
            default:
        }
        return x;
    }
}
