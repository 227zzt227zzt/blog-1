package com.zzt.blog.exception;

import lombok.Getter;

/**
 * 错误码枚举
 * @author 227
 */
@Getter
public enum ErrorCode {
    
    // 通用错误码
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统错误"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    TOKEN_INVALID(4011, "token无效"),
    SAVE_FILE_ERROR(5001, "保存文件失败"),
    
    // 业务错误码 (1000-1999)
    USER_NOT_FOUND(1001, "用户不存在"),
    USERNAME_ALREADY_EXISTS(1002, "用户名已存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    USER_DISABLED(1004, "用户被禁用"),
    EMAIL_ALREADY_EXISTS(1005, "邮箱已经注册，请登录"),
    // 文章相关错误码 (2000-2999)
    ARTICLE_NOT_FOUND(2001, "文章不存在"),
    CATEGORY_NOT_FOUND(2002, "分类不存在"),
    FILE_TOO_LARGE(2003, "文件大小超过限制"),
    INVALID_FILE_TYPE(2004, "文件类型不支持"),
    // 评论相关错误码 (3000-3999)
    COMMENT_NOT_FOUND(3001, "评论不存在");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}