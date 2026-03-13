package com.vendorpro.network;

import com.vendorpro.model.DashboardStats;
import com.vendorpro.model.ToggleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface DashboardService {

    /*
    =====================================
        DASHBOARD STATS API
    =====================================

    Final URL:
    http://10.0.2.2:4000/api/owner/{ownerId}/stats
    */

    @GET("owner/{ownerId}/stats")
    Call<DashboardStats> getDashboardStats(
            @Path("ownerId") String ownerId
    );


    /*
    =====================================
        TOGGLE RESTAURANT OPEN/CLOSE
    =====================================

    Final URL:
    http://10.0.2.2:4000/api/mess/{messId}/toggle-open
    */

    @PATCH("mess/{messId}/toggle-open")
    Call<ToggleResponse> toggleRestaurant(
            @Path("messId") String messId
    );

}