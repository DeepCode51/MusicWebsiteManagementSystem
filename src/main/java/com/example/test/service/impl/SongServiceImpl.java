package com.example.test.service.impl;

import com.example.test.dao.SongMapper;
import com.example.test.entity.Song;
import com.example.test.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* 歌曲Service实现类
* */
@Service
public class SongServiceImpl implements SongService {
    @Autowired
    private SongMapper songMapper;
    @Override
    public boolean insert(Song song) {
        return songMapper.insert(song)>0;
    }

    @Override
    public boolean update(Song song) {
        return songMapper.update(song)>0;
    }

    @Override
    public boolean updateClick(Song song) {
        return songMapper.updateClick(song)>0;
    }

    @Override
    public boolean delete(Integer id) {
        return songMapper.delete(id)>0;
    }
    @Override
    public boolean deleteBySingerId(Integer singerId) {
        return songMapper.delete(singerId)>0;
    }

    @Override
    public Song selectByPrimaryKey(Integer id) {
        return songMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Song> allSong() {
        return songMapper.allSong();
    }

    @Override
    public List<Song> songOfName(String name) {
        return songMapper.songOfName(name);
    }

    @Override
    public List<Song> likeSongOfName(String name) {
        return songMapper.likeSongOfName("%"+name+"%");
    }

    @Override
    public List<Song> songOfSingerId(Integer singerId) {
        return songMapper.songOfSingerId(singerId);
    }

    @Override
    public boolean existSong(String name, Integer singerId) {
        return songMapper.existSong(name,singerId)>0;
    }
}
