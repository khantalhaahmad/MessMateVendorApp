package com.vendorpro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    /* =========================
       ORDER ID
    ========================= */

    @SerializedName("_id")
    private String id;

    /* =========================
       CUSTOMER NAME
    ========================= */

    @SerializedName("customerName")
    private String customerName;

    /* =========================
       TOTAL PRICE
    ========================= */

    @SerializedName("total_price")
    private double totalAmount;

    /* =========================
       STATUS
    ========================= */

    @SerializedName("status")
    private String status;

    /* =========================
       CREATED TIME
    ========================= */

    @SerializedName("createdAt")
    private String createdAt;

    /* =========================
       ORDER ITEMS
    ========================= */

    @SerializedName("items")
    private List<OrderItem> items;

    /* =========================
       GETTERS
    ========================= */

    public String getId() {
        return id;
    }

    public String getCustomerName() {

        if (customerName == null || customerName.isEmpty()) {
            return "Customer";
        }

        return customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {

        if (status == null) return "PENDING";

        return status.toUpperCase();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

}