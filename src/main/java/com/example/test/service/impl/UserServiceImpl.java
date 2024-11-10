package com.example.test.service.impl;

import com.example.test.dao.UserMapper;
import com.example.test.entity.Singer;
import com.example.test.entity.User;
import com.example.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* service实现类
* */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean insert(User user) {
        return userMapper.insert(user)>0;
    }

    @Override
    public boolean update(User user) {
        return userMapper.update(user)>0;
    }

//    @Override
//    public boolean updatePass(User user) {
//        return userMapper.updatePass(user)>0;
//    }
//
//    @Override
//    public boolean updatePermission(User user) {
//        return userMapper.updatePermission(user)>0;
//    }

    @Override
    public boolean delete(Integer id) {
        return userMapper.delete(id)>0;
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<User> allUser() {
        return userMapper.allUser();
    }

    @Override
    public boolean verifyPassword(String username, String password) {
        return userMapper.verifyPassword(username,password)>0;
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }
}
