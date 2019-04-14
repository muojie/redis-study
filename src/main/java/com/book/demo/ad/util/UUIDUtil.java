package com.book.demo.ad.util;

import java.util.UUID;

/**
 * @描述 : uuid工具类
 */
public class UUIDUtil {

    /**
     * @描述 : 获取一个UUID标识(小写)
     */
    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    /**
     * @描述 : 获取一个UUID标识(大写)
     */
    public static String upperUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase();
    }
}
