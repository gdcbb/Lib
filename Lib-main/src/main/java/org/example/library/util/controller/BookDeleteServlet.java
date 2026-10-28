package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.model.User;
import org.example.library.util.service.BookService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/books/delete")
public class BookDeleteServlet extends HttpServlet {

    private final BookService bookService = new BookService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "仅管理员可删除图书"
            );
            return;
        }

        try {
            long id = Long.parseLong(request.getParameter("id"));

            if (id <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "图书 ID 不正确"
                );
                return;
            }

            if (!bookService.delete(id)) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "图书不存在或已被删除"
                );
                return;
            }

            response.sendRedirect(request.getContextPath() + "/books");

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "图书 ID 必须是有效数字"
            );
        } catch (SQLException e) {
            throw new ServletException("删除图书失败", e);
        }
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        User user = (User) session.getAttribute("loginUser");
        return user != null && "admin".equals(user.getRole());
    }
}
