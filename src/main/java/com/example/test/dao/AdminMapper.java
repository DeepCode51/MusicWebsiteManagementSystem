package com.example.test.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
/*
* 管理员Dao
* */
@Repository
public interface AdminMapper {
    /*
    验证密码是否正确
    */
    public int verifyPassword(@Param("name")String name,@Param("password") String password);
}
