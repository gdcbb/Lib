package org.example.library.util.controller;

import org.example.library.util.PasswordUtil;
import org.example.library.util.dao.UserDAO;
import org.example.library.util.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "请输入用户名和密码");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        try {
            User user = userDAO.findByUsername(username.trim());

            // 当前阶段先比较数据库中的密码字段。
            // 下一步会将其替换为安全的密码哈希验证。
            if (user == null
                    || !PasswordUtil.verifyPassword(
                    password, user.getPassword())) {
                request.setAttribute("errorMessage", "用户名或密码错误");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                        .forward(request, response);
                return;
            }

            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("loginUser", user);
            session.setAttribute("role", user.getRole());

            response.sendRedirect(
                    request.getContextPath() + "/books"
            );

        } catch (SQLException e) {
            throw new ServletException("登录验证失败", e);
        }
    }
}
