package com.zzt.blog.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.dto.RoleDTO;
import com.zzt.blog.entity.Role;
import com.zzt.blog.mapper.RoleMapper;
import com.zzt.blog.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> listRoles() {
        return this.list();
    }

    @Override
    public Role getRoleById(Integer id) {
        return this.getById(id);
    }

    @Transactional
    @Override
    public void saveRole(RoleDTO role) {
        if (this.lambdaQuery().eq(Role::getCode, role.getCode()).exists()) {
            throw new RuntimeException("角色编码已存在");
        }
        Role newRole = new Role();
        BeanUtils.copyProperties(role, newRole);
        this.save(newRole);
    }

    @Transactional
    @Override
    public void updateRole(Role role) {
        if (this.lambdaQuery().eq(Role::getCode, role.getCode()).ne(Role::getId, role.getId()).exists()) {
            throw new RuntimeException("角色编码已存在");
        }
        //如果为空就不修改
        if (role.getName() == null) {
            role.setName(this.getById(role.getId()).getName());
        }
        if (role.getDescription() == null) {
            role.setDescription(this.getById(role.getId()).getDescription());
        }
        if (role.getCode() == null) {
            role.setCode(this.getById(role.getId()).getCode());
        }
        Role newRole = new Role();
        BeanUtils.copyProperties(role, newRole);
        this.updateById(newRole);
    }

    @Transactional
    @Override
    public void deleteRole(Integer id) {
        if (baseMapper.countUsersByRole(id) > 0) {
            throw new RuntimeException("角色已被用户关联，无法删除");
        }
        this.removeById(id);
    }
}