package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.entity.UserRole;
import com.zzt.blog.mapper.UserRoleMapper;
import com.zzt.blog.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 227
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Transactional
    @Override
    public void assignRole(UserRole userRole) {
        // 检查是否已存在关联
        if (baseMapper.existsByUserAndRole(userRole.getUserId(), userRole.getRoleId())== 1) {
            throw new RuntimeException("用户已拥有该角色");
        }
        this.save(userRole);
    }

    @Transactional
    @Override
    public void revokeRole(UserRole userRole) {
        this.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRole.getUserId())
                .eq(UserRole::getRoleId, userRole.getRoleId()));
    }

    @Override
    public List<UserRole> getRolesByUserId(Long userId) {
        return this.list(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId));
    }
}