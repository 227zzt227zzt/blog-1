package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {
    @TableId
    private Integer id;
    private String name;
    private String code;
    private String description;
}