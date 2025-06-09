package com.zzt.blog.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author 227
 */
@Data
public class Result<T>  {

    // 状态码
    private Integer code;
    // 提示信息
    private String message;
    // 响应数据
    private T data;
    // 时间戳
    private Date timestamp;

    //session Id
    private String sessionId;

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setTimestamp(new Date());
        return success(null);
    }
    /**
     * 成功响应（带有xml提示信息）
     */
    public static <T> Result<T> success(String message) {
            Result<T> result = new Result<>();
            result.setCode(200);
            result.setMessage(message);
            result.setTimestamp(new Date());
            return result;
    }
    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setTimestamp(new Date());
        return result;
    }
    /**
     * 成功响应（
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        result.setTimestamp(new Date());
        return result;
    }
    /**
     * 成功响应（
     */
    public static <T> Result<T> success(String message, T data, String sessionId) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        result.setSessionId(sessionId);
        result.setTimestamp(new Date());
        return result;
    }
    /**
     * 错误响应,没有错误信息
     */
    public static <T> Result<T> error( ) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage("error");
        result.setTimestamp(new Date());
        return result;
    }
    /**
     * 错误响应,只有错误信息
     */
    public static <T> Result<T> error( String message) {
        Result<T> result = new Result<>();
        result.setMessage(message);
        result.setCode(500);
        result.setTimestamp(new Date());
        return result;
    }
    /**
     * 错误响应
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(new Date());
        return result;
    }
    /**
     * 错误响应（带数据）
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        result.setTimestamp(new Date());
        return result;
    }


}
