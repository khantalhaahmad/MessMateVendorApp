package com.vendorpro.network;

import com.vendorpro.model.DashboardStats;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DashboardService {
    @GET("owner/{ownerId}/stats")
    Call<DashboardStats> getDashboardStats(@Path("ownerId") String ownerId);
}
