package com.zzt.blog.util;

public  class UserContext {
    // 设置ThreadLocal变量，以便在线程中获取 用户ID
    private static final ThreadLocal<Long> userHolder = new ThreadLocal<>();

    //  设置用户ID
    public static void setUserId(Long userId) {
        userHolder.set(userId);
    }
    // 获取用户ID
     public static Long getUserId() {
        return userHolder.get();
    }
    // 清除用户ID
    public static void removeUserId() {
        userHolder.remove();
    }
}
