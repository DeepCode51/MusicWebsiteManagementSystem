package com.example.test.dao;

import com.example.test.entity.ListSong;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* 歌单内歌曲
* */
@Repository
public interface ListSongMapper {

    public int insert(ListSong listSong);

    public int delete(Integer id);

    public int deleteByIds(Integer songId,Integer songListId);

    public int update(ListSong listSong);

    public ListSong selectByPrimaryKey(Integer id);

    public List<ListSong> allListSong();

    public List<ListSong> listSongOfSongId(Integer songListId);

    public int existSongId(@Param("songId") Integer SongId, @Param("songListId") Integer songListId);
}
