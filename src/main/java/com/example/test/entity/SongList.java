package com.example.test.entity;

import java.io.Serializable;
import java.util.Date;

/*
 * 歌单
 * */
public class SongList implements Serializable {
    private Integer id;

    private String title;

    private String pic;

    private String style;

    private String introduction;
    private Ranks ranks;
    private Comment comment;
    private Updown updown;

    /*歌单歌曲*/
    private ListSong listSong;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Updown getUpdown() {
        return updown;
    }

    public void setUpdown(Updown updown) {
        this.updown = updown;
    }

    public Ranks getRanks() {
        return ranks;
    }

    public void setRanks(Ranks ranks) {
        this.ranks = ranks;
    }

    public ListSong getListSong() {
        return listSong;
    }

    public void setListSong(ListSong listSong) {
        this.listSong = listSong;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
