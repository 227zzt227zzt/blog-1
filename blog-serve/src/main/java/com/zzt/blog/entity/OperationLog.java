// OperationLog.java
package com.zzt.blog.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("operation_log")
public class OperationLog {
    @TableId
    private Long id;
    private Long userId;
    private String operation;
    private String targetType;
    private Long targetId;
    private String detail;
    private String ipAddress;
    private Date createTime;
}