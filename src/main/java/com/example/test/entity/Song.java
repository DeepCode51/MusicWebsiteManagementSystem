package com.example.test.entity;

import java.io.Serializable;
import java.util.Date;

/*
 * 歌曲
 * */
public class Song implements Serializable {
    /*主鍵*/
    private Integer id;
    /*歌曲名*/
    private String name;
    /*歌手id*/
    private Integer singerId;
    /*图片*/
    private String pic;
    /*创建时间*/
    private Date createTime;
    /*更新时间*/
    private Date updateTime;
    /*歌词*/
    private String lyric;
    /*地址*/
    private String url;
    /*价格*/
    private String price;
    /*专辑*/
    private String introduction;
    private Integer clickTimes;
    private String theme;
    private Comment comment;
    private ListSong listSong;
    private Updown updown;
    private Collect collect;
    private  OrderList orderList;

    public OrderList getOrderList() {
        return orderList;
    }

    public void setOrderList(OrderList orderList) {
        this.orderList = orderList;
    }

    public Collect getCollect() {
        return collect;
    }

    public void setCollect(Collect collect) {
        this.collect = collect;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public ListSong getListSong() {
        return listSong;
    }

    public void setListSong(ListSong listSong) {
        this.listSong = listSong;
    }

    public Updown getUpdown() {
        return updown;
    }

    public void setUpdown(Updown updown) {
        this.updown = updown;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Integer getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Integer clickTimes) {
        this.clickTimes = clickTimes;
    }
    public Integer getSingerId() {
        return singerId;
    }

    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

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
}
