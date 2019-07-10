package com.imooc.utils;

import java.util.Random;

/**
 * created By 李世豪
 */
public class keyUtil {
    /**
     * 生成唯一的主键
     * 格式：时间+随机数
     * 为了解决多线程并发,随机数重复（synchronized）
     * @return
     */
    public static synchronized String genUniqueKey() {
        Random random = new Random();

        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);


    }
}
