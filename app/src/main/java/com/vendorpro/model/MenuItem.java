package com.vendorpro.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MenuItem implements Serializable {

    /* =====================================
       MongoDB ID
    ===================================== */

    @SerializedName("_id")
    private String id;

    /* =====================================
       Basic Fields
    ===================================== */

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private double price;

    /* =====================================
       Image
    ===================================== */

    @SerializedName("image")
    private String imageUrl;

    /* =====================================
       Availability
    ===================================== */

    @SerializedName("available")
    private boolean available;

    /* =====================================
       Veg / Non Veg
    ===================================== */

    @SerializedName("isVeg")
    private boolean isVeg;

    /* =====================================
       Category
    ===================================== */

    @SerializedName("category")
    private String category;

    /* =====================================
       Constructors
    ===================================== */

    public MenuItem() {}

    public MenuItem(String name, String description, double price, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    /* =====================================
       Getters
    ===================================== */

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public String getCategory() {
        return category;
    }

    /* =====================================
       Setters
    ===================================== */

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setVeg(boolean veg) {
        this.isVeg = veg;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}