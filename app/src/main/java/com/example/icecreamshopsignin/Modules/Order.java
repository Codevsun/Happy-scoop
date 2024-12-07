package com.example.icecreamshopsignin.Modules;

public class Order {
    private int id;
    private String date;
    private double totalPrice;
    private String status;

    public Order(String date, double totalPrice, String status) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
