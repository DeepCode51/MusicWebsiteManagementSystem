package com.example.test.service;

import com.example.test.entity.Collect;
import com.example.test.entity.Updown;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UpdownService {
    /*
     * 增加
     * */
    public boolean insert(Updown updown);

    /*
     * 删除
     * */
    public boolean delete(@Param("commentId") Integer commentId,@Param("userId") Integer userId,@Param("optionType") Byte optionType);
    public boolean deleteById(Integer id);
    /*
     * 根据评论ID查询点赞/举报
     * */
    public List<Updown> updownOfCommentId(Integer commentId,Byte optionType);

    /*
     * 查询用户是否已经点赞/举报
     * */
    public boolean existUpdown(@Param("commentId") Integer commentId, @Param("userId") Integer userId, @Param("optionType") Byte optionType);

    /*
     * 查询点赞/举报数
     * */
    public int updownNum(@Param("commentId") Integer commentId,@Param("optionType") Byte optionType);

    /*
     * 根据某个用户的点赞/举报
     * */
    public List<Updown> updownOfUserId(Integer userId);
}
