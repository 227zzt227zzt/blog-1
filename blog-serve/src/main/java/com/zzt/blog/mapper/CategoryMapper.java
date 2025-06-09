// CategoryMapper.java
package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("SELECT COUNT(*) FROM article WHERE category_id = #{categoryId}")
    int countArticlesByCategory(Long categoryId);
}