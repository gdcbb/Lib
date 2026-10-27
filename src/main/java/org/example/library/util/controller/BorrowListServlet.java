package org.example.library.util.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.example.library.util.model.BorrowRecord;
import org.example.library.util.model.User;
import org.example.library.util.service.BorrowService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 我的借阅：展示当前登录用户的全部借阅记录。
 */
@WebServlet("/borrows")
public class BorrowListServlet extends HttpServlet {

    private final BorrowService borrowService = new BorrowService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loginUser = session == null
                ? null
                : (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<BorrowRecord> records =
                    borrowService.listByUser(loginUser.getId());

            request.setAttribute("records", records);

            request.getRequestDispatcher("/WEB-INF/views/borrows.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("查询借阅记录失败", e);
        }
    }
}
