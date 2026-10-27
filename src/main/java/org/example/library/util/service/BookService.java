package org.example.library.util.service;

import org.example.library.util.dao.BookDAO;
import org.example.library.util.model.Book;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * 图书业务层：负责参数校验、业务规则与 DAO 调用。
 */
public class BookService {

    private final BookDAO bookDAO = new BookDAO();

    public List<Book> search(String keyword) throws SQLException {
        return bookDAO.findAll(keyword);
    }

    public Book findById(long id) throws SQLException {
        return bookDAO.findById(id);
    }

    /**
     * 新增图书。
     *
     * @return 成功返回 null，校验失败返回错误提示
     * @throws ServiceException ISBN 重复时抛出
     */
    public String add(String isbn,
                      String title,
                      String author,
                      String publisher,
                      String category,
                      String stockText)
            throws SQLException, ServiceException {

        if (isBlank(isbn) || isBlank(title) || isBlank(author)
                || isBlank(publisher) || isBlank(category)
                || isBlank(stockText)) {
            return "所有图书字段均不能为空";
        }

        int stock;

        try {
            stock = Integer.parseInt(stockText.trim());
        } catch (NumberFormatException e) {
            return "库存格式不正确";
        }

        if (stock < 0) {
            return "库存不能小于 0";
        }

        Book book = new Book();
        book.setIsbn(isbn.trim());
        book.setTitle(title.trim());
        book.setAuthor(author.trim());
        book.setPublisher(publisher.trim());
        book.setCategory(category.trim());
        // 价格字段已从新增页移除，数据库默认 0.00
        book.setPrice(BigDecimal.ZERO);
        book.setStock(stock);

        try {
            bookDAO.insert(book);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw ServiceException.conflict("新增失败：ISBN 已存在", e);
            }
            throw e;
        }

        return null;
    }

    /**
     * 修改图书。
     *
     * @return 成功返回 null，校验失败返回错误提示
     * @throws ServiceException ISBN 与其他图书重复时抛出
     */
    public String update(long id,
                         String isbn,
                         String title,
                         String author,
                         String publisher,
                         String category,
                         String stockText)
            throws SQLException, ServiceException {

        if (id <= 0 || isBlank(isbn) || isBlank(title) || isBlank(author)
                || isBlank(publisher) || isBlank(category)
                || isBlank(stockText)) {
            return "ID 和所有图书字段均不能为空";
        }

        Book existing = bookDAO.findById(id);

        if (existing == null) {
            throw ServiceException.notFound("图书不存在或没有发生修改");
        }

        int stock;

        try {
            stock = Integer.parseInt(stockText.trim());
        } catch (NumberFormatException e) {
            return "ID 或库存格式不正确";
        }

        if (stock < 0) {
            return "库存不能小于 0";
        }

        Book book = new Book();
        book.setId(id);
        book.setIsbn(isbn.trim());
        book.setTitle(title.trim());
        book.setAuthor(author.trim());
        book.setPublisher(publisher.trim());
        book.setCategory(category.trim());
        // 价格字段已从编辑页移除，修改时保留原值
        book.setPrice(existing.getPrice());
        book.setStock(stock);

        try {
            if (!bookDAO.update(book)) {
                throw ServiceException.notFound("图书不存在或没有发生修改");
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw ServiceException.conflict("修改失败：ISBN 已被其他图书使用", e);
            }
            throw e;
        }

        return null;
    }

    public boolean delete(long id) throws SQLException {
        return bookDAO.delete(id);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
