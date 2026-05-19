package dao;

import model.Order;
import model.Cart;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Orderdao {

    // 1. Tạo đơn hàng mới (Dành cho Người mua - Checkout)
    // Trả về ID vừa tạo để bạn có thể dùng ID này sang bên PaymentDAO
    public int createOrder(Order order) throws Exception {
        String sql = "INSERT INTO orders (userId, totalAmount, status) VALUES (?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();) {

            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getStatus());
            ps.executeUpdate();

            // Lấy ID tự sinh từ Database
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Xem lịch sử đơn hàng của một User (Dành cho Người mua)
    public List<Order> getOrderHistory(int userId) throws Exception {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE userId = ? ORDER BY orderDate DESC";
        try (Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Sử dụng Constructor số 3 của bạn (đầy đủ tham số)
                Order o = new Order(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getDouble("totalAmount"),
                        rs.getString("status"),
                        rs.getTimestamp("orderDate"));
                list.add(o);
            }
        }
        return list;
    }

    // 3. Cập nhật trạng thái đơn hàng (Dành cho Người bán: Đang giao, Hoàn thành)
    public void updateStatus(int orderId, String newStatus) throws Exception {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("=> Đã cập nhật trạng thái đơn #" + orderId + " thành: " + newStatus);
            } else {
                System.out.println("=> LỖI: Không tìm thấy Mã đơn hàng #" + orderId + " trong hệ thống!");
            }
        }
    }

    // 4. Hủy đơn hàng (Dành cho Người mua)
    public void cancelOrder(int orderId) throws Exception {
        updateStatus(orderId, "CANCELLED");
    }

    // Lay trang thai hien tai cua don hang
    public String getOrderStatus(int orderId) throws Exception {
        String sql = "SELECT status FROM orders WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        }
        return null;
    }

    // 5. Xem đơn khách đặt (Dành cho Người bán - Xem toàn bộ hệ thống)
    public void viewAllOrders() throws Exception {
        String sql = "SELECT * FROM orders";
        try (Connection conn = DBconnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- DANH SÁCH TẤT CẢ ĐƠN HÀNG ---");
            while (rs.next()) {
                java.sql.Timestamp date = rs.getTimestamp("orderDate");
                String dateStr = (date != null) ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) : "N/A";
                System.out.printf("Mã Đơn: %d | User: %d | Tiền: %.2f | Trạng thái: %s | Ngày: %s\n",
                        rs.getInt("id"), rs.getInt("userId"), rs.getDouble("totalAmount"),
                        rs.getString("status"), dateStr);
            }
        }
    }

    // 6. Lưu chi tiết đơn hàng và trừ tồn kho
    public void saveOrderDetails(int orderId, List<Cart> cartItems) throws Exception {
        String sqlInsert = "INSERT INTO orderDetails (orderId, productId, quantity) VALUES (?, ?, ?)";
        // Thống nhất với Đức dùng cột 'stock' cho bảng product
        String sqlUpdateStock = "UPDATE product SET stock = stock - ? WHERE productId = ?";
        
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement psUpdateStock = conn.prepareStatement(sqlUpdateStock)) {
             
            for (Cart item : cartItems) {
                // Batch insert orderDetails
                psInsert.setInt(1, orderId);
                psInsert.setInt(2, item.getProductId());
                psInsert.setInt(3, item.getQuantity());
                psInsert.addBatch();
                
                // Batch update stock
                psUpdateStock.setInt(1, item.getQuantity());
                psUpdateStock.setInt(2, item.getProductId());
                psUpdateStock.addBatch();
            }
            
            psInsert.executeBatch();
            
            try {
                psUpdateStock.executeBatch();
            } catch (Exception e) {
                System.out.println("=> [Canh bao] Chua tru duoc so luong trong kho.");
            }
        }
    }
}
