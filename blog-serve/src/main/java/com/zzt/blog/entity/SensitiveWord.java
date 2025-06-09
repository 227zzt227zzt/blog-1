package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sensitive_word")
public class SensitiveWord {
    @TableId
    private Integer id;
    private String word;
    private String replaceWord;
    /**
     * 1-评论过滤 2-文章过滤
     */
    private Integer type;
}