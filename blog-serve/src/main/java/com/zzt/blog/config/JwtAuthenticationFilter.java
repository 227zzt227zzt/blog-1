package com.zzt.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzt.blog.entity.Role;
import com.zzt.blog.entity.UserRole;
import com.zzt.blog.service.RoleService;
import com.zzt.blog.util.JwtUtils;
import com.zzt.blog.util.Result;
import com.zzt.blog.exception.ErrorCode;
import com.zzt.blog.util.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 227
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private final RoleService roleService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, ObjectMapper objectMapper, RoleService roleService) {
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
        this.roleService = roleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 获取请求路径
        String requestPath = request.getServletPath();
        
        // 对于登录和注册接口，直接放行，不进行JWT验证
        if ("/api/users/login".equals(requestPath) || "/api/users/register".equals(requestPath) || requestPath.startsWith("/api/captcha/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        List<UserRole> userRoles = null;

        // 检查Authorization头是否存在且以Bearer开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtils.getUsernameFromToken(jwt);
                userRoles = jwtUtils.getRoleFromToken(jwt);
            } catch (Exception e) {
                logger.error("JWT令牌验证失败", e);
                // 使用我们之前修改的方式返回错误响应
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                Result<Void> result = Result.error(ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMessage());
                response.getWriter().write(objectMapper.writeValueAsString(result));
                return; // 直接返回，不再继续过滤器链
            }
        }

        // 如果成功提取用户名且当前SecurityContext中没有认证信息，则进行认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 创建权限列表
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            
            // 如果有角色信息，将角色转换为Spring Security的权限
            if (userRoles != null && !userRoles.isEmpty()) {
                // 从数据库获取角色详细信息
                for (UserRole userRole : userRoles) {
                    Role role = roleService.getRoleById(userRole.getRoleId());
                    if (role != null) {
                        // 添加ROLE_前缀是Spring Security的约定
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
                    }
                }
            }
            
            // 创建认证令牌，包含用户名和权限列表
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // 将认证信息设置到SecurityContext中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            Long userIdFromToken = jwtUtils.getUserIdFromToken(jwt);
            UserContext.setUserId(userIdFromToken);
        }
        
        filterChain.doFilter(request, response);
    }
}