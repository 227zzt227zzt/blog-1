// Tag.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tag")
public class Tags {
    @TableId
    private Integer id;
    private String name;
    private String color;
}