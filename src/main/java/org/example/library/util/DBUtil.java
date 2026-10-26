package org.example.library.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库工具类：从 druid.properties 读取配置并创建连接池。
 */
public final class DBUtil {

    private static final DataSource DATA_SOURCE;

    static {
        try (InputStream in = DBUtil.class.getClassLoader()
                .getResourceAsStream("druid.properties")) {

            if (in == null) {
                throw new IllegalStateException("找不到 druid.properties");
            }

            Properties properties = new Properties();
            properties.load(in);
            DATA_SOURCE = DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    new IllegalStateException("初始化数据库连接池失败", e));
        }
    }

    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }
}
