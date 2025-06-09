package com.zzt.blog.service;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author 227
 */
public interface CaptchaService {
    /**
     * 生成验证码并存入 Redis
     * @param key 验证码关联的键（如用户会话ID）
     * @return 验证码图片的 Base64 编码（可直接返回给前端）
     */
    String generateCaptcha(String key);
    /**
     * 验证用户输入的验证码
     * @param key 验证码关联的键
     * @param userInput 用户输入
     * @return 验证结果
     */
    boolean validateCaptcha(String key, String userInput);
}
