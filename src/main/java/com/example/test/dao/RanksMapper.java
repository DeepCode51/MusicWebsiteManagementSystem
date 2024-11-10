package com.example.test.dao;

import com.example.test.entity.Ranks;
import com.example.test.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface RanksMapper {
    /*
    * 增加
    * */
    public int insert(Ranks ranks);
    /*
    * 查总分
    * */
    public int selectSocreSum(Integer songListId);
    /*
    * 查总评人数
    * */
    public int selectRanksNum(Integer songListId);

}
