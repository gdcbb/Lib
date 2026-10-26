package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.library.util.PasswordUtil;
import org.example.library.util.dao.UserDAO;
import org.example.library.util.model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        String error = validate(username, password, confirmPassword);

        if (error != null) {
            forwardWithError(request, response, error);
            return;
        }

        try {
            if (userDAO.findByUsername(username.trim()) != null) {
                forwardWithError(request, response, "用户名已存在");
                return;
            }

            User user = new User();
            user.setUsername(username.trim());
            user.setRealName(username.trim());
            user.setPassword(PasswordUtil.hash(password));
            user.setRole("user");

            userDAO.insert(user);

            response.sendRedirect(
                    request.getContextPath() + "/login?registered=1"
            );

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                forwardWithError(request, response, "用户名已存在");
            } else {
                throw new ServletException("注册失败", e);
            }
        }
    }

    private String validate(String username,
                            String password,
                            String confirmPassword) {

        if (isBlank(username)
                || isBlank(password) || isBlank(confirmPassword)) {
            return "请填写所有字段";
        }

        username = username.trim();
        if (username.length() < 3 || username.length() > 20) {
            return "用户名长度需在 3 到 20 个字符之间";
        }

        if (password.length() < 6) {
            return "密码长度不能少于 6 位";
        }

        if (!password.equals(confirmPassword)) {
            return "两次输入的密码不一致";
        }

        return null;
    }

    private void forwardWithError(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String message)
            throws ServletException, IOException {

        request.setAttribute("error", message);
        request.setAttribute("username", request.getParameter("username"));

        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
