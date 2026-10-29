
package org.example.library.util.dao;

import org.example.library.util.DBUtil;
import org.example.library.util.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {

        String sql = "SELECT id, username, password, role " +
                "FROM users WHERE username = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    User user = new User();

                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    return user;
                }

                return null;
            }
        }
    }
}
