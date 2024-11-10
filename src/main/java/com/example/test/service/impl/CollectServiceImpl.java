package com.example.test.service.impl;

import com.example.test.dao.CollectMapper;
import com.example.test.entity.Collect;
import com.example.test.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectMapper collectMapper;

    @Override
    public boolean insert(Collect collect) {
        return collectMapper.insert(collect)>0;
    }

    @Override
    public boolean delete(Integer songId, Integer userId) {
        return collectMapper.delete(songId,userId)>0;
    }



    @Override
    public List<Collect> allCollect() {
        return collectMapper.allCollect();
    }

    @Override
    public List<Collect> collectOfUserId(Integer userId) {
        return collectMapper.collectOfUserId(userId);
    }

    @Override
    public boolean existSongId(Integer userId, Integer songId) {
        return collectMapper.existSongId(userId,songId)>0;
    }

    @Override
    public boolean deleteByUser(Integer userId, Integer songId) {
        return collectMapper.deleteByUser(userId,songId)>0;
    }
}
