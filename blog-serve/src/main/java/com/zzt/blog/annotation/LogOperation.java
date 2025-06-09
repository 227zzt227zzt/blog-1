package com.zzt.blog.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * @author zzt
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    /**
     * 操作描述
     */
    String value() default "";

    /**
     * 操作类型
     */
    String type() default "";
}