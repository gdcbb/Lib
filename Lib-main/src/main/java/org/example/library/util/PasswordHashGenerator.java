package org.example.library.util;

/**
 * 用于生成初始账号密码哈希，方便写入 library.sql。
 * 用法：java PasswordHashGenerator admin123
 */
public final class PasswordHashGenerator {

    public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "admin123";
        System.out.println(raw + " -> " + PasswordUtil.hash(raw));
    }
}
