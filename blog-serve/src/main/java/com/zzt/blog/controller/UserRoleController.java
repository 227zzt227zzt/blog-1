package com.zzt.blog.controller;

import com.zzt.blog.entity.UserRole;
import com.zzt.blog.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-roles")
@Tag(name = "用户角色管理")
public class UserRoleController {
    
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping
    @Operation(summary = "分配用户角色")
    public void assignRole(@RequestBody UserRole userRole) {
        userRoleService.assignRole(userRole);
    }

    @DeleteMapping
    @Operation(summary = "撤销用户角色")
    public void revokeRole(@RequestBody UserRole userRole) {
        userRoleService.revokeRole(userRole);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户所有角色")
    public List<UserRole> getRolesByUserId(@PathVariable Long userId) {
        return userRoleService.getRolesByUserId(userId);
    }
}