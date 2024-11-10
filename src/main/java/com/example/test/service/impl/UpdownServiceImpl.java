package com.example.test.service.impl;

import com.example.test.dao.UpdownMapper;
import com.example.test.entity.Updown;
import com.example.test.service.UpdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdownServiceImpl implements UpdownService {
    @Autowired
    private UpdownMapper updownMapper;
    @Override
    public boolean insert(Updown updown) {
        return updownMapper.insert(updown)>0;
    }

    @Override
    public boolean delete(Integer commentId, Integer userId,Byte optionType) {
        return updownMapper.delete(commentId,userId,optionType)>0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return updownMapper.deleteById(id)>0;
    }

    @Override
    public List<Updown> updownOfCommentId(Integer commentId,Byte optionType) {
        return updownMapper.updownOfCommentId(commentId,optionType);
    }

    @Override
    public boolean existUpdown(Integer commentId, Integer userId, Byte optionType) {
        return updownMapper.existUpdown(commentId,userId,optionType)>0;
    }

    @Override
    public int updownNum(Integer commentId, Byte optionType) {
        return updownMapper.updownNum(commentId,optionType);
    }

    @Override
    public List<Updown> updownOfUserId(Integer userId) {
        return updownMapper.updownOfUserId(userId);
    }


}
