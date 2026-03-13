package com.vendorpro.model;

import java.util.List;

public class AnalyticsResponse {

    private String messId;

    private int totalOrders;

    private double totalRevenue;

    private double averageOrderValue;

    private int activeCustomers;

    private double avgRating;

    private List<Integer> weeklyOrders;

    private List<Integer> monthlyRevenue;

    private List<String> weeklyLabels;

    private List<String> monthlyLabels;

    private List<TopItem> topItems;

    /* =========================
       GETTERS
    ========================= */

    public String getMessId() {
        return messId;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getAverageOrderValue() {
        return averageOrderValue;
    }

    public int getActiveCustomers() {
        return activeCustomers;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public List<Integer> getWeeklyOrders() {
        return weeklyOrders;
    }

    public List<Integer> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public List<String> getWeeklyLabels() {
        return weeklyLabels;
    }

    public List<String> getMonthlyLabels() {
        return monthlyLabels;
    }

    public List<TopItem> getTopItems() {
        return topItems;
    }

    /* =========================
       SETTERS
    ========================= */

    public void setMessId(String messId) {
        this.messId = messId;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setAverageOrderValue(double averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public void setActiveCustomers(int activeCustomers) {
        this.activeCustomers = activeCustomers;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public void setWeeklyOrders(List<Integer> weeklyOrders) {
        this.weeklyOrders = weeklyOrders;
    }

    public void setMonthlyRevenue(List<Integer> monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public void setWeeklyLabels(List<String> weeklyLabels) {
        this.weeklyLabels = weeklyLabels;
    }

    public void setMonthlyLabels(List<String> monthlyLabels) {
        this.monthlyLabels = monthlyLabels;
    }

    public void setTopItems(List<TopItem> topItems) {
        this.topItems = topItems;
    }
}