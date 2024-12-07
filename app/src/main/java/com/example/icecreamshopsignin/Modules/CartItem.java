package com.example.icecreamshopsignin.Modules;

public class CartItem {
    private int id;
    private int menuItemId;
    private int quantity;
    private String customizations;

    public CartItem(int menuItemId, int quantity, String customizations) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.customizations = customizations;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getCustomizations() { return customizations; }
    public void setCustomizations(String customizations) { this.customizations = customizations; }
}
