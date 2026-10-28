package org.example.library.util.dao;

import org.example.library.util.DBUtil;
import org.example.library.util.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private static final String COLUMNS =
            "id, isbn, title, author, publisher, category, price, stock";

    public List<Book> findAll(String keyword) throws SQLException {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();

        StringBuilder sql = new StringBuilder(
                "SELECT " + COLUMNS + " FROM book");
        if (hasKeyword) {
            sql.append(" WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?");
        }
        sql.append(" ORDER BY id DESC");

        List<Book> books = new ArrayList<>();

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            if (hasKeyword) {
                String like = "%" + keyword.trim() + "%";
                statement.setString(1, like);
                statement.setString(2, like);
                statement.setString(3, like);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    books.add(mapRow(resultSet));
                }
            }
        }

        return books;
    }

    public Book findById(long id) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM book WHERE id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    public boolean insert(Book book) throws SQLException {
        String sql = "INSERT INTO book "
                + "(isbn, title, author, publisher, category, price, stock) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getCategory());
            statement.setBigDecimal(6, book.getPrice());
            statement.setInt(7, book.getStock());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean update(Book book) throws SQLException {
        String sql = "UPDATE book SET isbn = ?, title = ?, author = ?, "
                + "publisher = ?, category = ?, price = ?, stock = ? "
                + "WHERE id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setString(5, book.getCategory());
            statement.setBigDecimal(6, book.getPrice());
            statement.setInt(7, book.getStock());
            statement.setLong(8, book.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM book WHERE id = ?";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Book mapRow(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setCategory(resultSet.getString("category"));
        book.setPrice(resultSet.getBigDecimal("price"));
        book.setStock(resultSet.getInt("stock"));
        return book;
    }
}
