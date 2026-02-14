package com.vendorpro.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String id;
    private String customerName;
    private double totalAmount;
    private String status; // PENDING, ACCEPTED, COMPLETED, REJECTED
    private String createdAt;
    private List<OrderItem> items;

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
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
