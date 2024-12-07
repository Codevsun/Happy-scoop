package com.example.icecreamshopsignin.Modules;

public class IceCream {
    private int id;
    private String name;
    private double price;
    private String description;
    private byte[] image;

    public IceCream(String name, double price, String description, byte[] image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }
}
