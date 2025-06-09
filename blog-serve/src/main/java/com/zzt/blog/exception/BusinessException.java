package com.zzt.blog.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 业务异常
 * @author 227
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {
    
    private Integer code;
    
    /**
     * 构造函数
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    /**
     * 构造函数
     * @param errorCodecode 错误码枚举
     */
    public BusinessException(ErrorCode errorCodecode) {
        
        super(errorCodecode.getMessage());
        this.code = errorCodecode.getCode();
    }
    /**
     * 构造函数
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }
    
    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}