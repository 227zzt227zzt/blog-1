package com.zzt.blog.dto;

import com.zzt.blog.enums.CommentStatus;
import lombok.Data;

import java.util.Date;
/**
 * @author 227
 */
@Data
public class CommentDTO {
    private Long articleId;
    private Long userId;
    private String content;
    private Long parentId;
    /**
     * 0-待审核 1-已发布 2-已删除
     */
    private CommentStatus status;
    private String ipAddress;
}
