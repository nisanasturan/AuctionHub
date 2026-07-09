package com.shopauc.model;

import java.time.LocalDateTime;

public class Bid {
    private int bidderId;
    private int productId;
    private double amount;
    private LocalDateTime timestamp;

    public Bid(int bidderId, int productId, double amount) {
        this.bidderId = bidderId;
        this.productId = productId;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public int getBidderId() { return bidderId; }
    public int getProductId() { return productId; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Bid{buyer=" + bidderId + ", product=" + productId + ", amount=$" + amount + "}";
    }
}