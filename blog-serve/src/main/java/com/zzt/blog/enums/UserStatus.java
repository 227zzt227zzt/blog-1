// UserStatus.java
package com.zzt.blog.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author 227
 */

@Getter
public enum UserStatus {
    DISABLED(0, "禁用"),
    NORMAL(1, "正常");

    @EnumValue
    private final int code;
    private final String desc;

    UserStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}