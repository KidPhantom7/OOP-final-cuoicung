package dao;

import model.Cart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class Cartdao {
    // 1. Hàm thêm sản phẩm vào giỏ hàng (Đã sửa để cộng dồn nếu trùng SP)
    public void addToCart(Cart cart) {
        // Sử dụng ON DUPLICATE KEY để nếu user thêm trùng SP thì tự tăng số lượng
        String sql = "INSERT INTO cart (userId, productId, quantity) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = DBconnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cart.getUserId());
            ps.setInt(2, cart.getProductId());
            ps.setInt(3, cart.getQuantity());
            ps.setInt(4, cart.getQuantity());

            ps.executeUpdate();
            System.out.println("=> Đã thêm sản phẩm vào giỏ hàng!");
        } catch (Exception e) {
            System.out.println("Lỗi addToCart: " + e.getMessage());
        }
    }

    // 2. Hàm cập nhật số lượng theo userId + productId
    public void updateQuantity(int userId, int productId, int newQty) {
        String sql = "UPDATE cart SET quantity = ? WHERE userId = ? AND productId = ?";
        try (Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQty);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("=> Da cap nhat so luong san pham ID " + productId);
            } else {
                System.out.println("Loi: San pham ID " + productId + " khong co trong gio hang!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. Hàm xem chi tiết giỏ hàng (Yêu cầu mục 7)
    public void viewCart(int userId) {
        String sql = "SELECT c.cartId, c.productId, p.name, p.price, c.quantity, p.shopId " +
                     "FROM cart c JOIN product p ON c.productId = p.productId " +
                     "WHERE c.userId = ?";
        try (Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- GIO HANG CUA BAN ---");
            double tongTien = 0;
            boolean coSP = false;
            while (rs.next()) {
                coSP = true;
                int cartId = rs.getInt("cartId");
                int productId = rs.getInt("productId");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                int shopId = rs.getInt("shopId");
                double thanhTien = price * quantity;
                tongTien += thanhTien;
                System.out.printf("ID SP: %d | Ten: %s | Gia: %,.0f | SL: %d | Ma Shop: %d | Thanh tien: %,.0f\n",
                        productId, name, price, quantity, shopId, thanhTien);
            }
            if (!coSP) {
                System.out.println("Gio hang trong!");
            } else {
                System.out.printf("=> TONG TIEN GIO HANG: %,.0f VND\n", tongTien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Hàm xóa sản phẩm khỏi giỏ theo userId + productId
    public void removeFromCart(int userId, int productId) {
        String sql = "DELETE FROM cart WHERE userId = ? AND productId = ?";
        try (Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("=> Da xoa san pham ID " + productId + " khoi gio hang!");
            } else {
                System.out.println("Loi: San pham ID " + productId + " khong co trong gio hang!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. Hàm tính tổng tiền giỏ hàng (Yêu cầu phải có bảng product với cột price)
    public double calculateTotalAmount(int userId) {
        double total = 0;
        // Lưu ý: Lệnh JOIN này yêu cầu CSDL phải có bảng 'product' và cột 'price'.
        // Khi ghép code SQL với nhóm, đảm bảo bảng sản phẩm đúng cấu trúc này.
        String sql = "SELECT SUM(c.quantity * p.price) AS total_money " +
                     "FROM cart c JOIN product p ON c.productId = p.productId " +
                     "WHERE c.userId = ?";
        try (Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total_money");
            }
        } catch (Exception e) {
            System.out.println("Lỗi tính tổng tiền: " + e.getMessage());
        }
        return total;
    }

    // 6. Hàm lấy danh sách sản phẩm trong giỏ hàng (Để tạo Order Details)
    public List<Cart> getCartItems(int userId) {
        List<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE userId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cart c = new Cart();
                c.setId(rs.getInt("cartId"));
                c.setUserId(rs.getInt("userId"));
                c.setProductId(rs.getInt("productId"));
                c.setQuantity(rs.getInt("quantity"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 7. Hàm làm sạch giỏ hàng (Sau khi thanh toán xong)
    public void clearCart(int userId) {
        String sql = "DELETE FROM cart WHERE userId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- CÁC HÀM HỖ TRỢ KIỂM TRA SỐ LƯỢNG TỒN KHO ---

    // Lấy số lượng tồn kho của sản phẩm từ database
    public int getAvailableStock(int productId) {
        int stock = 0; 
        String sql = "SELECT stock FROM product WHERE productId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stock = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("=> [Loi] Khong lay duoc so luong ton kho: " + e.getMessage());
        }
        return stock;
    }

    // Lấy số lượng của một sản phẩm đã có trong giỏ hàng của user
    public int getQuantityInCart(int userId, int productId) {
        int qty = 0;
        String sql = "SELECT quantity FROM cart WHERE userId = ? AND productId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                qty = rs.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qty;
    }

    // Lấy productId từ cartId để phục vụ updateQuantity
    public int getProductIdByCartId(int cartId) {
        int productId = -1;
        String sql = "SELECT productId FROM cart WHERE cartId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                productId = rs.getInt("productId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productId;
    }
}
