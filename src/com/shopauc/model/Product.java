package com.shopauc.model;

public class Product implements Purchasable {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int sellerId;
    private String category;

    public Product(int id, String name, double price, int stock, int sellerId, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sellerId = sellerId;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public int getSellerId() { return sellerId; }
    public String getCategory() { return category; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public boolean isAvailable() { return stock > 0; }

    @Override
    public String toString() {
        return "[PRODUCT] " + name + " | $" + price + " | Stock:" + stock + " | " + category;
    }
}