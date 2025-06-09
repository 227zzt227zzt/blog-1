package com.zzt.blog.controller;

import com.zzt.blog.dto.RoleDTO;
import com.zzt.blog.entity.Role;
import com.zzt.blog.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name = "角色管理")
public class RoleController {
    
    @Autowired
    private RoleService roleService;

    @GetMapping
    @Operation(summary = "获取所有角色")
    public List<Role> listRoles() {
        return roleService.listRoles();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取角色")
    public Role getRoleById(@PathVariable Integer id) {
        return roleService.getRoleById(id);
    }

    @PostMapping
    @Operation(summary = "创建角色")
    public void createRole(@RequestBody RoleDTO role) {
        roleService.saveRole(role);
    }

    @PutMapping
    @Operation(summary = "更新角色")
    public void updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public void deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
    }
}