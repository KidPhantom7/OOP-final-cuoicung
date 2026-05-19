package dao;

import model.Payment;
import java.sql.*;

public class Paymentdao {

    // 1. Tạo biên lai thanh toán (Mục 9)
    public void createPayment(Payment p) throws Exception {
        String status = p.getMethod().equals("COD") ? "Chua thanh toan" : "Da thanh toan";

        String sql = "INSERT INTO payments (orderId, paymentMethod, amount, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getOrderId());
            ps.setString(2, p.getMethod());
            ps.setDouble(3, p.getAmount());
            ps.setString(4, status);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi createPayment: " + e.getMessage());
        }
    }

    // 2. Xem biên lai lịch sử (Mục 9)
    public void viewReceipts() throws Exception {
        String sql = "SELECT * FROM payments";
        try (Connection conn = DBconnection.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\n--- DANH SÁCH BIÊN LAI ---");
            boolean coDuLieu = false;
            while (rs.next()) {
                coDuLieu = true;
                java.sql.Timestamp date = rs.getTimestamp("paymentDate");
                String dateStr = (date != null) ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) : "N/A";
                System.out.printf("Mã BL: %d | Đơn: %d | Phương thức: %s | Tiền: %,.0f VND | Trạng thái: %s | Ngày: %s\n",
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        dateStr);
            }
            if (!coDuLieu)
                System.out.println("Chưa có giao dịch nào!");
        }
    }

    // Xem biên lai của một user cụ thể
    public void viewReceiptsByUser(int userId) throws Exception {
        String sql = "SELECT p.* FROM payments p JOIN orders o ON p.orderId = o.id WHERE o.userId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- DANH SÁCH BIÊN LAI ---");
            boolean coDuLieu = false;
            while (rs.next()) {
                coDuLieu = true;
                java.sql.Timestamp date = rs.getTimestamp("paymentDate");
                String dateStr = (date != null) ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) : "N/A";
                System.out.printf("Mã BL: %d | Đơn: %d | Phương thức: %s | Tiền: %,.0f VND | Trạng thái: %s | Ngày: %s\n",
                        rs.getInt("id"),
                        rs.getInt("orderId"),
                        rs.getString("paymentMethod"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        dateStr);
            }
            if (!coDuLieu)
                System.out.println("Chưa có giao dịch nào!");
        }
    }

    // 3. Cập nhật trạng thái thu tiền (Mục 9)
    public void updatePaymentStatus(int paymentId, String newStatus) throws Exception {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";
        try (Connection conn = DBconnection.getConnection();) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newStatus);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
            System.out.println("=> Da cap nhat trang thai thanh toan #" + paymentId + " thanh: " + newStatus);
        }
    }

    // 4. Lay phuong thuc thanh toan theo ma don hang
    public String getPaymentMethodByOrderId(int orderId) throws Exception {
        String sql = "SELECT paymentMethod FROM payments WHERE orderId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("paymentMethod");
            }
        }
        return null;
    }

    // 5. Cap nhat trang thai thanh toan theo ma don hang
    public void updatePaymentStatusByOrderId(int orderId, String newStatus) throws Exception {
        String sql = "UPDATE payments SET status = ? WHERE orderId = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
}
