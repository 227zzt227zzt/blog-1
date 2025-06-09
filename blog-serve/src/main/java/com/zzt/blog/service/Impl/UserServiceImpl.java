package com.zzt.blog.service.Impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzt.blog.dto.user.LoginDTO;
import com.zzt.blog.dto.user.RegisterDTO;
import com.zzt.blog.dto.user.UpdateUserDTO;
import com.zzt.blog.entity.User;
import com.zzt.blog.enums.UserStatus;
import com.zzt.blog.exception.BusinessException;
import com.zzt.blog.exception.ErrorCode;
import com.zzt.blog.mapper.UserMapper;
import com.zzt.blog.service.UserService;
import com.zzt.blog.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static com.zzt.blog.util.PasswordUtils.hashPassword;
import static com.zzt.blog.util.PasswordUtils.verifyPassword;

//import static com.zzt.blog.util.PasswordUtils.hashPassword;
//import static com.zzt.blog.util.PasswordUtils.verifyPassword;

//import static com.zzt.blog.util.PasswordUtils.hashPassword;

/**
 * @author 227
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public User getUserById(Long id) {
        User user = getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public User registerUser(RegisterDTO user) {
        // 检查用户名是否已存在
        if (this.lambdaQuery().eq(User::getUsername, user.getUsername()).exists()) {
            throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        //检查邮箱是否已经注册
        if (this.lambdaQuery().eq(User::getEmail, user.getEmail()).exists()) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User newUser = new User();
        user.setPassword(hashPassword(user.getPassword()));
        BeanUtils.copyProperties(user, newUser);
        newUser.setCreateTime(new Date());
        newUser.setStatus(UserStatus.NORMAL);
        getBaseMapper().insert(newUser);
        // 注册成功后，分配默认角色
        return newUser;
    }
    @Override
    public User loginUser(LoginDTO loginDTO) {
        //检查用户状态0禁用1正常
        if(!this.lambdaQuery().eq(User::getStatus, UserStatus.NORMAL)
                .exists()){
            throw new BusinessException(ErrorCode.USER_DISABLED.getCode(), ErrorCode.USER_DISABLED.getMessage());
        }
        //检查用户名是否存在
        if (!this.lambdaQuery().eq(User::getUsername, loginDTO.getUsername()).exists()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        //检查密码是否正确
        User user = this.lambdaQuery().eq(User::getUsername, loginDTO.getUsername()).one();
        if (!verifyPassword(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        //更新最后登录时间
        user.setLastLogin(new Date());
        getBaseMapper().updateById(user);
        return user;
    }

    @Override
    public User updateUser(UpdateUserDTO user) {
        try  {
            // 检查用户是否存在
            if (getById(user.getId()) == null) {
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
            // 检查用户名是否已存在
            if (this.lambdaQuery().eq(User::getUsername, user.getUsername())
                    .ne(User::getId, user.getId())
                    .exists()) {
                throw new BusinessException(ErrorCode.USERNAME_ALREADY_EXISTS);
                    }

            // 检查邮箱是否已经注册
            if (this.lambdaQuery().eq(User::getEmail, user.getEmail())
                    .ne(User::getId, user.getId())
                    .exists()) {
                throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            // 更新用户信息
            User newUser = new User();
            BeanUtils.copyProperties(user, newUser);
            newUser.setUpdateTime(new Date());
            getBaseMapper().updateById(newUser);
            // 返回更新后的用户信息
            return newUser;
          }
        catch (Exception e) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

    }


    @Override
    public void disableUser(Long userId) {
        User existingUser = getById(userId);
        if (existingUser == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        User user = new User();
        user.setId(userId);
        user.setStatus(UserStatus.DISABLED);
        getBaseMapper().updateById(user);
    }

    @Override
    public Page<User> getUserPage(Integer page, Integer size) {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //  只查询正常状态的用户, 按创建时间倒序排列
        wrapper.eq(User::getStatus, UserStatus.NORMAL)
                .orderByDesc(User::getCreateTime);

        // 执行分页查询
        return baseMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public String uploadAvatar(MultipartFile file, Long userId) {
        // 检查用户是否存在
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        // 检查文件类型是否合法
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        // 保存文件到本地
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        
        // 使用绝对路径存储文件
        String uploadDir = "D:/project/blog/blog/blog-serve/upload/avatars";


        // 确保上传目录存在
        java.io.File dir = new java.io.File(uploadDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new BusinessException(ErrorCode.SAVE_FILE_ERROR);
            }
        }
        
        // 生成文件名：时间戳_用户ID_随机UUID.后缀
        String newFileName = System.currentTimeMillis() + "_" + userId + "_" + UUID.randomUUID() + fileSuffix;
        String filePath = uploadDir + "/" + newFileName;
        
        try {
            file.transferTo(new java.io.File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SAVE_FILE_ERROR);
        }
        
        // 更新用户头像信息
        String avatarUrl = "/upload/avatars/" + newFileName;
        user.setAvatar(avatarUrl);
        if (!updateById(user)) {
            throw new BusinessException(ErrorCode.SAVE_FILE_ERROR);
        }
        
        return avatarUrl;
    }
    @Autowired
    private UserMapper userMapper;
    @Override
    public String getUserNameById(Long userId) {
        //用简单的方式实现，重写一个userMapper方法
        return userMapper.getUserNameById(userId);
    }

}