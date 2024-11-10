package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Collect;
import com.example.test.service.impl.CollectServiceImpl;
import com.example.test.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Controller
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectServiceImpl collectService;

    /*
     * 新增收藏
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");
        String songId = request.getParameter("songId");

        if(songId==null || songId.equals("")){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "收藏歌曲不存在");
            return jsonObject;
        }
        if (collectService.existSongId(Integer.parseInt(userId),Integer.parseInt(songId))){
            if(collectService.deleteByUser(Integer.parseInt(userId),Integer.parseInt(songId))){
                jsonObject.put(Consts.CODE, 2);
                jsonObject.put(Consts.MSG, "取消收藏成功");
                return jsonObject;
            }
        }

        Collect collect = new Collect();
        collect.setUserId(Integer.parseInt(userId));
        collect.setSongId(Integer.parseInt(songId));

        boolean flag = collectService.insert(collect);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "收藏成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "收藏失败");
        return jsonObject;
    }

    //    获取所有收藏列表
    @RequestMapping(value = "/allCollect", method = RequestMethod.GET)
    public Object allCollect(){
        return collectService.allCollect();
    }

    //    查询某个用户收藏列表
    @RequestMapping(value = "/collectOfUserId", method = RequestMethod.GET)
    public Object collectOfUserId(HttpServletRequest request){
        String userId = request.getParameter("userId");
        return collectService.collectOfUserId(Integer.parseInt(userId));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSong (HttpServletRequest request){
        String songId = request.getParameter("songId");
        String userId = request.getParameter("userId");
        boolean flag = collectService.delete(Integer.parseInt(songId),Integer.parseInt(userId));
        return flag;
    }
}
