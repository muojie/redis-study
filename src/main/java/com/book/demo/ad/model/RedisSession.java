package com.book.demo.ad.model;

import com.book.demo.ad.util.UUIDUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RedisSession implements Serializable {

    private static final long serialVersionUID = 1L;
    /** 回话ID **/
    private String sessionId = UUIDUtil.uuid();
    /** 存储对象的map **/
    private Map<String, Object> map = new HashMap<String, Object>();

    public String getSessionId() {
        return sessionId;
    }

    public Object getAttribute(String name) {
        return map.get(name);
    }

    public void setAttribute(String name, Object value) {
        map.put(name, value);
    }
}
