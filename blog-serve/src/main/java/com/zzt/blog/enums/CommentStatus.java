// CommentStatus.java
package com.zzt.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum CommentStatus {
    PENDING(0, "待审核"),
    PUBLISHED(1, "已发布"),
    DELETED(2, "已删除");

    @EnumValue
    private final int code;
    private final String desc;

    CommentStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }}
