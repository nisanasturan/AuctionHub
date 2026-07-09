package com.shopauc.model;

public class Seller extends User {

    public Seller(int id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String toString() {
        return "[SELLER] " + super.toString();
    }
}