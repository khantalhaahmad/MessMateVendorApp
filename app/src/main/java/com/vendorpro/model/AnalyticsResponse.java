package com.vendorpro.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AnalyticsResponse {

    @SerializedName("messId")
    private String messId;

    @SerializedName("totalOrders")
    private int totalOrders;

    @SerializedName("totalRevenue")
    private double totalRevenue;

    @SerializedName("averageOrderValue")
    private double averageOrderValue;

    @SerializedName("activeCustomers")
    private int activeCustomers;

    @SerializedName("avgRating")
    private double avgRating;

    // ✅ safer (future proof)
    @SerializedName("weeklyOrders")
    private List<Integer> weeklyOrders;

    // 🔥 FIXED (MAIN ISSUE)
    @SerializedName("monthlyRevenue")
    private List<Double> monthlyRevenue;

    @SerializedName("weeklyLabels")
    private List<String> weeklyLabels;

    @SerializedName("monthlyLabels")
    private List<String> monthlyLabels;

    @SerializedName("topItems")
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

    public List<Double> getMonthlyRevenue() {   // 🔥 FIX
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

    public void setMonthlyRevenue(List<Double> monthlyRevenue) {  // 🔥 FIX
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