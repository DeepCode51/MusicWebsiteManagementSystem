package com.example.test.service;

import com.example.test.entity.Collect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
* 收藏service接口
* */
public interface CollectService {
    /*
     * 增加
     * */
    public boolean insert(Collect collect);

    /*
     * 删除
     * */
    public boolean delete(Integer songId,Integer userId);

    /*
     * 查询所有收藏
     * */
    public List<Collect> allCollect();

    /*
     * 根据某个用户的收藏列表
     * */
    public List<Collect> collectOfUserId(Integer userId);

    /*
     * 查询用户是否已经收藏歌曲
     * */
    public boolean existSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

    /*
     * 删除
     * */
    public boolean deleteByUser(@Param("userId") Integer userId,@Param("songId") Integer songId);
}
