package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Song;
import com.example.test.service.SongService;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;

/*
 * 歌曲控制类
 * */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongService songService;

    /*
     * 添加歌曲
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSong(HttpServletRequest request, @RequestParam("file") MultipartFile mpfile) {
        JSONObject jsonObject = new JSONObject();
        String singerId = request.getParameter("singerId").trim(); //歌手id
        String name = request.getParameter("name").trim();  //歌名
        String introduction = request.getParameter("introduction").trim();  //介绍
        String pic = "/img/songPic/default.jpeg";  //图片
        String lyric = request.getParameter("lyric").trim();
        String price = request.getParameter("price");
        String theme = request.getParameter("theme");

        //        判断歌曲是否存在
        if (songService.existSong(name,Integer.parseInt(singerId))){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "该歌手存在同名歌曲!");
            return jsonObject;
        }

        if (mpfile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "音乐上传失败！");
            return jsonObject;
        }
        String fileName = System.currentTimeMillis() + mpfile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }

        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
        String storeUrlPath = "/song/" + fileName;
        try {
            mpfile.transferTo(dest);
            Song song = new Song();
            song.setSingerId(Integer.parseInt(singerId));
            song.setName(name);
            song.setIntroduction(introduction);
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            song.setPic(pic);
            song.setLyric(lyric);
            song.setPrice(price);
            song.setTheme(theme);
            song.setUrl(storeUrlPath);
            boolean flag = songService.insert(song);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put("avator", storeUrlPath);
                jsonObject.put(Consts.MSG, "上传成功");
                return jsonObject;
            } else {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }

    /*
    * 返回指定歌手ID的歌曲
    * */
    @RequestMapping(value = "/singer/detail", method = RequestMethod.GET)
    public Object songOfSingerId(HttpServletRequest request){
        String singerId = request.getParameter("singerId");
        return songService.songOfSingerId(Integer.parseInt(singerId));
    }

    //    返回所有歌曲
    @RequestMapping(value = "/allSong", method = RequestMethod.GET)
    public Object allSong(){
        return songService.allSong();
    }

    /*
    * 更新歌曲相关内容
    * */
    @RequestMapping(value = "/updateSong", method = RequestMethod.POST)
    public Object updateSong(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String singerId = request.getParameter("singerId").trim();
        String name = request.getParameter("name").trim();
        String introduction = request.getParameter("introduction").trim();
        String lyric = request.getParameter("lyric").trim();
        String price = request.getParameter("price");
        String theme = request.getParameter("theme");

        String songName = songService.selectByPrimaryKey(Integer.parseInt(id)).getName();

        //        判断歌曲是否存在
        if (songService.existSong(name,Integer.parseInt(singerId)) && !Objects.equals(songName,name)){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "该歌手存在同名歌曲!");
            return jsonObject;
        }

        Song song = new Song();
        song.setId(Integer.parseInt(id));
//        song.setSingerId(Integer.parseInt(singer_id));
        song.setName(name);
        song.setIntroduction(introduction);
        song.setLyric(lyric);
        song.setPrice(price);
        song.setTheme(theme);

        boolean res = songService.update(song);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "修改成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }

    /*
     * 更新歌曲点击次数
     * */
    @RequestMapping(value = "/updateClick", method = RequestMethod.POST)
    public Object updateClick(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String clickTimes = request.getParameter("clickTimes");

        Song song = new Song();
        song.setId(Integer.parseInt(id));
        song.setClickTimes(Integer.parseInt(clickTimes));

        boolean res = songService.updateClick(song);
        if (res){
            jsonObject.put("code", 1);
            jsonObject.put("msg", "更新成功");
            return jsonObject;
        }else {
            jsonObject.put("code", 0);
            jsonObject.put("msg", "更新失败");
            return jsonObject;
        }
    }


    /*
     * 更新歌曲图片
     * */
    @RequestMapping(value = "/updateSongPic", method = RequestMethod.POST)
    public Object updateSongPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败！");
            return jsonObject;
        }

//        文件名=当前时间到毫秒+原来文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "songPic";
//        如果文件路径不存在，则新增路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
//        实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
//        存储到数据库内的相对文件地址
        String storeAvatorPath = "/img/songPic/" + fileName;
        delPicUrl(id,storeAvatorPath);
        try {
            avatorFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setPic(storeAvatorPath);
            boolean flag = songService.update(song);
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
     * 更新歌曲
     * */
    @RequestMapping(value = "/updateSongUrl", method = RequestMethod.POST)
    public Object updateSongUrl(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败！");
            return jsonObject;
        }

//        删除原歌曲文件
        delSongUrl(id);

//        文件名=当前时间到毫秒+原来文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "song";
//        如果文件路径不存在，则新增路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
//        实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
//        存储到数据库内的相对文件地址
        String storeAvatorPath = "/song/" + fileName;
        try {
            avatorFile.transferTo(dest);
            Song song = new Song();
            song.setId(id);
            song.setUrl(storeAvatorPath);
            boolean flag = songService.update(song);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put("avator", storeAvatorPath);
                jsonObject.put(Consts.MSG, "上传成功");
                return jsonObject;
            } else {
                jsonObject.put(Consts.CODE, 0);
                jsonObject.put(Consts.MSG, "上传失败");
                return jsonObject;
            }
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }

    /*
    * 删除歌曲
    * */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSong (HttpServletRequest request){
//        -TODO 先查询到数据库中对应的文件地址，再删除掉
        String id = request.getParameter("id");
        delSongUrl(Integer.parseInt(id));
        delPicUrl(Integer.parseInt(id),null);
        boolean flag = songService.delete(Integer.parseInt(id));
        return flag;
    }

    /*
    * 根据歌手ID删除歌曲
    * */
    @RequestMapping(value = "/deleteBySingerId", method = RequestMethod.GET)
    public Object deleteBySingerId (HttpServletRequest request){
//        -TODO 先查询到数据库中对应的文件地址，再删除掉
        String singerId = request.getParameter("singerId");
        List<Song> song = songService.songOfSingerId(Integer.parseInt(singerId));
        boolean flag = false;
        for (int i = 0; i < song.size(); i++) {
            delSongUrl(song.get(i).getId());
            delPicUrl(song.get(i).getId(),null);
            songService.delete(song.get(i).getId());
            flag = true;
        }
//        delSongUrl(Integer.parseInt(id));
//        delPicUrl(Integer.parseInt(id),null);

        return flag;
    }

    /*
     * 根据歌曲id查询歌曲对象
     * */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request) {
        String songId = request.getParameter("songId");
        return songService.selectByPrimaryKey(Integer.parseInt(songId));
    }

    /*
     * 根据歌曲名精确查询歌曲对象
     * */
    @RequestMapping(value = "/songOfSongName", method = RequestMethod.GET)
    public Object songOfSongName(HttpServletRequest request) {
        String songName = request.getParameter("songName");
        return songService.songOfName(songName);
    }

    /*
     * 根据歌曲名模糊查询歌曲对象
     * */
    @RequestMapping(value = "/likeSongName", method = RequestMethod.GET)
    public Object likeSongOfName(HttpServletRequest request) {
        String songName = request.getParameter("songName");
        return songService.likeSongOfName(songName);
    }

    public void delPicUrl(int id,String storeAvatorPath){
        Song song = songService.selectByPrimaryKey(id);
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        String pic = song.getPic();
        System.out.println(!pic.equals("/img/songPic/default.jpeg"));
        if (!pic.equals("/img/songPic/default.jpeg") && !pic.equals(storeAvatorPath)){
            File picfile = new File(filePath + pic);
            System.out.println(picfile.delete());
        }
    }

    public void delSongUrl(int id){
        Song song = songService.selectByPrimaryKey(id);
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        File dest = new File(filePath + song.getUrl());
        System.out.println(dest.delete());
    }
}
