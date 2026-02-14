package com.vendorpro.model;

import java.util.List;

public class DashboardStats {
    private int totalOrders;
    private double totalRevenue;
    private int activeCustomers;
    private float averageRating;
    private List<Integer> weeklyOrders;

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public List<Integer> getWeeklyOrders() {
        return weeklyOrders;
    }
}
