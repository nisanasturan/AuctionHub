package com.shopauc.exception;

public class InvalidBidException extends Exception {
    public InvalidBidException(double currentBid, double attemptedBid) {
        super("Invalid bid: $" + attemptedBid + " must be higher than current bid: $" + currentBid);
    }
}