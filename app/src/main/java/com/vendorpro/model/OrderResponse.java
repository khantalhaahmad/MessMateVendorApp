package com.vendorpro.model;

import java.util.List;

public class OrderResponse {

    private boolean success;
    private List<Order> orders;

    public boolean isSuccess() {
        return success;
    }

    public List<Order> getOrders() {
        return orders;
    }
}