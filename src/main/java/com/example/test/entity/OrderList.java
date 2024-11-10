package com.example.test.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderList implements Serializable {
    private Integer id;
    private String code;
    private Date createTime;
    private Integer userId;
    private Integer songId;
    private byte ispay;
    private byte ostatus;
    private User user;
    private Song song;
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public byte getIspay() {
        return ispay;
    }

    public void setIspay(byte ispay) {
        this.ispay = ispay;
    }

    public byte getOstatus() {
        return ostatus;
    }

    public void setOstatus(byte ostatus) {
        this.ostatus = ostatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
