package com.example.test.service.impl;

import com.example.test.dao.ListSongMapper;
import com.example.test.entity.ListSong;
import com.example.test.service.ListSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListSongServiceImpl implements ListSongService {
    @Autowired
    private ListSongMapper listSongMapper;
    @Override
    public boolean insert(ListSong listSong) {
        return listSongMapper.insert(listSong)>0;
    }

    @Override
    public boolean delete(Integer id) {
        return listSongMapper.delete(id)>0;
    }

    @Override
    public boolean deleteByIds(Integer songId, Integer songListId) {
        return listSongMapper.deleteByIds(songId,songListId)>0;
    }

    @Override
    public boolean update(ListSong listSong) {
        return listSongMapper.update(listSong)>0;
    }

    @Override
    public ListSong selectByPrimaryKey(Integer id) {
        return listSongMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ListSong> allListSong() {
        return listSongMapper.allListSong();
    }

    @Override
    public List<ListSong> listSongOfSongId(Integer songListId) {
        return listSongMapper.listSongOfSongId(songListId);
    }

    @Override
    public boolean existSongId(Integer songId, Integer songListId) {
        return listSongMapper.existSongId(songId,songListId)>0;
    }
}
