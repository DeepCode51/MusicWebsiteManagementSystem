package com.example.test.entity;

import java.io.Serializable;

public class Updown implements Serializable {
    private Integer id;
    private Integer userId;
    private Byte optionType; //类型 0是点赞，1是举报
    private String reason;
    private Integer commentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getOptionType() {
        return optionType;
    }

    public void setOptionType(Byte optionType) {
        this.optionType = optionType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
}
