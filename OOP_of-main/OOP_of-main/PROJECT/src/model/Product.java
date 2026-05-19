package model;

public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;
    private int categoryId; // Khóa ngoại liên kết tới Category
    private int shopId; // Khóa ngoại liên kết tới Shop

    public Product() {}

    public Product(int productId, String name, double price, int stock, int categoryId, int shopId) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
        this.shopId = shopId;
    }

    // Getters và Setters
    public int getId() { return productId; }
    public void setId(int id) { this.productId = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getShopId() { return shopId; }
    public void setShopId(int shopId) { this.shopId = shopId; }
}
