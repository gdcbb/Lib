package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.model.User;
import org.example.library.util.service.BookService;
import org.example.library.util.service.ServiceException;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/books/add")
public class BookAddServlet extends HttpServlet {

    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "仅管理员可新增图书"
            );
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/book-add.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "仅管理员可新增图书"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            String error = bookService.add(
                    request.getParameter("isbn"),
                    request.getParameter("title"),
                    request.getParameter("author"),
                    request.getParameter("publisher"),
                    request.getParameter("category"),
                    request.getParameter("stock")
            );

            if (error != null) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        error
                );
                return;
            }

            response.sendRedirect(request.getContextPath() + "/books");

        } catch (ServiceException e) {
            response.sendError(e.getStatus(), e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("新增图书失败", e);
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
