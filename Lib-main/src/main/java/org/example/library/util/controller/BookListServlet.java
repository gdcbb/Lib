package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.library.util.model.Book;
import org.example.library.util.service.BookService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/books")
public class BookListServlet extends HttpServlet {

    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String keyword = request.getParameter("keyword");

        try {
            List<Book> books = bookService.search(keyword);
            request.setAttribute("books", books);
            request.setAttribute("keyword", keyword);

            request.getRequestDispatcher("/WEB-INF/views/books.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("查询图书列表失败", e);
        }
    }
}
