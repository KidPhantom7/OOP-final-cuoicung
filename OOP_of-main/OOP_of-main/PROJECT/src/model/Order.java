package model;
import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private double totalAmount;
    private String status; 
    private Timestamp orderDate; // Tên này khớp với logic order_date trong SQL

    // 1. Constructor không tham số
    public Order() {}

    // 2. Constructor dùng khi Người mua chốt đơn (chưa có ID và ngày - DB tự sinh)
    public Order(int userId, double totalAmount, String status) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // 3. Constructor đầy đủ dùng khi lấy dữ liệu từ DB lên để hiển thị lịch sử
    public Order(int id, int userId, double totalAmount, String status, Timestamp orderDate) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    // --- Getter và Setter ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
}
