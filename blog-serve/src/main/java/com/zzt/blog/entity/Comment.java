// Comment.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzt.blog.enums.CommentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author 227
 */
@Data
@TableName("comment")
@Schema(description = "评论实体")
public class Comment {
    @TableId
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Date createTime;
    private Long parentId;
    /**
     * 0-待审核 1-已发布 2-已删除
     */
    private CommentStatus status;
}