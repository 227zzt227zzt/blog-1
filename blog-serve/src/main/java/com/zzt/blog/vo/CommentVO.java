package com.zzt.blog.vo;

import com.zzt.blog.enums.CommentStatus;
import lombok.Data;

import java.util.Date;

/**
 * @author 227
 */
@Data
public class CommentVO {
    private Long id;
    private Long articleId;
    private Long userId;
    private String userName;
    private String content;
    private Date createTime;
    private Long parentId;
}
