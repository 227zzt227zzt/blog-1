package com.zzt.blog.service;

import com.zzt.blog.dto.RoleDTO;
import com.zzt.blog.entity.Role;
import java.util.List;

public interface RoleService {
    List<Role> listRoles();
    Role getRoleById(Integer id);
    void saveRole(RoleDTO role);
    void updateRole(Role role);
    void deleteRole(Integer id);
}