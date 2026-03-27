package com.vendorpro.model;

public class WithdrawRequest {

    private String userId;
    private double amount;

    public WithdrawRequest(String userId, double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }
}