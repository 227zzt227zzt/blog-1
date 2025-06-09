package com.zzt.blog.aspect;

import com.zzt.blog.annotation.LogOperation;
import com.zzt.blog.entity.OperationLog;
import com.zzt.blog.entity.User;
import com.zzt.blog.service.OperationLogService;
import com.zzt.blog.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 操作日志切面
 * @author zzt
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 定义切点 - 所有controller包下的所有方法
     */
    @Pointcut("execution(* com.zzt.blog.controller..*.*(..))")
    public void controllerPointcut() {}

    /**
     * 前置通知：在目标方法执行前执行
     */
    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 记录请求内容
        log.info("请求URL: {}", request.getRequestURL().toString());
        log.info("请求方法: {}", request.getMethod());
        log.info("请求IP: {}", request.getRemoteAddr());
        log.info("调用方法: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        log.info("返回当前用户id: {}", getCurrentUserId());
    }

    /**
     * 返回通知：在目标方法执行后执行
     */
    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 获取当前登录用户信息
        Long userId = getCurrentUserId();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 创建操作日志对象
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(userId);
        operationLog.setOperation(method.getName());
        operationLog.setTargetType(joinPoint.getTarget().getClass().getSimpleName());
        operationLog.setDetail("调用了" + joinPoint.getSignature().getDeclaringTypeName() + "." + method.getName());
        operationLog.setIpAddress(request.getRemoteAddr());
        operationLog.setCreateTime(new Date());

        // 保存操作日志
        try {
            operationLogService.logOperation(operationLog);
        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
                // 获取请求信息
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String authHeader = request.getHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        // 从JWT令牌中获取userId
                        return jwtUtils.getUserIdFromToken(token);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
        }
        return null;
    }
    @Pointcut("@annotation(com.zzt.blog.annotation.LogOperation)")
    public void logOperationPointcut() {}

    @AfterReturning(pointcut = "logOperationPointcut()", returning = "result")
    public void doAfterReturningWithAnnotation(JoinPoint joinPoint, Object result) {
        // 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogOperation logOperation = method.getAnnotation(LogOperation.class);

    }
}