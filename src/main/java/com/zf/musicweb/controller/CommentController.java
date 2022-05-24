package com.zf.musicweb.controller;import com.alibaba.fastjson.JSONObject;import com.zf.musicweb.entity.Comment;import com.zf.musicweb.service.CommentService;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PostMapping;import org.springframework.web.bind.annotation.RestController;import javax.annotation.Resource;import javax.servlet.http.HttpServletRequest;import java.util.*;/** * <p> *  前端控制器 * </p> * * @author ${author} * @since 2022-01-05 */@RestControllerpublic class CommentController {    @Resource    private CommentService commentService;    //提交评论    @PostMapping("/comment/add")    public Object addComment(HttpServletRequest req){        JSONObject jsonObject=new JSONObject();        String user_id = req.getParameter("userId");        String type = req.getParameter("type");        String song_list_id=req.getParameter("songListId");        String song_id=req.getParameter("songId");        String content = req.getParameter("content").trim();        Comment comment1=new Comment();        comment1.setUserId(Integer.parseInt(user_id));        comment1.setType(new Byte(type));        //如果类型为0，为歌曲评论。类型为1，为歌单评论        if(new Byte(type)==0){            comment1.setSongId(Integer.parseInt(song_id));        }else if(new Byte(type)==1){            comment1.setSongListId(Integer.parseInt(song_list_id));        }        comment1.setContent(content);        comment1.setCreateTime(new Date());        boolean res=commentService.save(comment1);        if(res){            jsonObject.put("code",1);            jsonObject.put("msg","评论成功");            return jsonObject;        }else {            jsonObject.put("code",0);            jsonObject.put("msg","评论失败");            return jsonObject;        }    }    //获取全部评论    @GetMapping("/comment")    public Object allComment(){        List<Comment> comments=commentService.list();        return comments;    }    //获取指定歌曲ID的所有评论    @GetMapping("/comment/song/detail")    public Object commentOfSongId(HttpServletRequest req){        String songId=req.getParameter("songId");        Map<String,Object> id=new HashMap<>();        id.put("song_id",Integer.parseInt(songId));        List<Comment> comments=commentService.listByMap(id);        return comments;    }    //获取指定歌单ID的所有评论    @GetMapping("/comment/songList/detail")    public Object commentOfSongListId(HttpServletRequest req){        String songListId=req.getParameter("songListId");        Map<String,Object> id=new HashMap<>();        id.put("song_list_id",Integer.parseInt(songListId));        List<Comment> comments=commentService.listByMap(id);        return comments;    }    //点赞    @PostMapping("/comment/like")    public Object commentLike(HttpServletRequest req){        JSONObject jsonObject = new JSONObject();        String id = req.getParameter("id").trim();        String up = req.getParameter("up").trim();        Comment comment = new Comment();        comment.setId(Integer.parseInt(id));        comment.setUp(Integer.parseInt(up));        boolean res = commentService.updateById(comment);        if(res){            jsonObject.put("code", 1);            jsonObject.put("msg", "点赞成功");            return jsonObject;        }else {            jsonObject.put("code", 0);            jsonObject.put("msg", "点赞失败");            return jsonObject;        }    }    //删除评论    @GetMapping("/comment/delete")    public Object deleteComment(HttpServletRequest req){        String id=req.getParameter("id");        return commentService.removeById(Integer.parseInt(id));    }    //更新评论    @PostMapping("/comment/update")    public Object updateCommentMsg(HttpServletRequest req){        JSONObject jsonObject = new JSONObject();        String id = req.getParameter("id").trim();        String user_id = req.getParameter("userId").trim();        String song_id = req.getParameter("songId").trim();        String song_list_id = req.getParameter("songListId").trim();        String content = req.getParameter("content").trim();        String type = req.getParameter("type").trim();        String up = req.getParameter("up").trim();        Comment comment = new Comment();        comment.setId(Integer.parseInt(id));        comment.setUserId(Integer.parseInt(user_id));        if(song_id==""){            comment.setSongId(null);        }else {            comment.setSongId(Integer.parseInt(song_id));        }        if(song_list_id==""){            comment.setSongListId(null);        }else {            comment.setSongListId(Integer.parseInt(song_list_id));        }        comment.setContent(content);        comment.setType(new Byte(type));        comment.setUp(Integer.parseInt(up));        boolean res=commentService.updateById(comment);        if (res){            jsonObject.put("code", 1);            jsonObject.put("msg", "修改成功");            return jsonObject;        }else {            jsonObject.put("code", 0);            jsonObject.put("msg", "修改失败");            return jsonObject;        }    }}