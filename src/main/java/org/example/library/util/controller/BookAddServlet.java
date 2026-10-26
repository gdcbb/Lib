package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.dao.BookDAO;
import org.example.library.util.model.Book;
import org.example.library.util.model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/books/add")
public class BookAddServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

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

        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String category = request.getParameter("category");
        String priceText = request.getParameter("price");
        String stockText = request.getParameter("stock");

        if (isBlank(isbn)
                || isBlank(title)
                || isBlank(author)
                || isBlank(publisher)
                || isBlank(category)
                || isBlank(priceText)
                || isBlank(stockText)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "所有图书字段均不能为空"
            );
            return;
        }

        try {
            BigDecimal price = new BigDecimal(priceText);
            int stock = Integer.parseInt(stockText);

            if (price.compareTo(BigDecimal.ZERO) < 0 || stock < 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "价格和库存不能小于 0"
                );
                return;
            }

            Book book = new Book();
            book.setIsbn(isbn.trim());
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setPublisher(publisher.trim());
            book.setCategory(category.trim());
            book.setPrice(price);
            book.setStock(stock);

            bookDAO.insert(book);

            response.sendRedirect(request.getContextPath() + "/books");

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "价格或库存格式不正确"
            );
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "新增失败：ISBN 已存在"
                );
            } else {
                throw new ServletException("新增图书失败", e);
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
