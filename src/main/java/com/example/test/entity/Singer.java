package com.example.test.entity;

import java.io.Serializable;
import java.util.Date;

/*
 * 歌手
 * */
public class Singer implements Serializable {
    /*主鍵*/
    private Integer id;
    /*歌手名*/
    private String name;
    /*性别 1男0女*/
    private Byte sex;
    /*图片*/
    private String pic;
    /*生日*/
    private Date birth;
    /*地区*/
    private String location;
    /*介绍*/
    private String introduction;
    /*歌曲表*/
    private Song song;
    /*歌单歌曲表*/
    private ListSong listSong;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public ListSong getListSong() {
        return listSong;
    }

    public void setListSong(ListSong listSong) {
        this.listSong = listSong;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
