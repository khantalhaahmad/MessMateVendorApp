package com.vendorpro.network;

import android.util.Log;

import com.vendorpro.model.AnalyticsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AnalyticsService {

    /*
    =====================================
        ANALYTICS API
    =====================================

    Base URL Example:
    http://10.0.2.2:4000/api/

    Final Endpoint:
    http://10.0.2.2:4000/api/owner/{ownerId}/stats

    Debug Flow:
    1️⃣ ViewModel -> loadAnalytics()
    2️⃣ Repository -> getAnalytics()
    3️⃣ Retrofit -> this API call
    4️⃣ Backend -> returns stats
    */

    @GET("owner/{ownerId}/stats")
    Call<AnalyticsResponse> getAnalytics(
            @Path("ownerId") String ownerId
    );

}