package com.zzt.blog.controller;

import com.zzt.blog.entity.OperationLog;
import com.zzt.blog.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 227
 */
@RestController
@RequestMapping("/operation-logs")
@Tag(name = "操作日志管理")
public class OperationLogController {
    
    @Autowired
    private OperationLogService operationLogService;

    @GetMapping
    @Operation(summary = "获取所有操作日志")
    public List<OperationLog> listOperationLogs() {
        return operationLogService.listOperationLogs();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取操作日志")
    public OperationLog getOperationLogById(@PathVariable Long id) {
        return operationLogService.getOperationLogById(id);
    }

    /**
     * 自动生成日志
     * TODO: 待实现
     */
    @PostMapping
    @Operation(summary = "根据当前新增操作日志")
    public void logOperation(@RequestBody OperationLog log) {
        operationLogService.logOperation(log);
    }
}