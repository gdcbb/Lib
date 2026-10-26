package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.PasswordUtil;
import org.example.library.util.dao.UserDAO;
import org.example.library.util.model.User;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("loginUser") != null) {
            response.sendRedirect(request.getContextPath() + "/books");
            return;
        }

        if ("1".equals(request.getParameter("registered"))) {
            request.setAttribute("message", "注册成功，请登录");
        }

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

        if (isBlank(username) || isBlank(password)) {
            forwardWithError(request, response, "请输入用户名和密码");
            return;
        }

        try {
            User user = userDAO.findByUsername(username.trim());

            if (user == null
                    || !PasswordUtil.verify(password, user.getPassword())) {
                forwardWithError(request, response, "用户名或密码错误");
                return;
            }

            // 防止会话固定攻击：登录前销毁旧会话，再创建新会话
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);

            // 仅保存必要的身份信息，密码不进入 Session
            user.setPassword(null);
            session.setAttribute("loginUser", user);
            session.setAttribute("loginTime", System.currentTimeMillis());

            response.sendRedirect(request.getContextPath() + "/books");

        } catch (SQLException e) {
            throw new ServletException("登录失败", e);
        }
    }

    private void forwardWithError(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String message)
            throws ServletException, IOException {
        request.setAttribute("error", message);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
