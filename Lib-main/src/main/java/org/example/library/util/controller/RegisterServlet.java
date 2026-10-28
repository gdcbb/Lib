package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.library.util.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

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

        try {
            String error = userService.register(username, password, confirmPassword);

            if (error != null) {
                forwardWithError(request, response, error);
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/login?registered=1"
            );

        } catch (SQLException e) {
            throw new ServletException("注册失败", e);
        }
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
}
