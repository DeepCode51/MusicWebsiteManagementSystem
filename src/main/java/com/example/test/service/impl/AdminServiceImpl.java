package com.example.test.service.impl;

import com.example.test.dao.AdminMapper;
import com.example.test.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 管理员service实现类
* */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    /*
    * 验证密码是否正确
    *
    * @param name
    * @param password
    * */
    @Override
    public boolean verifyPassword(String name, String password) {
        return adminMapper.verifyPassword(name, password)>0;
    }
}
