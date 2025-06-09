// User.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzt.blog.enums.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author 227
 */
@Data
@TableName("user")
@Schema(description = "用户实体")
public class User {
    @TableId
    private Long id;
    private String username;
    private String password;
    private String avatar;
    private String bio;
    private String email;
    private Date createTime;
    private Date lastLogin;
    /**
     * 0:禁用 1:正常
     */
    private UserStatus status;

    private Date updateTime;
    /**
     * 昵称
     */
    private String nickname;
}