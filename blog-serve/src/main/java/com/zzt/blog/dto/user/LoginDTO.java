package com.zzt.blog.dto.user;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

/**
 * @author 227
 */
@Data

public class LoginDTO {
    @Parameter(description = "用户名", required = true)
    private String username;
    @Parameter(description = "密码", required = true)
    private String password;
    @Parameter(description = "验证码", required = true)
    private String captcha;
    @Parameter(description = "sessionId", required = true)
    private String sessionId;
}
