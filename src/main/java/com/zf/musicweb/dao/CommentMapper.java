package com.zf.musicweb.dao;import com.zf.musicweb.entity.Comment;import com.baomidou.mybatisplus.core.mapper.BaseMapper;/** * <p> *  Mapper 接口 * </p> * * @author ${author} * @since 2022-01-05 */public interface CommentMapper extends BaseMapper<Comment> {    int updateComment(Comment comment);}