package org.example.library.util.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 借阅记录（JavaBean），对应 borrow_record 表。
 */
public class BorrowRecord {

    private static final DateTimeFormatter DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private long id;
    private long userId;
    private long bookId;

    private String username;
    private String bookTitle;

    private LocalDateTime borrowTime;
    private LocalDate dueDate;
    private LocalDateTime returnTime;

    /** 0 未归还，1 已归还 */
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDateTime getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(LocalDateTime borrowTime) {
        this.borrowTime = borrowTime;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 未归还且已超过应还日期时视为逾期。
     */
    public boolean isOverdue() {
        return status == 0
                && dueDate != null
                && dueDate.isBefore(LocalDate.now());
    }

    public String getBorrowTimeText() {
        return borrowTime == null ? "" : DATE_TIME_FORMAT.format(borrowTime);
    }

    public String getDueDateText() {
        return dueDate == null ? "" : dueDate.toString();
    }

    public String getReturnTimeText() {
        return returnTime == null ? "" : DATE_TIME_FORMAT.format(returnTime);
    }
}
