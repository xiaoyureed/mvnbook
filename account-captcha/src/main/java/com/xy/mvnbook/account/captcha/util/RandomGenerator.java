package com.xy.mvnbook.account.captcha.util;

import java.util.Random;
/**
 * 生成随机串 工具类
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午11:06:06
 */
public class RandomGenerator {

    /**
     * 字符范围
     */
    private static final String RANGE = "0123456789abcdefghijklmnopqrstuvwxyz";
    /**
     * 生成随机的8位字符串
     * @return
     */
    public static synchronized String getRandomString() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            result.append(RANGE.charAt(random.nextInt(RANGE.length())));
        }
        return result.toString();
    }
}
