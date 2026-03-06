package com.vendorpro.model;

import java.util.List;

public class DashboardStats {

    // 🔥 IMPORTANT (backend se directly aata hai)
    private String messId;

    private int totalOrders;
    private double totalRevenue;
    private int activeCustomers;
    private float avgRating;
    private List<Integer> weeklyOrders;

    public String getMessId() {
        return messId;
    }

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

    public List<Integer> getWeeklyOrders() {
        return weeklyOrders;
    }
}