package com.vendorpro.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private String name;
    private int quantity;
    private double price;

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
