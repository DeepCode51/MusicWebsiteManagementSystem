package com.example.test.dao;

import com.example.test.entity.Song;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* 歌曲Dao
* */
@Repository
public interface SongMapper {
    /*
    * 增加
    * */
    public int insert(Song song);

    /*
    * 修改
    * */
    public int update(Song song);

    /*
     * 更新点击次数
     * */
    public int updateClick(Song song);

    /*
     * 删除
     * */
    public int delete(Integer id);
    public int deleteBySingerId(Integer singerId);

    /*
     * 根据主键查询
     * */
    public Song selectByPrimaryKey(Integer id);

    /*
     * 查询所有歌曲
     * */
    public List<Song> allSong();

    /*
     * 根据歌名精确查询
     * */
    public List<Song> songOfName(String name);

    /*
     * 根据歌名模糊查询
     * */
    public List<Song> likeSongOfName(String name);

    /*
     * 根据歌手id查询
     * */
    public List<Song> songOfSingerId(Integer singerId);

    public int existSong(@Param("name") String name,@Param("singerId") Integer singerId);
}
