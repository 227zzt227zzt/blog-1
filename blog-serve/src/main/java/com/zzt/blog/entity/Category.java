// Category.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author 227
 */
@Data
@TableName("category")
public class Category {
    private Long id;
    private String name;
    private String description;
    private Integer sort;
    private String icon;
    private Date createTime;
    private Date updateTime;
}