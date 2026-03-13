package com.vendorpro.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class DashboardStats {

    /* -----------------------------
       Mess Info
    ------------------------------ */

    @SerializedName("messId")
    private String messId = "";

    @SerializedName("messName")
    private String messName = "";

    @SerializedName("isOpen")
    private boolean isOpen = false;

    public String getMessId() {
        return messId;
    }

    public String getMessName() {
        return messName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    /* -----------------------------
       DASHBOARD (Today Stats)
    ------------------------------ */

    @SerializedName("ordersToday")
    private int ordersToday = 0;

    @SerializedName("revenueToday")
    private double revenueToday = 0;

    @SerializedName("customersToday")
    private int customersToday = 0;

    public int getOrdersToday() {
        return ordersToday;
    }

    public double getRevenueToday() {
        return revenueToday;
    }

    public int getCustomersToday() {
        return customersToday;
    }

    /* -----------------------------
       ANALYTICS TOTAL
    ------------------------------ */

    @SerializedName("totalOrders")
    private int totalOrders = 0;

    @SerializedName("totalRevenue")
    private double totalRevenue = 0;

    @SerializedName("activeCustomers")
    private int activeCustomers = 0;

    @SerializedName("avgRating")
    private float avgRating = 0;

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public float getAvgRating() {
        return avgRating;
    }

    /* -----------------------------
       CHART DATA
    ------------------------------ */

    @SerializedName("weeklyOrders")
    private List<Integer> weeklyOrders = new ArrayList<>();

    @SerializedName("monthlyRevenue")
    private List<Double> monthlyRevenue = new ArrayList<>();

    public List<Integer> getWeeklyOrders() {
        return weeklyOrders;
    }

    public List<Double> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    /* -----------------------------
       RECENT ORDERS
    ------------------------------ */

    @SerializedName("recentOrders")
    private List<Order> recentOrders = new ArrayList<>();

    public List<Order> getRecentOrders() {
        return recentOrders;
    }
}