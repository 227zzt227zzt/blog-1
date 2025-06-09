package com.zzt.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum ArticleStatus {
    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    PRIVATE(2, "私密");

    @EnumValue
    private final int code;
    private final String desc;

    ArticleStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}