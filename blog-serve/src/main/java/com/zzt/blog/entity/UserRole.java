package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author 227
 */
@Data
public class UserRole {
    @TableId
    private Long userId;
    private Integer roleId;
}