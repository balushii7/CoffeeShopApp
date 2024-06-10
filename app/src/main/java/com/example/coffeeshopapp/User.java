package com.example.coffeeshopapp;

public class User {
    private String name, email, address, phone;

    // Empty constructor needed for Firestore serialization
    public User() {}

    public User(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    // Getters for Firestore
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}