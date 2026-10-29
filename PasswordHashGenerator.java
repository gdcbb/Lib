
package org.example.library.util;

import java.util.Scanner;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入管理员测试密码：");
        String password = scanner.nextLine();

        String hash = PasswordUtil.hashPassword(password);

        System.out.println("生成的密码哈希：");
        System.out.println(hash);
    }
}
