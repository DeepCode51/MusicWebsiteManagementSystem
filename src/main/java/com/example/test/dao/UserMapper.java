package com.example.test.dao;

import com.example.test.entity.Singer;
import com.example.test.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* 用户Dao
* */
@Repository
public interface UserMapper {
    /*
    * 增加
    * */
    public int insert(User user);

    /*
    * 修改
    * */
    public int update(User user);
    public int updatePass(User user);
    public int updatePermission(User user);

    /*
     * 删除
     * */
    public int delete(Integer id);

    /*
     * 根据主键查询
     * */
    public User selectByPrimaryKey(Integer id);

    /*
     * 查询所有用户
     * */
    public List<User> allUser();

    /*
    * 验证密码
    * */
    public int verifyPassword(String username,String password);

    /*
    * 根据账号查询
    * */
    public User getByUsername(String username);

}
