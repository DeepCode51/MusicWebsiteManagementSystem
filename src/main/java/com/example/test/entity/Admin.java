package com.example.test.entity;

import java.io.Serializable;

/*
* 管理員
* */
public class Admin implements Serializable {
    /*主鍵*/
    private Integer id;
    /*账号*/
    private String name;
    /*密碼*/
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
