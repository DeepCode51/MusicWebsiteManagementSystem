package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Singer;
import com.example.test.service.SingerService;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/*
 * 歌手控制类
 * */
@RestController
@RequestMapping("/singer")
public class SingerController {

    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;

    /*
     * 增加歌手
     * */
    @RequestMapping(value = "/addSinger", method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name").trim();
        String sex = request.getParameter("sex").trim();
        String pic = request.getParameter("pic").trim();
        String birth = request.getParameter("birth").trim();
        String location = request.getParameter("location").trim();
        String introduction = request.getParameter("introduction").trim();
//        把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        判断歌手是否存在
        if (singerService.existSinger(name)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "已存在同名歌手!");
            return jsonObject;
        }

//        保存到歌手对象中
        Singer singer = new Singer();
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        boolean flag = singerService.insert(singer);
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
     * 修改歌手
     * */
    @RequestMapping(value = "/updateSinger", method = RequestMethod.POST)
    public Object updateSinger(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String name = request.getParameter("name").trim();
        String sex = request.getParameter("sex").trim();
        String birth = request.getParameter("birth").trim();
        String location = request.getParameter("location").trim();
        String introduction = request.getParameter("introduction").trim();
//        把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String singName = singerService.selectByPrimaryKey(Integer.parseInt(id)).getName();
        //        判断歌手是否存在
        if (singerService.existSinger(name) && !Objects.equals(singName, name)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "已存在同名歌手!");
            return jsonObject;
        }

//        保存到歌手对象中
        Singer singer = new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        boolean flag = singerService.update(singer);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "修改失败");
        return jsonObject;
    }

    /*
     * 删除歌手
     * */
    @RequestMapping(value = "/deleteSinger", method = RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest request) {
        String id = request.getParameter("id").trim();
        delPicUrl(Integer.parseInt(id), null);
//        System.out.println(id);
        boolean flag = singerService.delete(Integer.parseInt(id));
        return flag;
    }

    /*
     * 根据主键查询
     * */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim();
        return singerService.selectByPrimaryKey(Integer.parseInt(id));
    }

    /*
     * 查询所有歌手
     * */
    @RequestMapping(value = "/allSinger", method = RequestMethod.GET)
    public Object allSinger(HttpServletRequest request) {
        return singerService.allSinger();
    }

    /*
     * 根据歌手名模糊查询
     * */
    @RequestMapping(value = "/singerOfName", method = RequestMethod.GET)
    public Object singerOfName(HttpServletRequest request) {
        String name = request.getParameter("name").trim();
        return singerService.singerOfName("%" + name + "%");
    }

    /*
     * 根据性别查询
     * */
    @RequestMapping(value = "/singerOfSex", method = RequestMethod.GET)
    public Object singerOfSex(HttpServletRequest request) {
        String sex = request.getParameter("sex").trim();
        return singerService.singerOfSex(Integer.parseInt(sex));
    }

    /*
     * 更新歌手图片
     * */
    @RequestMapping(value = "/updateSingerPic", method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败！");
            return jsonObject;
        }
//        文件名=当前时间到毫秒+原来文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "img"
                + System.getProperty("file.separator") + "singerPic";
//        如果文件路径不存在，则新增路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
//        实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
//        存储到数据库内的相对文件地址
        String storeAvatorPath = "/img/singerPic/" + fileName;
        delPicUrl(id, storeAvatorPath);
        try {
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean flag = singerService.update(singer);
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
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "上传失败" + e.getMessage());
            return jsonObject;
        } finally {
            return jsonObject;
        }
    }

    public void delPicUrl(int id, String storeAvatorPath) {
        Singer singer = singerService.selectByPrimaryKey(id);
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        String pic = singer.getPic();
//        System.out.println(!pic.equals("/img/singerPic/default.jpeg"));
        if (!pic.equals("/img/singerPic/default.jpeg") && !pic.equals(storeAvatorPath)) {
            File picfile = new File(filePath + pic);
            System.out.println(picfile.delete());
        }
    }
}
