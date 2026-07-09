package com.shopauc.model;

public class Buyer extends User {

    public Buyer(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String toString() {
        return "[BUYER] " + super.toString();
    }
}