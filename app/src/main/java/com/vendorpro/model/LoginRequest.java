package com.vendorpro.model;

public class LoginRequest {
    private String phoneNumber;

    public LoginRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
