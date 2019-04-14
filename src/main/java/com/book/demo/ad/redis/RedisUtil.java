package com.book.demo.ad.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisUtil {

    public static JedisPool initPool() throws IOException {
        // load redis config
        InputStream inputStream = RedisUtil.class.getClass()
                .getResourceAsStream("/redis-config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);

        //init redis pool config
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.valueOf(properties.getProperty("maxActive")));
        config.setMaxIdle(Integer.valueOf(properties.getProperty("maxIdle")));
        config.setMaxWaitMillis(Integer.valueOf(properties.getProperty("maxWait")));
        config.setTestOnBorrow(Boolean.valueOf(properties.getProperty("testOnBorrow")));
        String[] address = properties.getProperty("ip").split(":");

        // init redis pool
        JedisPool pool = new JedisPool(config, address[0],
                Integer.valueOf(address[1]), Integer.valueOf(properties.getProperty("timeout")));

        return pool;
    }
}
