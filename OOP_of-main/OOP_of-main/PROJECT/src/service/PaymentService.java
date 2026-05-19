package service;

import dao.Paymentdao;
import model.Payment;

public class PaymentService {
    private Paymentdao dao = new Paymentdao();

    public void createPayment(Payment p) {
        try {
            if (p.getAmount() <= 0) {
                System.out.println("Số tiền thanh toán không hợp lệ!");
                return;
            }
            dao.createPayment(p);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo thanh toán: " + e.getMessage());
        }
    }

    public void viewReceipts() {
        try {
            dao.viewReceipts();
        } catch (Exception e) {
            System.err.println("Lỗi khi xem biên lai: " + e.getMessage());
        }
    }

    public void viewReceiptsByUser(int userId) {
        try {
            dao.viewReceiptsByUser(userId);
        } catch (Exception e) {
            System.err.println("Lỗi khi xem biên lai: " + e.getMessage());
        }
    }

    public void updatePaymentStatus(int paymentId, String newStatus) {
        try {
            dao.updatePaymentStatus(paymentId, newStatus);
        } catch (Exception e) {
            System.err.println("Loi khi cap nhat trang thai thanh toan: " + e.getMessage());
        }
    }

    public String getPaymentMethodByOrderId(int orderId) {
        try {
            return dao.getPaymentMethodByOrderId(orderId);
        } catch (Exception e) {
            System.err.println("Loi khi lay phuong thuc thanh toan: " + e.getMessage());
            return null;
        }
    }

    public void updatePaymentStatusByOrderId(int orderId, String newStatus) {
        try {
            dao.updatePaymentStatusByOrderId(orderId, newStatus);
        } catch (Exception e) {
            System.err.println("Loi khi cap nhat trang thai thanh toan: " + e.getMessage());
        }
    }
}
