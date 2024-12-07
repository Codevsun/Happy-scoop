// Order.java
package com.example.icecreamshopsignin.Modules;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private int id;
    private int userId;
    private String date;
    private double totalPrice;
    private String status;
    private List<OrderItem> items;

    public Order(int id, int userId, String date, double totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}