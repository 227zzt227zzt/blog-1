package com.zzt.blog.service;

import com.zzt.blog.entity.OperationLog;
import java.util.List;

/**
 * @author 227
 */
public interface OperationLogService {
    List<OperationLog> listOperationLogs();
    OperationLog getOperationLogById(Long id);
    void logOperation(OperationLog log);
}