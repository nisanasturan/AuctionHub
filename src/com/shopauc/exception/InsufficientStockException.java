package com.shopauc.exception;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String productName) {
        super("Insufficient stock for product: " + productName);
    }
}