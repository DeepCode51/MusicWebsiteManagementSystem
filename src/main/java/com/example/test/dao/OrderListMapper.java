package com.example.test.dao;

import com.example.test.entity.OrderList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderListMapper {
    /*
     * 增加
     * */
    public int insert(OrderList orderList);

    /*
     * 修改
     * */
    public int update(OrderList orderList);

    /*
     * 删除
     * */
    public int delete(Integer id);

    /*
     * 根据主键查询
     * */
    public OrderList selectByPrimaryKey(Integer id);

    /*
     * 查询所有订单
     * */
    public List<OrderList> allOrder();

    /*
     * 查询用户订单
     * */
    public List<OrderList> orderOfUserId(Integer userId);

    public int existSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);
}
