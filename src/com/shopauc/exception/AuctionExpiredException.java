package com.shopauc.exception;

public class AuctionExpiredException extends Exception {
    public AuctionExpiredException(String productName) {
        super("Auction has expired for product: " + productName);
    }
}