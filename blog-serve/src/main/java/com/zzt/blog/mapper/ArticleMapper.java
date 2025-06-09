package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 227
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}