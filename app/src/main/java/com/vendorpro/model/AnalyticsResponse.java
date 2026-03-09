package com.vendorpro.model;

import java.util.List;

public class AnalyticsResponse {

    public String messId;

    public int totalOrders;

    public double totalRevenue;

    public int activeCustomers;

    public double avgRating;

    public List<Integer> weeklyOrders;

    public List<Integer> monthlyRevenue;

    public List<String> weeklyLabels;

    public List<String> monthlyLabels;

}