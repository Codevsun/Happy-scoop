package com.example.icecreamshopsignin.Modules;

public class Order {
    private int id;
    private int userId;
    private String date;
    private double totalPrice;
    private String status;

    public Order(int userId, String date, double totalPrice, String status) {
        this.userId = userId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Existing getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // New getter and setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
