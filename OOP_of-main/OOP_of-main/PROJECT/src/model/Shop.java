package model;

public class Shop {
    int shopId;
    int userId;
    String name;
    String description;
    String address;
    String status;
    public Shop() {};
    public Shop(String name, String description, String address)
    {
        this.name = name;
        this.description = description;
        this.address = address;
        this.status = "active";
    }
    public Shop(int userId, String name, String description, String address)
    {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.status = "active";
    }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getShopId()
    {
        return shopId;
    }
    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
}