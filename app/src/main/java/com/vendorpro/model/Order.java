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
       MESS NAME
    ========================= */

    @SerializedName("mess_name")
    private String messName;

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
       ORDER EXPIRE TIMER
       (Vendor accept countdown)
    ========================= */

    @SerializedName("orderExpiresAt")
    private String orderExpiresAt;

    /* =========================
       ORDER ITEMS
    ========================= */

    @SerializedName("items")
    private List<OrderItem> items;

    /* =========================
       DELIVERY TIMESTAMPS
    ========================= */

    @SerializedName("acceptedAt")
    private String acceptedAt;

    @SerializedName("preparingAt")
    private String preparingAt;

    @SerializedName("readyAt")
    private String readyAt;

    @SerializedName("pickedAt")
    private String pickedAt;

    @SerializedName("deliveredAt")
    private String deliveredAt;

    @SerializedName("cancelledAt")
    private String cancelledAt;

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

    public String getMessName() {
        return messName;
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

    public String getOrderExpiresAt() {
        return orderExpiresAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public String getAcceptedAt() {
        return acceptedAt;
    }

    public String getPreparingAt() {
        return preparingAt;
    }

    public String getReadyAt() {
        return readyAt;
    }

    public String getPickedAt() {
        return pickedAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    public String getCancelledAt() {
        return cancelledAt;
    }

}