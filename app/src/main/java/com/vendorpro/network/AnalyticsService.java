package com.vendorpro.network;

import com.vendorpro.model.AnalyticsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AnalyticsService {

    // ✅ Correct backend endpoint
    // Final URL:
    // http://10.0.2.2:4000/api/owner/{ownerId}/stats

    @GET("owner/{ownerId}/stats")
    Call<AnalyticsResponse> getAnalytics(
            @Path("ownerId") String ownerId
    );

}