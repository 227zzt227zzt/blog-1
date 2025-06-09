package com.zzt.blog.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.util.Date;

/**
 * @author 227
 */
@Data
public class ArticleVO {

    @TableId
    @Parameter(description = "文章id")
    private Long id;
    @Parameter(description = "作者id")
    private Long authorId;
    @Parameter(description = "作者名称")
    private String author;
    @Parameter(description = "文章标题")
    private String title;
    @Parameter(description = "文章摘要")
    private String summary;
    @Parameter(description = "文章封面")
    private String coverImage;
    @Parameter(description = "文章内容")
    private String content;
    @Parameter(description = "文章分类id")
    private Long categoryId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Parameter(description = "创建时间")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Parameter(description = "更新时间")
    private Date updateTime;
    @Parameter(description = "文章状态")
    private Integer viewCount;
}
