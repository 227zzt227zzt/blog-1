package com.zzt.blog.dto.article;

import com.zzt.blog.enums.ArticleStatus;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * @author 227
 */
@Data
public class ArticleDTO {
    @Parameter(name = "文章ID", description = "文章ID")

    private String title;
    @Parameter(description = "文章摘要")
    private String summary;
    @Parameter(description = "文章内容")
    private String content;
    @Parameter(description = "文章分类ID")
    private Long categoryId;
    /**
     * 0-草稿，1-发布
     */
    @Parameter(description = "文章状态")
    private ArticleStatus status;
    @Parameter(description = "作者ID")
    private String authorId;
    @Parameter(description = "文章封面图片")
    private String coverImage;
}
