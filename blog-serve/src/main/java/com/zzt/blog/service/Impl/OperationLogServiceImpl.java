package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.entity.OperationLog;
import com.zzt.blog.mapper.OperationLogMapper;
import com.zzt.blog.service.OperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 227
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public List<OperationLog> listOperationLogs() {
        return this.list();
    }

    @Override
    public OperationLog getOperationLogById(Long id) {
        return this.getById(id);
    }

    @Transactional
    @Override
    public void logOperation(OperationLog log) {
        log.setCreateTime(new Date());
        //
        //获取到当前用户的id
        log.setUserId(log.getUserId());
        log.setOperation(log.getOperation());
        log.setTargetType(log.getTargetType());
        log.setTargetId(log.getTargetId());
        log.setDetail(log.getDetail());
        log.setIpAddress(log.getIpAddress());
        this.save(log);
    }
}