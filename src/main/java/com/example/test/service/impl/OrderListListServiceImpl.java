package com.example.test.service.impl;

import com.example.test.dao.OrderListMapper;
import com.example.test.entity.OrderList;
import com.example.test.service.OrderListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderListListServiceImpl implements OrderListService {
    @Autowired
    private OrderListMapper orderListMapper;

    @Override
    public boolean insert(OrderList orderList) {
        return orderListMapper.insert(orderList)>0;
    }

    @Override
    public OrderList selectByPrimaryKey(Integer id) {
        return orderListMapper.selectByPrimaryKey(id);
    }


    @Override
    public boolean update(OrderList orderList) {
        return orderListMapper.update(orderList)>0;
    }

//    @Override
//    public boolean delete(Integer id) {
//        return orderMapper.delete(id)>0;
//    }

    @Override
    public List<OrderList> allOrder() {
        return orderListMapper.allOrder();
    }

    @Override
    public List<OrderList> orderOfUserId(Integer userId) {
        return orderListMapper.orderOfUserId(userId);
    }

    @Override
        public boolean existSongId(Integer userId, Integer songId) {
        return orderListMapper.existSongId(userId,songId)>0;
    }
}
