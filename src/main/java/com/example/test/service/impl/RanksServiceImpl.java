package com.example.test.service.impl;

import com.example.test.dao.RanksMapper;
import com.example.test.entity.Ranks;
import com.example.test.service.RanksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RanksServiceImpl implements RanksService {
    @Autowired
    private RanksMapper ranksMapper;
    @Override
    public boolean insert(Ranks ranks) {
        return ranksMapper.insert(ranks)>0;
    }

    @Override
    public int selectSocreSum(Integer songListId) {
        return ranksMapper.selectSocreSum(songListId);
    }

    @Override
    public int selectRanksNum(Integer songListId) {
        return ranksMapper.selectRanksNum(songListId);
    }

    @Override
    public int ranksofSongListId(Integer songListId) {
        int rankNum = ranksMapper.selectRanksNum(songListId);
        if (rankNum == 0){
            return 0;
        }
        return ranksMapper.selectSocreSum(songListId)/rankNum;
    }


}
