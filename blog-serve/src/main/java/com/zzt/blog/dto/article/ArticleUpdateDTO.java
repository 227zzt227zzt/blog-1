package com.zzt.blog.dto.article;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzt.blog.enums.ArticleStatus;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class ArticleUpdateDTO {
    @Parameter(description = "文章ID", required = true)
    private Long id;
    @Parameter(description = "文章标题", required = true)
    private String title;
    @Parameter(description = "文章摘要", required = true)
    private String summary;
    @Parameter(description = "文章封面图片", required = true)
    private String coverImage;
    @Parameter(description = "文章内容", required = true)
    private String content;
    @Parameter(description = "文章分类ID", required = true)
    private Long categoryId;
    @Parameter(description = "文章状态", required = true)
    private ArticleStatus status;

}
