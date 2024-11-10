package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Ranks;
import com.example.test.entity.User;
import com.example.test.service.RanksService;
import com.example.test.service.impl.RanksServiceImpl;
import com.example.test.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Controller
@RequestMapping("/ranks")
public class RanksController {

    @Autowired
    private RanksServiceImpl ranksService;

    /*
    * 新增评分
    * */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object add(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String songListId = request.getParameter("songListId");
        String userId = request.getParameter("userId");
        String score = request.getParameter("score");
        Ranks ranks = new Ranks();
        ranks.setSongListId(Integer.parseInt(songListId));
        ranks.setUserId(Integer.parseInt(userId));
        ranks.setScore(Integer.parseInt(score));

        boolean flag = ranksService.insert(ranks);
        if (flag) {
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"评分成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"评分失败");
        return jsonObject;
    }

    /*
    * 计算平均分
    * */
    @RequestMapping(value = "/score",method = RequestMethod.GET)
    public Object score(HttpServletRequest request){
        String songListId = request.getParameter("songListId");
        return ranksService.ranksofSongListId(Integer.parseInt(songListId));
    }
}
