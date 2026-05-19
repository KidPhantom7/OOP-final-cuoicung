package model;

public class Cart {
    private int cartId;
    private int userId;
    private int productId;
    private String productName; // Thêm để hiển thị
    private int quantity;
    private double price;       // Thêm để tính toán tiền

    public Cart() {
    }

    // Constructor đầy đủ để dùng trong CartDAO khi lấy dữ liệu từ DB
    public Cart(int cartId, int userId, int productId, String productName, int quantity, double price) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    // Constructor rút gọn để dùng khi Người mua thêm hàng mới vào giỏ
    public Cart(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getter/Setter cho các thuộc tính mới
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // Getter/Setter 
    public int getId() { return cartId; }
    public void setId(int cartId) { this.cartId = cartId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Thêm hàm bổ trợ để tính tiền nhanh
    public double getSubTotal() {
        return this.quantity * this.price;
    }
}
