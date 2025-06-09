package com.zzt.blog.dto.user;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * @author 227
 */
@Data
public class UpdateUserDTO {
    @TableId
    private Long id;
    //每个字段记得添加注解到接口文档
    /**
     * 昵称
     */
   @Parameter(description = "昵称")
    private String nickname;
    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String username;
    /**
     * 密码
     */
    @Parameter(description = "密码")
    private String password;
    /**
     * 头像
     */
    @Parameter(description = "头像")
    private String avatar;
    /**
     * 签名
     */
    @Parameter(description = "签名")
    private String bio;
    /**
     * 邮箱
     */
    @Parameter(description = "邮箱")
    private String email;
}
