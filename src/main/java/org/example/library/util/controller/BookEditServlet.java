
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

@WebServlet("/books/edit")
public class BookEditServlet extends HttpServlet {

    private final BookDAO bookDAO = new BookDAO();

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

        String idText = request.getParameter("id");

        try {
            long id = Long.parseLong(idText);

            if (id <= 0) {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "图书 ID 不正确"
                );
                return;
            }

            Book book = bookDAO.findById(id);

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

            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String publisher = request.getParameter("publisher");
            String category = request.getParameter("category");
            String priceText = request.getParameter("price");
            String stockText = request.getParameter("stock");

            if (id <= 0
                    || isBlank(isbn)
                    || isBlank(title)
                    || isBlank(author)
                    || isBlank(publisher)
                    || isBlank(category)
                    || isBlank(priceText)
                    || isBlank(stockText)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "ID 和所有图书字段均不能为空"
                );
                return;
            }

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
            book.setId(id);
            book.setIsbn(isbn.trim());
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setPublisher(publisher.trim());
            book.setCategory(category.trim());
            book.setPrice(price);
            book.setStock(stock);

            boolean updated = bookDAO.update(book);

            if (!updated) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "图书不存在或没有发生修改"
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/books"
            );

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "ID、价格或库存格式不正确"
            );
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                response.sendError(
                        HttpServletResponse.SC_CONFLICT,
                        "修改失败：ISBN 已被其他图书使用"
                );
            } else {
                throw new ServletException("修改图书失败", e);
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
