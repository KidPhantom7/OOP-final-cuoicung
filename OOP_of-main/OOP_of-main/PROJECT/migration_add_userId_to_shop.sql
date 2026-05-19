-- ============================================================
-- MIGRATION: Them cot userId vao bang shop
-- Chay file nay de cap nhat database dang co ma KHONG xoa du lieu
-- Database: btl
-- ============================================================

USE btl;

-- BUOC 1: Them cot userId vao bang shop (neu chua co)
ALTER TABLE shop
    ADD COLUMN IF NOT EXISTS userId INT NULL AFTER shopId;

-- BUOC 2: Them khoa ngoai lien ket shop.userId -> users.userId
--   (chi chay duoc sau khi tat ca shop da duoc gan userId hop le)
--   Tam thoi bo qua de uu tien cap nhat du lieu truoc.

-- BUOC 3: Xem danh sach user co role SELLER de biet userId cua ho
SELECT userId, username, fullName, role
FROM users
WHERE role = 'SELLER';

-- BUOC 4: Xem danh sach shop hien tai
SELECT shopId, userId, name, status
FROM shop;

-- ============================================================
-- BUOC 5: GAP userId CUA SELLER VAO TUNG SHOP
-- Sua lai userId cho dung voi seller so huu shop do.
--
-- Cach tim userId: xem ket qua query o Buoc 3 phia tren.
-- Vi du: seller1 co userId = 2
--
-- Neu ban chi co 1 seller va muon gan het shop cho seller do:
-- ============================================================

-- Gan tat ca shop chua co userId cho seller1 (userId=2, sua lai neu khac)
UPDATE shop
SET userId = (SELECT userId FROM users WHERE username = 'seller1' LIMIT 1)
WHERE userId IS NULL;

-- Hoac cap nhat tung shop cu the theo shopId:
-- UPDATE shop SET userId = 2 WHERE shopId = 1;
-- UPDATE shop SET userId = 2 WHERE shopId = 2;

-- ============================================================
-- BUOC 6: Sau khi tat ca shop da co userId, them khoa ngoai
-- ============================================================

-- Kiem tra xem con shop nao chua gan userId khong
SELECT shopId, name FROM shop WHERE userId IS NULL;

-- Neu ket qua rong (0 dong), chay lenh them khoa ngoai phia duoi:
ALTER TABLE shop
    MODIFY COLUMN userId INT NOT NULL UNIQUE,
    ADD CONSTRAINT fk_shop_user
        FOREIGN KEY (userId) REFERENCES users(userId)
        ON DELETE CASCADE;

-- Kiem tra ket qua cuoi cung
SELECT s.shopId, s.userId, s.name, s.status, u.username, u.fullName
FROM shop s
JOIN users u ON s.userId = u.userId;
