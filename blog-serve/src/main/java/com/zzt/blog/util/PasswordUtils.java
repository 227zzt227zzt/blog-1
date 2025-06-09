package com.zzt.blog.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 227
 */
public class PasswordUtils {
    public static String hashPassword(String plainPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(plainPassword);
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plainPassword, hashedPassword);
    }
}