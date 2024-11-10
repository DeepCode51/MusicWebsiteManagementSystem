package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.OrderList;
import com.example.test.entity.Song;
import com.example.test.service.OrderListService;
import com.example.test.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Controller
@RequestMapping("/orderList")
public class OrderController {
    @Autowired
    private OrderListService orderListService;

    //生成编号
    private static String date;
    private static long orderNum = 0L;
    public static synchronized String getOrderNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        if(date == null || date.equals(str)) {
            date = str;
            orderNum = 0L;
        }
        orderNum++;
        long orderNO = Long.parseLong(date)*1000;
        orderNO += orderNum;
        return orderNO+"";
    }

    /*
     * 新增订单
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");
        String songId = request.getParameter("songId");
        String ispay = request.getParameter("ispay");
        String ostatus = request.getParameter("ostatus");

        if(songId==null || songId.equals("")){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "付费歌曲不存在");
            return jsonObject;
        }
        if (orderListService.existSongId(Integer.parseInt(userId),Integer.parseInt(songId))){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "已经生成过订单");
            return jsonObject;
        }

        OrderList orderList = new OrderList();
        orderList.setUserId(Integer.parseInt(userId));
        orderList.setSongId(Integer.parseInt(songId));
        orderList.setCode(getOrderNo());
        orderList.setIspay(new Byte(ispay));
        orderList.setOstatus(new Byte(ostatus));

        boolean flag = orderListService.insert(orderList);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "订单生成成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "订单生成失败");
        return jsonObject;
    }

//    /*
//    * 取消订单
//    * */
//    @RequestMapping(value = "/delete", method = RequestMethod.GET)
//    public Object delete(HttpServletRequest request) {
//        String id = request.getParameter("id").trim();
//        String ispay = request.getParameter("ispay").trim();
//        System.out.println(ispay);
//        boolean flag = orderService.delete(Integer.parseInt(id));
//        return flag;
//    }

    /*
    * 更新订单
    * */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object update(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String ispay = request.getParameter("ispay");
        String ostatus = request.getParameter("ostatus");

        OrderList order = new OrderList();
        order.setId(Integer.parseInt(id));
        order.setIspay(new Byte(ispay));
        order.setOstatus(new Byte(ostatus));

        boolean flag = orderListService.update(order);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "支付成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "支付失败");
        return jsonObject;
    }

    //    查询某个用户的订单
    @RequestMapping(value = "/ByUserId", method = RequestMethod.GET)
    public Object orderOfUserId(HttpServletRequest request){
        String userId = request.getParameter("userId");
        return orderListService.orderOfUserId(Integer.parseInt(userId));
    }

    /*
     * 根据主键查询订单
     * */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request) {
        String id = request.getParameter("id");
        return orderListService.selectByPrimaryKey(Integer.parseInt(id));
    }

    //    获取所有订单
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Object allOrder(){
        return orderListService.allOrder();
    }


    /*
     * 更新歌曲
     * */
    @RequestMapping(value = "/sendSongUrl", method = RequestMethod.POST)
    public Object sendSongUrl(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id,@RequestParam("ispay") byte ispay,@RequestParam("ostatus") byte ostatus) {
        JSONObject jsonObject = new JSONObject();

        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "文件上传失败！");
            return jsonObject;
        }

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
//        delSongUrl(id,storeAvatorPath);
        try {
            avatorFile.transferTo(dest);
            OrderList orderList = new OrderList();
            orderList.setId(id);
            orderList.setIspay(new Byte(ispay));
            orderList.setOstatus(new Byte(ostatus));
            orderList.setUrl(storeAvatorPath);
            boolean flag = orderListService.update(orderList);
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

    public void delSongUrl(int id,String storeAvatorPath){
        OrderList orderList = orderListService.selectByPrimaryKey(id);
        String filePath = System.getProperty("user.dir") + System.getProperty("file.separator");
        String song = orderList.getUrl();
        if (!song.equals(storeAvatorPath) && !song.equals("")){
            File dest = new File(filePath + song);
            System.out.println(dest.delete());
        }
    }

}

