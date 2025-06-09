package com.zzt.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzt.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 227
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID获取用户名
     */
    @Select("SELECT username FROM user WHERE id = #{userId}")
    String getUserNameById(Long userId);
}
