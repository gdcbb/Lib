package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.model.Book;
import org.example.library.util.model.User;
import org.example.library.util.service.BookService;
import org.example.library.util.service.ServiceException;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/books/edit")
public class BookEditServlet extends HttpServlet {

    private final BookService bookService = new BookService();

    // 显示图书修改页面
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "仅管理员可编辑图书"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            long id = Long.parseLong(request.getParameter("id"));

            if (id <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "图书 ID 不正确"
                );
                return;
            }

            Book book = bookService.findById(id);

            if (book == null) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "没有找到这本图书"
                );
                return;
            }

            request.setAttribute("book", book);
            request.getRequestDispatcher(
                    "/WEB-INF/views/book-edit.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "图书 ID 必须是有效数字"
            );
        } catch (SQLException e) {
            throw new ServletException("查询图书信息失败", e);
        }
    }

    // 接收修改表单并保存
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "仅管理员可编辑图书"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            long id = Long.parseLong(request.getParameter("id"));

            String error = bookService.update(
                    id,
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

            response.sendRedirect(
                    request.getContextPath() + "/books"
            );

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID 或库存格式不正确"
            );
        } catch (ServiceException e) {
            response.sendError(e.getStatus(), e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("修改图书失败", e);
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
