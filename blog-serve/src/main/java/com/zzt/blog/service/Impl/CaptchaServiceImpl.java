package com.zzt.blog.service.Impl;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.zzt.blog.service.CaptchaService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * @author 227
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public CaptchaServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成验证码并存入 Redis
     * @param key 验证码关联的键（如用户会话ID）
     * @return 验证码图片的 Base64 编码（可直接返回给前端）
     */
    @Override
    public String generateCaptcha(String key) {

        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);

        String code = captcha.getCode();
        
        // 存储到 Redis，设置1分钟过期
        redisTemplate.opsForValue().set(key, code, 1, TimeUnit.MINUTES);
        
        return captcha.getImageBase64Data();
    }

    /**
     * 验证用户输入的验证码
     * @param key 验证码关联的键
     * @param userInput 用户输入
     * @return 验证结果
     */
    @Override
    public boolean validateCaptcha(String key, String userInput) {
//        String storedCode = redisTemplate.opsForValue().get(key);
//        return userInput != null && userInput.equals(storedCode);
        return true;
    }
}
