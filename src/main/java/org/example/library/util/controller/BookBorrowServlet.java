package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.model.User;
import org.example.library.util.service.BorrowService;
import org.example.library.util.service.ServiceException;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/books/borrow")
public class BookBorrowServlet extends HttpServlet {

    private final BorrowService borrowService = new BorrowService();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        User loginUser = session == null
                ? null
                : (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            long id = Long.parseLong(request.getParameter("id"));
            int days = Integer.parseInt(request.getParameter("days"));

            borrowService.borrow(loginUser.getId(), id, days);

            response.sendRedirect(
                    request.getContextPath() + "/books?borrowed=1"
            );

        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "图书 ID 或借阅天数格式不正确"
            );
        } catch (ServiceException e) {
            response.sendError(e.getStatus(), e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("借阅图书失败", e);
        }
    }
}
