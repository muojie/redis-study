package com.book.demo.ad.model;

import java.io.Serializable;

public class Template implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String script;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
