package com.vendorpro.repository;

import android.content.Context;
import android.util.Log;

import com.vendorpro.model.AnalyticsResponse;
import com.vendorpro.network.AnalyticsService;
import com.vendorpro.network.RetrofitClient;

import retrofit2.Call;

public class AnalyticsRepository {

    private static final String TAG = "ANALYTICS_REPO";

    private final AnalyticsService analyticsService;

    public AnalyticsRepository(Context context){

        Log.d(TAG,"AnalyticsRepository initialized");

        analyticsService =
                RetrofitClient
                        .getClient(context)
                        .create(AnalyticsService.class);

        Log.d(TAG,"Retrofit client created");
    }

    /* =========================
       GET ANALYTICS API
    ========================= */

    public Call<AnalyticsResponse> getAnalytics(String ownerId){

        Log.d(TAG,"getAnalytics() called");

        if(ownerId == null || ownerId.isEmpty()){
            Log.e(TAG,"OwnerId is NULL or EMPTY ❌");
        }else{
            Log.d(TAG,"OwnerId = "+ownerId);
        }

        Log.d(TAG,"Creating Retrofit API call -> analyticsService.getAnalytics()");

        Call<AnalyticsResponse> call = analyticsService.getAnalytics(ownerId);

        Log.d(TAG,"API Call object created successfully");

        return call;
    }

}