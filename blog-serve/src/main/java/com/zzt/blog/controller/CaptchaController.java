package com.zzt.blog.controller;

import com.zzt.blog.service.CaptchaService;
import com.zzt.blog.util.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 227
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;


    @GetMapping("/generate")
    public Result<String> generateCaptcha( HttpSession httpSession) {
        String sessionId = httpSession.getId();
        String captchaImage = captchaService.generateCaptcha(sessionId);
        return Result.success("验证码生成成功", captchaImage, sessionId);
    }
}
