package com.example.test.service;

import com.example.test.entity.Song;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SongService {
    /*
     * 增加
     * */
    public boolean insert(Song song);

    /*
     * 修改
     * */
    public boolean update(Song song);

    /*
     * 更新点击次数
     * */
    public boolean updateClick(Song song);

    /*
     * 删除
     * */
    public boolean delete(Integer id);
    public boolean deleteBySingerId(Integer singerId);

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

    public boolean existSong(@Param("name") String name, @Param("singerId") Integer singerId);
}
