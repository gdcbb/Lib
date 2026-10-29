
package org.example.library.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    // 为密码生成随机盐，并保存为 salt:hash 格式
    public static String hashPassword(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        byte[] hash = pbkdf2(password, salt);

        return Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(
            String password, String storedValue) {

        if (password == null || storedValue == null) {
            return false;
        }

        try {
            String[] parts = storedValue.split(":");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            byte[] actualHash = pbkdf2(password, salt);

            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt) {
        try {
            javax.crypto.spec.PBEKeySpec spec =
                    new javax.crypto.spec.PBEKeySpec(
                            password.toCharArray(),
                            salt,
                            210000,
                            256
                    );

            try {
                return javax.crypto.SecretKeyFactory
                        .getInstance("PBKDF2WithHmacSHA256")
                        .generateSecret(spec)
                        .getEncoded();
            } finally {
                spec.clearPassword();
            }

        } catch (java.security.GeneralSecurityException e) {
            throw new IllegalStateException("密码哈希计算失败", e);
        }
    }
}
