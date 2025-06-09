package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 227
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}