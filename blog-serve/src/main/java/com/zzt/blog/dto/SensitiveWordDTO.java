package com.zzt.blog.dto;

import lombok.Data;

@Data
public class SensitiveWordDTO {
    private String word;
    private String replaceWord;
    /**
     * 1-评论过滤 2-文章过滤
     */
    private Integer type;
}
