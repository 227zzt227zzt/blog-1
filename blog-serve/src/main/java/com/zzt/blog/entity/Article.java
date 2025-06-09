// Article.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzt.blog.enums.ArticleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author 227
 */
@Data
@TableName("article")
@Schema(description = "文章实体")
public class Article {
    @TableId
    private Long id;
    @TableField(value = "author_id")
    private Long authorId;
    private String title;
    private String summary;
    private String coverImage;
    private String content;
    private Long categoryId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
    /**
     * 0-草稿，1-发布
     */
    private ArticleStatus status;
    private Integer viewCount;
    private Boolean isTop;
}