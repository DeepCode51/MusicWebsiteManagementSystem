package com.example.test.dao;

import com.example.test.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* 评论Dao
* */
@Repository
public interface CommentMapper {
    /*
    * 增加
    * */
    public int insert(Comment comment);

    /*
    * 修改
    * */
    public int update(Comment comment);

    /*
     * 删除
     * */
    public int delete(Integer id);

    /*
     * 根据主键查询
     * */
    public Comment selectByPrimaryKey(Integer id);

    /*
     * 查询所有评论
     * */
    public List<Comment> allComment();

    /*
     * 根据歌曲查询评论
     * */
    public List<Comment> commentOfSongId(Integer songId);

    /*
     * 根据歌单查询评论
     * */
    public List<Comment> commentOfSongListId(Integer songListId);
}
