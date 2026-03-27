package com.vendorpro.model;

public class WalletResponse {

    private boolean success;
    private double wallet;
    private double pending;
    private double processing; // 🔥 NEW FIELD
    private double paid;

    /* ============================
       GETTERS
    ============================ */

    public boolean isSuccess() {
        return success;
    }

    public double getWallet() {
        return wallet;
    }

    public double getPending() {
        return pending;
    }

    public double getProcessing() {   // 🔥 NEW
        return processing;
    }

    public double getPaid() {
        return paid;
    }

    /* ============================
       SETTERS (OPTIONAL)
    ============================ */

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

    public void setProcessing(double processing) { // 🔥 NEW
        this.processing = processing;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }
}