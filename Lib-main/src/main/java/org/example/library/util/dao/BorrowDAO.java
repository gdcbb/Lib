package org.example.library.util.dao;

import org.example.library.util.DBUtil;
import org.example.library.util.model.BorrowRecord;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowDAO {

    private static final String SELECT_COLUMNS =
            "r.id, r.user_id, r.book_id, u.username, b.title, "
            + "r.borrow_time, r.due_date, r.return_time, r.status "
            + "FROM borrow_record r "
            + "JOIN user u ON u.id = r.user_id "
            + "JOIN book b ON b.id = r.book_id ";

    /**
     * 借阅：在同一事务中写入借阅记录并扣减库存。
     *
     * @return 库存不足时返回 false，且不会留下记录
     */
    public boolean borrow(long userId, long bookId, int days) throws SQLException {
        String insertSql = "INSERT INTO borrow_record "
                + "(user_id, book_id, borrow_time, due_date, status) "
                + "VALUES (?, ?, NOW(), ?, 0)";
        String stockSql = "UPDATE book SET stock = stock - 1 "
                + "WHERE id = ? AND stock > 0";

        Connection connection = DBUtil.getConnection();

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement insert = connection.prepareStatement(
                    insertSql, Statement.RETURN_GENERATED_KEYS)) {

                insert.setLong(1, userId);
                insert.setLong(2, bookId);
                insert.setDate(3, Date.valueOf(LocalDate.now().plusDays(days)));
                insert.executeUpdate();
            }

            try (PreparedStatement stock = connection.prepareStatement(stockSql)) {
                stock.setLong(1, bookId);

                if (stock.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    /**
     * 归还：校验记录归属，写归还时间并把库存加一。
     *
     * @return 记录不存在、不属于该用户或已归还时返回 false
     */
    public boolean returnBook(long recordId, long userId) throws SQLException {
        String checkSql = "SELECT book_id FROM borrow_record "
                + "WHERE id = ? AND user_id = ? AND status = 0";
        String updateRecordSql = "UPDATE borrow_record "
                + "SET status = 1, return_time = NOW() "
                + "WHERE id = ? AND status = 0";
        String stockSql = "UPDATE book SET stock = stock + 1 WHERE id = ?";

        Connection connection = DBUtil.getConnection();

        try {
            connection.setAutoCommit(false);

            long bookId;

            try (PreparedStatement check = connection.prepareStatement(checkSql)) {
                check.setLong(1, recordId);
                check.setLong(2, userId);

                try (ResultSet resultSet = check.executeQuery()) {
                    if (!resultSet.next()) {
                        connection.rollback();
                        return false;
                    }
                    bookId = resultSet.getLong("book_id");
                }
            }

            try (PreparedStatement updateRecord = connection.prepareStatement(
                    updateRecordSql)) {

                updateRecord.setLong(1, recordId);

                if (updateRecord.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            try (PreparedStatement stock = connection.prepareStatement(stockSql)) {
                stock.setLong(1, bookId);
                stock.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    public List<BorrowRecord> findByUser(long userId) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS
                + "WHERE r.user_id = ? "
                + "ORDER BY r.borrow_time DESC, r.id DESC";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return mapRows(resultSet);
            }
        }
    }

    public List<BorrowRecord> findAll() throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS
                + "ORDER BY r.borrow_time DESC, r.id DESC";

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            return mapRows(resultSet);
        }
    }

    private List<BorrowRecord> mapRows(ResultSet resultSet) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();

        while (resultSet.next()) {
            BorrowRecord record = new BorrowRecord();
            record.setId(resultSet.getLong("id"));
            record.setUserId(resultSet.getLong("user_id"));
            record.setBookId(resultSet.getLong("book_id"));
            record.setUsername(resultSet.getString("username"));
            record.setBookTitle(resultSet.getString("title"));
            record.setBorrowTime(toLocalDateTime(
                    resultSet.getTimestamp("borrow_time")));
            record.setDueDate(resultSet.getDate("due_date").toLocalDate());
            record.setReturnTime(toLocalDateTime(
                    resultSet.getTimestamp("return_time")));
            record.setStatus(resultSet.getInt("status"));
            records.add(record);
        }

        return records;
    }

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
