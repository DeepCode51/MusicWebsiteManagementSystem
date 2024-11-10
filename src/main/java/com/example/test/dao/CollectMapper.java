package com.example.test.dao;

import com.example.test.entity.Collect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* 收藏Dao
* */
@Repository
public interface CollectMapper {
    /*
    * 增加
    * */
    public int insert(Collect collect);

    /*
     * 删除
     * */
    public int delete(Integer songId,Integer userId);

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
    public int existSongId(@Param("userId") Integer userId,@Param("songId") Integer songId);

    public int deleteByUser(@Param("userId") Integer userId,@Param("songId") Integer songId);

}
