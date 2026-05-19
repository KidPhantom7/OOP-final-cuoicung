package model;
import java.sql.Timestamp;

public class Payment {
    private int id;
    private int orderId;
    private String method;
    private double amount;
    private String status;
    private Timestamp paymentDate;

    public Payment() {}
    public Payment(int orderId, String method, double amount) {
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
    }

    // Getter và Setter
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public int getID() { return id;}
    public void setID(int id) {this.id = id;}
    public Timestamp getPaymentDate() {return paymentDate;}
    public void setPaymentDate(Timestamp paymentDate) {this.paymentDate = paymentDate;}
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
