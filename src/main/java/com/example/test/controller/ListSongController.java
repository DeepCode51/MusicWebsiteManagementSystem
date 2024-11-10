package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.ListSong;
import com.example.test.service.ListSongService;
import com.example.test.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/*
 * 歌单管理控制类
 * */
@RestController
@RequestMapping("/listSong")
public class ListSongController {

    @Autowired
    private ListSongService listSongService;

    /*
     * 给歌单添加歌曲
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addListSong(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String songId = request.getParameter("songId").trim(); //歌曲id
        String songListId = request.getParameter("songListId").trim();  //歌单id
        System.out.println(listSongService.existSongId(Integer.parseInt(songId),Integer.parseInt(songListId)));
        if (listSongService.existSongId(Integer.parseInt(songId),Integer.parseInt(songListId))){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌曲已存在");
            return jsonObject;
        }
        ListSong listSong = new ListSong();
        listSong.setSongId(Integer.parseInt(songId));
        listSong.setSongListId(Integer.parseInt(songListId));
        boolean flag = listSongService.insert(listSong);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "添加成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "添加失败");
        return jsonObject;

    }

    /*
     * 返回指定歌单ID的歌曲
     * */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object listSongOfSongId(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        return listSongService.listSongOfSongId(Integer.parseInt(songListId));
    }

    /*
     * 删除歌单歌曲
     * */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteListSong(HttpServletRequest request) {
        String songId = request.getParameter("songId").trim();
        String songListId = request.getParameter("songListId").trim();
        boolean flag = listSongService.deleteByIds(Integer.parseInt(songId),Integer.parseInt(songListId));
        return flag;
    }
}
