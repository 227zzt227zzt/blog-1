package com.zzt.blog.service;

import com.zzt.blog.entity.UserRole;
import java.util.List;

public interface UserRoleService {
    void assignRole(UserRole userRole);
    void revokeRole(UserRole userRole);
    List<UserRole> getRolesByUserId(Long userId);
}