// ArticleTag.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ArticleTag {
    @TableId
    private Long articleId;
    private Integer tagId;
}