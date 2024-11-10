package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Song;
import com.example.test.entity.SongList;
import com.example.test.service.SongListService;
import com.example.test.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/*
 * 歌单控制类
 * */
@RestController
@RequestMapping("/songList")
public class SongListController {

    @Autowired
    private SongListService songListService;

    /*
     * 添加歌单
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSongList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String title = request.getParameter("title").trim();
        String pic = request.getParameter("pic").trim();
        String introduction = request.getParameter("introduction").trim();
        String style = request.getParameter("style").trim();

        //        判断同名歌单是否存在
        if (songListService.existSongList(title)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "已存在同名歌单!");
            return jsonObject;
        }

//        保存到歌单对象中
        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        boolean flag = songListService.insert(songList);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "上传成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "上传失败");
        return jsonObject;
    }

    /*
     * 查询所有歌单
     * */
    @RequestMapping(value = "/allSongList", method = RequestMethod.GET)
    public Object allSongList() {
        return songListService.allSongList();
    }

    //    返回指定标题对应的歌单
    @RequestMapping(value = "/songListofTitle", method = RequestMethod.GET)
    public Object songListOfTitle(HttpServletRequest request) {
        String title = request.getParameter("title").trim();
        return songListService.songListOfTitle(title);
    }

    //    返回标题包含文字的歌单
    @RequestMapping(value = "/likeTitle", method = RequestMethod.GET)
    public Object songListOfLikeTitle(HttpServletRequest request) {
        String title = request.getParameter("title").trim();
        return songListService.likeTitle('%' + title + '%');
    }

    //    返回指定类型的歌单
    @RequestMapping(value = "/likeStyle", method = RequestMethod.GET)
    public Object songListOfStyle(HttpServletRequest request) {
        String style = request.getParameter("style").trim();
        return songListService.likeStyle('%' + style + '%');
    }

    /*
     * 更新歌单
     * */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSongList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String title = request.getParameter("title").trim();
        String introduction = request.getParameter("introduction").trim();
        String style = request.getParameter("style").trim();

        String listTitle = songListService.selectByPrimaryKey(Integer.parseInt(id)).getTitle();

        //        判断同名歌单是否存在
        if (songListService.existSongList(title) && !Objects.equals(listTitle, title)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "已存在同名歌单!");
            return jsonObject;
        }

        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        boolean res = songListService.update(songList);
        if (res) {
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
            return jsonObject;
        } else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }

    /*
     * 更新歌单图片
     * */
    @RequestMapping(value = "/updateSongListPic", method = RequestMethod.POST)
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败！");
            return jsonObject;
        }
//        文件名=当前时间到毫秒+原来文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songListPic";
//        如果文件路径不存在，则新增路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
//        实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
//        存储到数据库内的相对文件地址
        String storeAvatorPath = "/img/songListPic/" + fileName;
        delPicUrl(id,fileName);
        try {
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storeAvatorPath);
            boolean flag = songListService.update(songList);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put("pic", storeAvatorPath);
                jsonObject.put(Consts.MSG, "上传成功");
                return jsonObject;
            } else {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }

    /*
     * 删除歌单
     * */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSongList(HttpServletRequest request) {
        String id = request.getParameter("id");
        delPicUrl(Integer.parseInt(id),null);
        boolean flag = songListService.delete(Integer.parseInt(id));
        return flag;
    }

    /*
     * 返回指定歌单ID的信息
     * */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        return songListService.selectByPrimaryKey(Integer.parseInt(songListId));
    }

    //    删除图片
    public void delPicUrl(int id, String storeAvatorPath) {
        SongList songList = songListService.selectByPrimaryKey(id);
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        String pic = songList.getPic();
        if (!pic.equals("/img/songListPic/list.jpeg") && !pic.equals(storeAvatorPath)) {
            File picfile = new File(filePath + pic);
            System.out.println(picfile.delete());
        }
    }
}
