-- 图书管理系统建库脚本
-- 使用方式：mysql -uroot -p < database/library.sql

CREATE DATABASE IF NOT EXISTS library
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE library;

CREATE TABLE IF NOT EXISTS `user` (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    username  VARCHAR(50)  NOT NULL,
    password  CHAR(64)     NOT NULL COMMENT 'SHA-256 十六进制摘要',
    real_name VARCHAR(50)  NOT NULL,
    role      VARCHAR(20)  NOT NULL DEFAULT 'admin',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS book (
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    isbn       VARCHAR(20)   NOT NULL,
    title      VARCHAR(200)  NOT NULL,
    author     VARCHAR(100)  NOT NULL,
    publisher  VARCHAR(100)  NOT NULL,
    category   VARCHAR(50)   NOT NULL,
    price      DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock      INT           NOT NULL DEFAULT 0,
    created_at DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_book_isbn (isbn)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS borrow_record (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    user_id     BIGINT      NOT NULL,
    book_id     BIGINT      NOT NULL,
    borrow_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date    DATE        NOT NULL,
    return_time DATETIME    DEFAULT NULL,
    status      TINYINT     NOT NULL DEFAULT 0 COMMENT '0 未归还，1 已归还',
    PRIMARY KEY (id),
    KEY idx_borrow_user (user_id),
    KEY idx_borrow_book (book_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 默认账号：admin / admin123
INSERT INTO `user` (username, password, real_name, role)
SELECT 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '管理员', 'admin'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE username = 'admin');

INSERT INTO book (isbn, title, author, publisher, category, price, stock)
SELECT '9787111213826', 'Java 编程思想', 'Bruce Eckel', '机械工业出版社', '计算机', 108.00, 5
WHERE NOT EXISTS (SELECT 1 FROM book WHERE isbn = '9787111213826');

INSERT INTO book (isbn, title, author, publisher, category, price, stock)
SELECT '9787115546081', '深入理解计算机系统', 'Randal E. Bryant', '人民邮电出版社', '计算机', 139.00, 3
WHERE NOT EXISTS (SELECT 1 FROM book WHERE isbn = '9787115546081');

INSERT INTO book (isbn, title, author, publisher, category, price, stock)
SELECT '9787020002207', '红楼梦', '曹雪芹', '人民文学出版社', '文学', 59.70, 10
WHERE NOT EXISTS (SELECT 1 FROM book WHERE isbn = '9787020002207');
