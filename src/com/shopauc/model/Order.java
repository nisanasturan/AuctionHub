package com.shopauc.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int buyerId;
    private List<CartItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;

    public Order(int id, int buyerId, List<CartItem> items) {
        this.id = id;
        this.buyerId = buyerId;
        this.items = items;
        this.totalAmount = items.stream().mapToDouble(CartItem::getTotalPrice).sum();
        this.orderDate = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    public int getId() { return id; }
    public int getBuyerId() { return buyerId; }
    public List<CartItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Order#" + id + " | Buyer:" + buyerId + " | Total:$" + totalAmount + " | " + status;
    }
}