package com.shopauc.model;

public class Product implements Purchasable {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int sellerId;

    public Product(int id, String name, double price, int stock, int sellerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.sellerId = sellerId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public int getSellerId() { return sellerId; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public double getPrice() { return price; }

    @Override
    public boolean isAvailable() { return stock > 0; }

    @Override
    public String toString() {
        return "[PRODUCT] ID:" + id + " | " + name + " | $" + price + " | Stock:" + stock;
    }
}