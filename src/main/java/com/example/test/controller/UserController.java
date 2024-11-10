package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Singer;
import com.example.test.entity.User;
import com.example.test.service.UserService;
import com.example.test.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 用户控制类
 * */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*
     * 增加用户
     * */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Object addUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String sex = request.getParameter("sex").trim();
        String phoneNum = request.getParameter("phoneNum").trim();
        String email = request.getParameter("email").trim();
        String birth = request.getParameter("birth").trim();
        String introduction = request.getParameter("introduction").trim();
        String location = request.getParameter("location").trim();
        String avator = request.getParameter("avator").trim();

        if (username.equals("") || username == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名不能为空");
            return jsonObject;
        }

        User user1 = userService.getByUsername(username);
        if (user1 != null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名已存在");
            return jsonObject;
        }

        if (password.equals("") || password == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "密码不能为空");
            return jsonObject;
        }

//        把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        保存到用户对象中
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setBirth(birthDate);
        user.setIntroduction(introduction);
        user.setLocation(location);
        user.setAvator(avator);

        boolean flag = userService.insert(user);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "注册成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "注册失败");
        return jsonObject;
    }

    /*
     * 修改用户
     * */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public Object updateUser(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
//        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();
        String sex = request.getParameter("sex").trim();
        String phoneNum = request.getParameter("phoneNum").trim();
        String email = request.getParameter("email").trim();
        String birth = request.getParameter("birth").trim();
        String introduction = request.getParameter("introduction").trim();
        String location = request.getParameter("location").trim();

//        把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        保存到用户对象中
        User user = new User();
        user.setId(Integer.parseInt(id));
//        user.setUsername(username);
        user.setPassword(password);
        user.setSex(new Byte(sex));
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setBirth(birthDate);
        user.setIntroduction(introduction);
        user.setLocation(location);

        boolean flag = userService.update(user);
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
    * 用户修改密码
    * */
    @RequestMapping(value = "/updatePass", method = RequestMethod.POST)
    public Object updatePass(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String password = request.getParameter("password").trim();

//        保存到用户对象中
        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setPassword(password);

        boolean flag = userService.update(user);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改密码成功,下次登录系统生效");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "修改失败");
        return jsonObject;
    }

    /*
    * 修改用户权限
    * */
    @RequestMapping(value = "/updatePermission", method = RequestMethod.POST)
    public Object updatePermission(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String permission = request.getParameter("permission").trim();

//        保存到用户对象中
        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setPermission(new Byte(permission));

        boolean flag = userService.update(user);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改权限成功!");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "修改失败");
        return jsonObject;
    }
    /*
     * 删除用户
     * */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public Object deleteUser(HttpServletRequest request) {
        String id = request.getParameter("id").trim();
        delPicUrl(Integer.parseInt(id),null);
        boolean flag = userService.delete(Integer.parseInt(id));
        return flag;
    }

    /*
     * 根据主键查询
     * */
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String id = request.getParameter("id").trim();
        return userService.selectByPrimaryKey(Integer.parseInt(id));
    }

    /*
     * 查询所有用户
     * */
    @RequestMapping(value = "/allUser", method = RequestMethod.GET)
    public Object allUser(HttpServletRequest request) {
        return userService.allUser();
    }

    /*
     * 更新用户图片
     * */
    @RequestMapping(value = "/updateUserPic", method = RequestMethod.POST)
    public Object updateUserPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败！");
            return jsonObject;
        }
//        文件名=当前时间到毫秒+原来文件名
        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "userImages";
//        如果文件路径不存在，则新增路径
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
//        实际的文件地址
        File dest = new File(filePath + System.getProperty("file.separator") + fileName);
//        存储到数据库内的相对文件地址
        String storeAvatorPath = "/userImages/" + fileName;
        delPicUrl(id,storeAvatorPath);
        try {
            avatorFile.transferTo(dest);
            User user = new User();
            user.setId(id);
            user.setAvator(storeAvatorPath);
            boolean flag = userService.update(user);
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
     * 用户登录
     * */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object loginStatus(HttpServletRequest request, HttpSession session) {

        JSONObject jsonObject = new JSONObject();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean res = userService.verifyPassword(username, password);

        if (res) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "登录成功");
            jsonObject.put("userMsg", userService.getByUsername(username));
            session.setAttribute("username", username);
            return jsonObject;
        } else {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "用户名或密码错误");
            return jsonObject;
        }
    }

    /*
     * 返回指定用户ID的信息
     * */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request){
        String id = request.getParameter("id");
        return userService.selectByPrimaryKey(Integer.parseInt(id));
    }

    public void delPicUrl(int id,String storeAvatorPath){
        User user = userService.selectByPrimaryKey(id);
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        String pic = user.getAvator();
//        System.out.println(!pic.equals("/img/singerPic/default.jpeg"));
        if (!pic.equals("/img/user.jpeg") && !pic.equals(storeAvatorPath)){
            File picfile = new File(filePath + pic);
            System.out.println(picfile.delete());
        }
    }
}
