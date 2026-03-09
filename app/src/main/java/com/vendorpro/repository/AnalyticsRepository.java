package com.vendorpro.repository;

import android.content.Context;

import com.vendorpro.model.AnalyticsResponse;
import com.vendorpro.network.AnalyticsService;
import com.vendorpro.network.RetrofitClient;

import retrofit2.Call;

public class AnalyticsRepository {

    private final AnalyticsService analyticsService;

    public AnalyticsRepository(Context context) {

        analyticsService =
                RetrofitClient
                        .getClient(context)
                        .create(AnalyticsService.class);
    }

    public Call<AnalyticsResponse> getAnalytics(String ownerId) {

        return analyticsService.getAnalytics(ownerId);
    }
}