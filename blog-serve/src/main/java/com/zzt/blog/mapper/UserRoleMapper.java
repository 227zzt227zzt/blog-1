// UserRoleMapper.java
package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Select("SELECT COUNT(*) FROM user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int existsByUserAndRole(@Param("userId") Long userId, @Param("roleId") Integer roleId);
}