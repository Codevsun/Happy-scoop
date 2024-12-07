package com.example.icecreamshopsignin.Modules;

public class CartItem {
    private int id;
    private int userId;
    private int menuItemId;
    private int quantity;
    private String customizations;
    private String name;
    private double price;

    public CartItem(int userId, int menuItemId, int quantity, String customizations) {
        this.userId = userId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.customizations = customizations;
    }

    // Existing getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getCustomizations() { return customizations; }
    public void setCustomizations(String customizations) { this.customizations = customizations; }

    // New getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
