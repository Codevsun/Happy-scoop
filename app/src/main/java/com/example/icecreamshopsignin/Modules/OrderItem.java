package com.example.icecreamshopsignin.Modules;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int menuItemId;
    private int quantity;
    private String name;
    private double price;
    private String customizations;

    public OrderItem(int menuItemId, int quantity, String name, double price, String customizations) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.customizations = customizations;
    }

    public double getItemTotal() {
        return price * quantity;
    }

    // Getters and setters
    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getCustomizations() { return customizations; }
    public void setCustomizations(String customizations) { this.customizations = customizations; }
}