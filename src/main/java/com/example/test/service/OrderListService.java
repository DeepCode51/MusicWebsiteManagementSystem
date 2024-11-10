package com.example.test.service;

import com.example.test.entity.OrderList;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrderListService {
    /*
     * 增加
     * */
    public boolean insert(OrderList orderList);

    /*
     * 修改
     * */
    public boolean update(OrderList orderList);
//
//    /*
//     * 删除
//     * */
//    public boolean delete(Integer id);

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

    public boolean existSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

}
