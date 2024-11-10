package com.example.test.service;

import com.example.test.entity.Ranks;
import com.example.test.entity.User;

/*
* 评价service接口
* */
public interface RanksService {
    /*
     * 增加
     * */
    public boolean insert(Ranks ranks);
    /*
     * 查总分
     * */
    public int selectSocreSum(Integer songListId);
    /*
     * 查总评人数
     * */
    public int selectRanksNum(Integer songListId);
    /*
    * 计算平均分
    * */
    public int ranksofSongListId(Integer songListId);

}
