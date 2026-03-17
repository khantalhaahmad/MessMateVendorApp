package com.vendorpro.model;

public class WalletResponse {

    private boolean success;
    private double wallet;
    private double pending;
    private double paid;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public double getWallet() {
        return wallet;
    }

    public double getPending() {
        return pending;
    }

    public double getPaid() {
        return paid;
    }

    // Setters (optional)
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }
}