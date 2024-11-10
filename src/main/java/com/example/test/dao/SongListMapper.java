package com.example.test.dao;

import com.example.test.entity.SongList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* 歌单Dao
* */
@Repository
public interface SongListMapper {
    /*
    * 增加
    * */
    public int insert(SongList songList);

    /*
    * 修改
    * */
    public int update(SongList songList);

    /*
     * 删除
     * */
    public int delete(Integer id);

    /*
     * 根据主键查询
     * */
    public SongList selectByPrimaryKey(Integer id);

    /*
     * 查询所有歌单
     * */
    public List<SongList> allSongList();

    /*
     * 根据标题模糊查询
     * */
    public List<SongList> likeTitle(String title);

    /*
     * 根据标题精确查询
     * */
    public List<SongList> songListOfTitle(String title);

    /*
     * 根据风格模糊查询
     * */
    public List<SongList> likeStyle(String style);

    public int existSongList(@Param("title")String title);

}
