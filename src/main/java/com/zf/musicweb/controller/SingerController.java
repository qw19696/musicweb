package com.zf.musicweb.controller;import com.alibaba.fastjson.JSONObject;import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;import com.zf.musicweb.entity.Consumer;import com.zf.musicweb.entity.Singer;import com.zf.musicweb.service.SingerService;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.boot.system.ApplicationHome;import org.springframework.data.redis.core.RedisTemplate;import org.springframework.util.ResourceUtils;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import javax.annotation.Resource;import javax.servlet.http.HttpServletRequest;import java.io.File;import java.io.FileNotFoundException;import java.io.IOException;import java.text.DateFormat;import java.text.ParseException;import java.text.SimpleDateFormat;import java.util.Date;import java.util.HashMap;import java.util.List;import java.util.Map;/** * <p> *  前端控制器 * </p> * * @author ${author} * @since 2022-01-05 */@RestControllerpublic class SingerController {    @Resource    private SingerService singerService;    @Autowired    private RedisTemplate redisTemplate;    //返回所有歌手    @GetMapping("/singer")    public Object allSinger(){        if(redisTemplate.hasKey("singerList")){            List<Singer> singers=redisTemplate.opsForList().range("singerList",0,-1);            return singers;        }else {            List<Singer> allSinger=singerService.list();            redisTemplate.opsForList().rightPushAll("singerList",allSinger);            return allSinger;        }    }    //根据歌手姓名搜索歌手    @GetMapping ("/singer/name/detail")    public Object singerOfName(HttpServletRequest req){        String name = req.getParameter("name").trim();        Map<String,Object> singer=new HashMap<>();        singer.put("name",name);        return singerService.listByMap(singer);    }    //根据歌手性别返回歌手    @GetMapping("/singer/sex/detail")    public Object singerOfsex(HttpServletRequest req){        String sex=req.getParameter("sex").trim();        Map<String,Object> sexs=new HashMap<>();        sexs.put("sex",sex);        return singerService.listByMap(sexs);    }    //根据id删除歌手    @PostMapping("/singer/delete")    public Object deleteSinger(HttpServletRequest req){        String id=req.getParameter("id").trim();        redisTemplate.delete("singerList");        return singerService.removeById(id);    }    //增加歌手    @PostMapping("/singer/add")    public Object addSinger(HttpServletRequest req){        JSONObject jsonObject=new JSONObject();        String name = req.getParameter("name").trim();        String sex = req.getParameter("sex").trim();        String pic = req.getParameter("pic").trim();        String birth = req.getParameter("birth").trim();        String location = req.getParameter("location").trim();        String introduction = req.getParameter("introduction").trim();        Singer singer=new Singer();        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");        Date myBirth=new Date();        try{            myBirth=simpleDateFormat.parse(birth);        } catch (ParseException e) {            e.printStackTrace();        }        singer.setName(name);        singer.setSex(new Byte(sex));        singer.setPic(pic);        singer.setBirth(myBirth);        singer.setLocation(location);        singer.setIntroduction(introduction);        boolean res = singerService.save(singer);        if (res){            jsonObject.put("code", 1);            jsonObject.put("msg", "添加成功");            redisTemplate.delete("singerList");        }else {            jsonObject.put("code", 0);            jsonObject.put("msg", "添加失败");        }        return jsonObject;    }    //更新歌手信息    @PostMapping("/singer/update")    public Object updateSinger(HttpServletRequest req){        JSONObject jsonObject=new JSONObject();        String id = req.getParameter("id").trim();        String name = req.getParameter("name").trim();        String sex = req.getParameter("sex").trim();        String pic = req.getParameter("pic").trim();        String birth = req.getParameter("birth").trim();        String location = req.getParameter("location").trim();        String introduction = req.getParameter("introduction").trim();        Singer singer=new Singer();        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");        Date myBirth = new Date();        try {            myBirth = dateFormat.parse(birth);        }catch (Exception e){            e.printStackTrace();        }        singer.setId(Integer.parseInt(id));        singer.setName(name);        singer.setSex(new Byte(sex));        singer.setPic(pic);        singer.setBirth(myBirth);        singer.setLocation(location);        singer.setIntroduction(introduction);        boolean res=singerService.updateById(singer);        if (res){            jsonObject.put("code", 1);            jsonObject.put("msg", "修改成功");            redisTemplate.delete("singerList");        }else {            jsonObject.put("code", 0);            jsonObject.put("msg", "修改失败");        }        return jsonObject;    }    //更新歌手头像    @PostMapping("/singer/avatar/update")    public Object updateSingerPic(@RequestParam("file")MultipartFile avatorFile,@RequestParam("id") int id) throws FileNotFoundException {        JSONObject jsonObject=new JSONObject();        if(avatorFile.isEmpty()){            jsonObject.put("code",0);            jsonObject.put("msg","文件上传失败!");            return jsonObject;        }        //删除本地旧头像        QueryWrapper<Singer> oldOne=new QueryWrapper<>();        oldOne.select("pic").eq("id",id);        Singer deleteSinger=singerService.getOne(oldOne);        String deleteFilePath=System.getProperty("user.dir")+deleteSinger.getPic();        File deleteFile=new File(deleteFilePath);        if(deleteFile.exists()){            deleteFile.delete();        }        //定义上传头像的文件名        String fileName=System.currentTimeMillis()+avatorFile.getOriginalFilename();        //获取JAR包同级目录        ApplicationHome h = new ApplicationHome(getClass());        //定义上传头像的文件存放路径--项目文件夹/img/singerPic        String filePath=System.getProperty("user.dir")+"/img"+"/singerPic";        //如果singerPic该文件夹不存在 则创建一个        File file1 =new File(filePath);        if (!file1.exists()){            file1.mkdir();        }        //存放文件---/img/singerPic/文件名        File dest =new File(filePath+"/"+fileName);        //定义返回数据库的文件的路径地址        String storeAvatorPath="/img/singerPic/"+fileName;        try{            avatorFile.transferTo(dest);            Singer singer=new Singer();            singer.setId(id);            singer.setPic(storeAvatorPath);            boolean res=singerService.updateById(singer);            if (res){                jsonObject.put("code", 1);                jsonObject.put("pic", storeAvatorPath);                jsonObject.put("msg", "上传成功");                redisTemplate.delete("singerList");            }else {                jsonObject.put("code", 0);                jsonObject.put("msg", "上传失败");            }            return jsonObject;        } catch (IOException e) {            jsonObject.put("code", 0);            jsonObject.put("msg", "上传失败" + e.getMessage());            return jsonObject;        }finally {            return jsonObject;        }    }}