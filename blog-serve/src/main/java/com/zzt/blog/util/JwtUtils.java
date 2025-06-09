package com.zzt.blog.util;

import com.zzt.blog.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author 227
 */
@Component
public class JwtUtils {
    // 假设，你现在启动了项目，那么这个key就是一个随机的值，这个时候有人 登录来生成jwt，是按照这个密钥生成的
    // 如果说这时候重启了，那么就会重新生成另一个随机的值，这时候校验jwt的密钥就对不上
    // 令牌密钥
//    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String SECRET_KEY = "weqoprjfgcioskanmfoerwu90418329023iwrifdndcoisuf9023i9-fjn9012213412fcdsafe";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    // 令牌过期时间（毫秒）
    private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

    // 从令牌中获取用户名
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // 从令牌中获取角色
    public List<UserRole> getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> {
            List<Map<String, Object>> roleList = (List<Map<String, Object>>) claims.get("role");
            return roleList.stream().map(roleMap -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(Long.valueOf(roleMap.get("userId").toString()));
                userRole.setRoleId(Integer.valueOf(roleMap.get("roleId").toString()));
                return userRole;
            }).toList();
        });
    }

    // 从令牌中获取过期日期
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 从令牌中获取指定声明
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 从令牌中获取所有声明
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否已过期
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 从令牌中获取用户ID
    public Long getUserIdFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    // 生成令牌的核心方法
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SIGNATURE_ALGORITHM, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // 验证令牌
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    public String generateToken(String username, Long userId, List<UserRole> role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return doGenerateToken(claims, username);
    }
}