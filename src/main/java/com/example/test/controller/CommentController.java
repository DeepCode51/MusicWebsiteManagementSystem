package com.example.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.test.entity.Comment;
import com.example.test.entity.Updown;
import com.example.test.entity.User;
import com.example.test.service.CommentService;
import com.example.test.service.UpdownService;
import com.example.test.service.UserService;
import com.example.test.utils.Consts;
import com.example.test.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Controller
@RequestMapping("/comment")
public class CommentController {
    //    敏感词过滤
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UpdownService updownService;

    @Autowired
    private UserService userService;

    /*
     * 新增评论
     * */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object add(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String songListId = request.getParameter("songListId");
        String userId = request.getParameter("userId");
        String songId = request.getParameter("songId");
        String content = request.getParameter("content").trim();
        String type = request.getParameter("type");

        User user = userService.selectByPrimaryKey(Integer.parseInt(userId));
        Byte permission = user.getPermission();
        if(permission == 0){
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "您没有权限进行评论！");
            return jsonObject;
        }

        Comment comment = new Comment();
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if (new Byte(type) == 0) {
            comment.setSongId(Integer.parseInt(songId));
        } else {
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(sensitiveFilter.filter(content));

        boolean flag = commentService.insert(comment);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "评论成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "评论失败");
        return jsonObject;
    }

    /*
     * 修改评论
     * */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object update(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String songListId = request.getParameter("songListId");
        String userId = request.getParameter("userId").trim();
        String songId = request.getParameter("songId");
        String content = request.getParameter("content").trim();
        String type = request.getParameter("type").trim();

//        保存到评论对象中
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setUserId(Integer.parseInt(userId));
        comment.setType(new Byte(type));
        if (songId != null && songId.equals("")) {
            songId = null;
        } else {
            comment.setSongId(Integer.parseInt(songId));
        }
        if (songListId != null && songListId.equals("")) {
            songListId = null;
        } else {
            comment.setSongListId(Integer.parseInt(songListId));
        }
        comment.setContent(content);

        boolean flag = commentService.update(comment);
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
     * 修改评论显示
     * */
    @RequestMapping(value = "/updateIsShow", method = RequestMethod.POST)
    public Object updatePermission(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String isshow = request.getParameter("isshow").trim();

//        保存到用户对象中
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setIsshow(new Byte(isshow));

        boolean flag = commentService.update(comment);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "修改评论显示成功!");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "修改评论显示失败");
        return jsonObject;
    }

    //    获取所有评论列表
    @RequestMapping(value = "/allComment", method = RequestMethod.GET)
    public Object allComment() {
        return commentService.allComment();
    }

    //    获得指定歌曲ID的所有评论
    @RequestMapping(value = "/commentOfSongId", method = RequestMethod.GET)
    public Object commentOfSongId(HttpServletRequest request) {
        String songId = request.getParameter("songId");
        return commentService.commentOfSongId(Integer.parseInt(songId));
    }

    //    获得指定歌单ID的所有评论
    @RequestMapping(value = "/commentOfSongListId", method = RequestMethod.GET)
    public Object commentOfSongListId(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        return commentService.commentOfSongListId(Integer.parseInt(songListId));
    }

    //    删除评论
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteComment(HttpServletRequest request) {
        String id = request.getParameter("id").trim();
        boolean flag = commentService.delete(Integer.parseInt(id));
        return flag;
    }

    //    删除评论举报原因
    @RequestMapping(value = "/deleteDown", method = RequestMethod.GET)
    public Object deleteDown(HttpServletRequest request) {
        String id = request.getParameter("id").trim();
        String commentId = request.getParameter("commentId").trim();
        boolean flag = updownService.deleteById(Integer.parseInt(id));
        Integer down = updownService.updownNum(Integer.parseInt(commentId), (byte) 1);
        Comment comment = new Comment();
        comment.setDown(down);
        comment.setId(Integer.parseInt(commentId));
        commentService.update(comment);
        return flag;
    }

    /*
     * 点赞评论
     * */
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Object like(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String userId = request.getParameter("userId").trim();
        String optionType = request.getParameter("optionType").trim();

        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));

        if (updownService.existUpdown(Integer.parseInt(id), Integer.parseInt(userId), new Byte(optionType))) {
            updownService.delete(Integer.parseInt(id), Integer.parseInt(userId), new Byte(optionType));
            Integer up = updownService.updownNum(Integer.parseInt(id), new Byte(optionType));
            comment.setUp(up);
            commentService.update(comment);
            jsonObject.put("up", up);
            jsonObject.put(Consts.CODE, 2);
            return jsonObject;
//            jsonObject.put(Consts.MSG, "已经点赞该歌曲");
//            return jsonObject;
        }

        Updown updown = new Updown();
        updown.setCommentId(Integer.parseInt(id));
        updown.setUserId(Integer.parseInt(userId));
        updown.setOptionType(new Byte(optionType));


        if (updownService.insert(updown)) {
            Integer up = updownService.updownNum(Integer.parseInt(id), new Byte(optionType));
//            System.out.println(up);
            comment.setUp(up);
        }

        boolean flag = commentService.update(comment);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "点赞成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "点赞失败");
        return jsonObject;
    }

    /*
     * 查询用户点赞/举报
     * */
    @RequestMapping(value = "/updownOfUserId", method = RequestMethod.GET)
    public Object updownOfUserId(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        return updownService.updownOfUserId(Integer.parseInt(userId));
    }

    /*
     * 用户举报
     * */
    @RequestMapping(value = "/down", method = RequestMethod.POST)
    public Object down(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();
        String userId = request.getParameter("userId").trim();
        String optionType = request.getParameter("optionType").trim();
        String reason = request.getParameter("reason");
        Comment comment = new Comment();
        comment.setId(Integer.parseInt(id));
        comment.setIsshow((byte) 0);

        if (updownService.existUpdown(Integer.parseInt(id), Integer.parseInt(userId), new Byte(optionType))) {
//            updownService.delete(Integer.parseInt(id), Integer.parseInt(userId), new Byte(optionType));
//            Integer down = updownService.updownNum(Integer.parseInt(id), new Byte(optionType));
//            comment.setDown(down);
//            commentService.update(comment);
//            jsonObject.put("down", down);
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "您已经举报过了");
            return jsonObject;
        }

        Updown updown = new Updown();
        updown.setCommentId(Integer.parseInt(id));
        updown.setUserId(Integer.parseInt(userId));
        updown.setOptionType(new Byte(optionType));
        updown.setReason(reason);


        if (updownService.insert(updown)) {
            Integer down = updownService.updownNum(Integer.parseInt(id), new Byte(optionType));
//            System.out.println(up);
            comment.setDown(down);
        }

        boolean flag = commentService.update(comment);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "举报成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "举报失败");
        return jsonObject;
    }

    /*
     * 查询用户举报原因
     * */
    @RequestMapping(value = "/updownOfComment", method = RequestMethod.GET)
    public Object updownOfComment(HttpServletRequest request) {
        String commentId = request.getParameter("commentId");
        String optionType = request.getParameter("optionType");
        return updownService.updownOfCommentId(Integer.parseInt(commentId),new Byte(optionType));
    }
}
