package com.shopauc.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionProduct extends Product {
    private double startingPrice;
    private double currentBid;
    private int highestBidderId;
    private String highestBidderName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<double[]> bidHistory;

    public AuctionProduct(int id, String name, double startingPrice, int sellerId, LocalDateTime endTime, String category) {
        super(id, name, startingPrice, 1, sellerId, category);
        this.startingPrice = startingPrice;
        this.currentBid = startingPrice;
        this.highestBidderId = -1;
        this.highestBidderName = "No bids yet";
        this.startTime = LocalDateTime.now();
        this.endTime = endTime;
        this.bidHistory = new ArrayList<>();
    }

    public double getCurrentBid() { return currentBid; }
    public double getStartingPrice() { return startingPrice; }
    public int getHighestBidderId() { return highestBidderId; }
    public String getHighestBidderName() { return highestBidderName; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getBidCount() { return bidHistory.size(); }

    public void placeBid(double amount, int bidderId, String bidderName) {
        this.currentBid = amount;
        this.highestBidderId = bidderId;
        this.highestBidderName = bidderName;
        this.bidHistory.add(new double[]{amount, bidderId});
    }

    public void placeBid(double amount, int bidderId) {
        this.currentBid = amount;
        this.highestBidderId = bidderId;
        if (bidderId == -1) this.highestBidderName = "No bids yet";
        this.bidHistory.add(new double[]{amount, bidderId});
    }

    public boolean isExpired() { return LocalDateTime.now().isAfter(endTime); }

    public boolean isWinner(int userId) {
        return isExpired() && highestBidderId == userId;
    }

    @Override
    public String toString() {
        return "[AUCTION] " + getName() + " | $" + currentBid + " | Ends:" + endTime;
    }
}