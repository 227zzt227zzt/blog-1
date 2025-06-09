package com.zzt.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzt.blog.dto.user.LoginDTO;
import com.zzt.blog.dto.user.RegisterDTO;
import com.zzt.blog.dto.user.UpdateUserDTO;
import com.zzt.blog.entity.User;
import com.zzt.blog.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getUserById(Long id);
    User getUserByUsername(String username);
    User registerUser(RegisterDTO user);
    User loginUser(LoginDTO loginDTO);
    User updateUser(UpdateUserDTO user);
    void disableUser(Long userId);

     Page<User> getUserPage(Integer page, Integer size);

    String uploadAvatar(MultipartFile file, Long userId);

    String getUserNameById(Long userId);
}