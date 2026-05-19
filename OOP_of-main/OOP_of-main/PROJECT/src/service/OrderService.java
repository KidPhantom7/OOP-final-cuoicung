package service;

import dao.Orderdao;
import model.Order;
import model.Cart;
import java.util.List;

public class OrderService {
    private Orderdao dao = new Orderdao();

    public int createOrder(Order order) {
        try {
            if (order.getTotalAmount() <= 0) {
                System.out.println("Tổng tiền không hợp lệ!");
                return 0;
            }
            return dao.createOrder(order);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
            return 0;
        }
    }

    public List<Order> getOrderHistory(int userId) {
        try {
            return dao.getOrderHistory(userId);
        } catch (Exception e) {
            System.err.println("Lỗi lấy lịch sử đơn hàng: " + e.getMessage());
            return null;
        }
    }

    public void updateStatus(int orderId, String newStatus) {
        try {
            dao.updateStatus(orderId, newStatus);
        } catch (Exception e) {
            System.err.println("Loi cap nhat trang thai: " + e.getMessage());
        }
    }

    public String getOrderStatus(int orderId) {
        try {
            return dao.getOrderStatus(orderId);
        } catch (Exception e) {
            System.err.println("Loi lay trang thai don hang: " + e.getMessage());
            return null;
        }
    }

    public void cancelOrder(int orderId) {
        try {
            dao.cancelOrder(orderId);
            System.out.println("=> Đã hủy đơn hàng #" + orderId);
        } catch (Exception e) {
            System.err.println("Lỗi hủy đơn hàng: " + e.getMessage());
        }
    }

    public void viewAllOrders() {
        try {
            dao.viewAllOrders();
        } catch (Exception e) {
            System.err.println("Lỗi lấy danh sách đơn hàng: " + e.getMessage());
        }
    }

    public void saveOrderDetails(int orderId, List<Cart> cartItems) {
        try {
            if (cartItems != null && !cartItems.isEmpty()) {
                dao.saveOrderDetails(orderId, cartItems);
            }
        } catch (Exception e) {
            System.err.println("Lỗi lưu chi tiết đơn hàng: " + e.getMessage());
        }
    }
}
