package com.example.test.service;

import com.example.test.entity.ListSong;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/*
* 歌单里面的歌曲Service
* */
public interface ListSongService {
    public boolean insert(ListSong listSong);

    public boolean delete(Integer id);

    public boolean deleteByIds(Integer songId,Integer songListId);

    public boolean update(ListSong listSong);

    public ListSong selectByPrimaryKey(Integer id);

    public List<ListSong> allListSong();

    public List<ListSong> listSongOfSongId(Integer songListId);

    public boolean existSongId(@Param("songId") Integer songId,@Param("songListId") Integer songListId);
}
