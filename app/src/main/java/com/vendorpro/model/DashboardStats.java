package com.vendorpro.model;

import java.util.List;

public class DashboardStats {

    /* -----------------------------
       Mess
    ------------------------------ */

    private String messId;

    public String getMessId() {
        return messId;
    }

    /* -----------------------------
       DASHBOARD (Today Stats)
    ------------------------------ */

    private int ordersToday;
    private double revenueToday;
    private int customersToday;

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
       ANALYTICS
    ------------------------------ */

    private int totalOrders;
    private double totalRevenue;
    private int activeCustomers;
    private float avgRating;

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

    private List<Integer> weeklyOrders;
    private List<Double> monthlyRevenue;

    public List<Integer> getWeeklyOrders() {
        return weeklyOrders;
    }

    public List<Double> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    /* -----------------------------
       RECENT ORDERS
    ------------------------------ */

    private List<Order> recentOrders;

    public List<Order> getRecentOrders() {
        return recentOrders;
    }
}