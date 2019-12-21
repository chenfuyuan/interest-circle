package com.cfy.interest.utils;

import java.util.Random;

public class AuthCodeRandom {

    public static String getRandomNumberCode(int length) {
        //使用StringBuilder缓存字符串，进行随机6位验证码的生成
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
//            生成0-9的随机数
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
