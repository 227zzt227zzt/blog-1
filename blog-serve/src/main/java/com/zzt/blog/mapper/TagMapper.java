// TagMapper.java
package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.Tags;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TagMapper extends BaseMapper<Tags> {
    @Select("SELECT COUNT(*) FROM article_tag WHERE tag_id = #{tagId}")
    int countArticlesByTag(@Param("tagId") Integer tagId);
}