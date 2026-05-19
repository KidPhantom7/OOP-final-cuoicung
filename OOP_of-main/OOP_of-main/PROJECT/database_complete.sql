-- ============================================================
-- FILE SQL HOÀN CHỈNH - SÀN THƯƠNG MẠI ĐIỆN TỬ (TMDT)
-- Database: btl
-- Tổng hợp từ tất cả file SQL của dự án
-- Chạy file này 1 lần duy nhất trên MySQL/HeidiSQL
-- ============================================================

-- Tạo database nếu chưa có
CREATE DATABASE IF NOT EXISTS btl
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE btl;

-- =========================
-- DROP TẤT CẢ BẢNG CŨ (theo đúng thứ tự khóa ngoại)
-- =========================
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS orderDetails;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS shop;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- 1. BẢNG USERS (Người dùng - 3 vai trò)
-- Thành viên: Vũ Đức Giang (UC 1,2,3)
-- =========================
CREATE TABLE users (
    userId      INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    fullName    VARCHAR(200) NOT NULL,
    role        ENUM('ADMIN', 'SELLER', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 2. BẢNG CATEGORY (Danh mục sản phẩm)
-- Thành viên: Lý Đình Đức (UC 4)
-- =========================
CREATE TABLE category (
    categoryId  INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 3. BẢNG SHOP (Gian hàng)
-- Thành viên: Doãn Minh Tuấn (UC 10)
-- =========================
CREATE TABLE shop (
    shopId      INT AUTO_INCREMENT PRIMARY KEY,
    userId      INT NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    address     VARCHAR(255),
    status      VARCHAR(50) DEFAULT 'active',

    FOREIGN KEY (userId)
        REFERENCES users(userId)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 4. BẢNG PRODUCT (Sản phẩm)
-- Thành viên: Lý Đình Đức (UC 5,6)
-- =========================
CREATE TABLE product (
    productId   INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    price       DOUBLE NOT NULL,
    stock       INT NOT NULL DEFAULT 0,
    categoryId  INT,
    shopId      INT,

    FOREIGN KEY (categoryId)
        REFERENCES category(categoryId)
        ON DELETE SET NULL,

    FOREIGN KEY (shopId)
        REFERENCES shop(shopId)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 5. BẢNG LOCATION (Địa chỉ nhận hàng)
-- Thành viên: Doãn Minh Tuấn (UC 12)
-- =========================
CREATE TABLE location (
    locationId  INT AUTO_INCREMENT PRIMARY KEY,
    userId      INT NOT NULL,
    detail      VARCHAR(255),
    phone       VARCHAR(20),

    FOREIGN KEY (userId)
        REFERENCES users(userId)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 6. BẢNG REVIEW (Đánh giá gian hàng)
-- Thành viên: Doãn Minh Tuấn (UC 11)
-- =========================
CREATE TABLE review (
    reviewId    INT AUTO_INCREMENT PRIMARY KEY,
    userId      INT NOT NULL,
    shopId      INT NOT NULL,
    rating      INT NOT NULL,
    comment     TEXT,

    FOREIGN KEY (userId)
        REFERENCES users(userId)
        ON DELETE CASCADE,

    FOREIGN KEY (shopId)
        REFERENCES shop(shopId)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 7. BẢNG CART (Giỏ hàng)
-- Thành viên: Trần Sách Trường (UC 7)
-- =========================
CREATE TABLE cart (
    cartId      INT AUTO_INCREMENT PRIMARY KEY,
    userId      INT NOT NULL,
    productId   INT NOT NULL,
    quantity    INT NOT NULL,

    UNIQUE KEY unique_user_product (userId, productId),

    FOREIGN KEY (userId)
        REFERENCES users(userId)
        ON DELETE CASCADE,

    FOREIGN KEY (productId)
        REFERENCES product(productId)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 8. BẢNG ORDERS (Đơn hàng)
-- Thành viên: Trần Sách Trường (UC 8)
-- LƯU Ý: PK là "orderId" để đồng bộ với of.sql
-- Nhưng DAO đang dùng "id" → cần sửa DAO hoặc dùng alias
-- Tạm thời dùng "id" để KHÔNG CẦN SỬA CODE JAVA
-- =========================
CREATE TABLE orders (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    userId      INT NOT NULL,
    totalAmount DOUBLE NOT NULL,
    status      VARCHAR(50) NOT NULL,
    orderDate   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (userId)
        REFERENCES users(userId)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 9. BẢNG ORDER DETAILS (Chi tiết đơn hàng)
-- Thành viên: Trần Sách Trường (UC 8)
-- =========================
CREATE TABLE orderDetails (
    orderId     INT NOT NULL,
    productId   INT NOT NULL,
    quantity    INT NOT NULL,

    PRIMARY KEY (orderId, productId),

    FOREIGN KEY (orderId)
        REFERENCES orders(id)
        ON DELETE CASCADE,

    FOREIGN KEY (productId)
        REFERENCES product(productId)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- 10. BẢNG PAYMENTS (Thanh toán / Biên lai)
-- Thành viên: Trần Sách Trường (UC 9)
-- LƯU Ý: Tương tự orders, dùng "id" để khớp DAO
-- =========================
CREATE TABLE payments (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    orderId         INT NOT NULL,
    paymentMethod   VARCHAR(50) NOT NULL,
    amount          DOUBLE NOT NULL,
    status          VARCHAR(50) NOT NULL,
    paymentDate     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (orderId)
        REFERENCES orders(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- DỮ LIỆU MẪU (Sample Data)
-- ============================================================

-- 1. Tài khoản ADMIN (mật khẩu: admin123)
INSERT INTO users (username, password, email, fullName, role) VALUES
    ('admin', 'admin123', 'admin@tmdt.com', 'Quan tri vien', 'ADMIN');

-- 2. Tài khoản SELLER mẫu (mật khẩu: seller123)
INSERT INTO users (username, password, email, fullName, role) VALUES
    ('seller1', 'seller123', 'seller1@tmdt.com', 'Nguoi ban hang A', 'SELLER'),
    ('seller2', 'seller123', 'seller2@tmdt.com', 'Nguoi ban hang B', 'SELLER');

-- 3. Tài khoản CUSTOMER mẫu (mật khẩu: customer123)
INSERT INTO users (username, password, email, fullName, role) VALUES
    ('customer1', 'customer123', 'customer1@tmdt.com', 'Nguyen Van A', 'CUSTOMER');

-- 4. Danh mục mặc định
INSERT INTO category (name, description) VALUES
    ('Dien tu', 'Cac san pham dien tu, cong nghe'),
    ('Thoi trang', 'Quan ao, giay dep, phu kien'),
    ('Gia dung', 'Do dung gia dinh');

-- 5. Gian hàng mẫu (mỗi seller quản lý tối đa 1 shop)
INSERT INTO shop (userId, name, description, address, status) VALUES
    (2, 'Shop Cong Nghe', 'Ban cac san pham dien tu', 'Ha Noi', 'active'),
    (3, 'Shop Thoi Trang', 'Ban quan ao thoi trang', 'TP Ho Chi Minh', 'active');

-- 6. Sản phẩm mẫu
INSERT INTO product (name, price, stock, categoryId, shopId) VALUES
    ('Tai nghe Bluetooth', 250000, 100, 1, 1),
    ('Chuot khong day', 150000, 200, 1, 1),
    ('Ao thun nam', 199000, 50, 2, 2),
    ('Quan jean nu', 350000, 30, 2, 2);

-- 7. Địa chỉ nhận hàng mẫu (customer1 có userId = 4)
INSERT INTO location (userId, detail, phone) VALUES
    (4, '123 Pho Hue, Hai Ba Trung, Ha Noi', '0912345678');

-- ============================================================
-- HOÀN TẤT! Bạn có thể đăng nhập với:
--   Admin:    admin / admin123
--   Seller 1: seller1 / seller123  
--   Seller 2: seller2 / seller123  
--   Customer: customer1 / customer123
-- ============================================================
