// RoleMapper.java
package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT COUNT(*) FROM user_role WHERE role_id = #{roleId}")
    int countUsersByRole(Integer roleId);
}