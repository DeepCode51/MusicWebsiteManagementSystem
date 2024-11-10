package com.example.test.dao;

import com.example.test.entity.Updown;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdownMapper {
    /*
     * 增加
     * */
    public int insert(Updown updown);
    
    /*
     * 删除
     * */
    public int delete(@Param("commentId") Integer commentId,@Param("userId") Integer userId,@Param("optionType") Byte optionType);
    public int deleteById(Integer id);
    /*
     * 根据评论ID查询点赞/举报
     * */
    public List<Updown> updownOfCommentId(Integer commentId,Byte optionType);

    /*
     * 查询用户是否已经点赞/举报
     * */
    public int existUpdown(@Param("commentId") Integer commentId,@Param("userId") Integer userId, @Param("optionType") Byte optionType);

    /*
     * 查询点赞/举报数
     * */
    public int updownNum(@Param("commentId") Integer commentId,@Param("optionType") Byte optionType);

    /*
     * 根据某个用户的收藏列表
     * */
    public List<Updown> updownOfUserId(Integer userId);
}
