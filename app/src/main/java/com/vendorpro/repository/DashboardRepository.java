package com.vendorpro.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.DashboardStats;
import com.vendorpro.model.ToggleResponse;
import com.vendorpro.network.DashboardService;
import com.vendorpro.network.Resource;
import com.vendorpro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardRepository {

    private static final String TAG = "DASHBOARD_API";

    private DashboardService dashboardService;

    public DashboardRepository(Context context) {

        Log.d(TAG, "DashboardRepository initialized");

        dashboardService = RetrofitClient
                .getClient(context)
                .create(DashboardService.class);
    }

    /* ===============================
       FETCH DASHBOARD STATS
    =============================== */

    public MutableLiveData<Resource<DashboardStats>> getDashboardStats(String ownerId) {

        Log.d(TAG, "getDashboardStats() called");

        if(ownerId == null){
            Log.e(TAG, "OwnerId is NULL ❌");
        } else {
            Log.d(TAG, "OwnerId: " + ownerId);
        }

        MutableLiveData<Resource<DashboardStats>> data = new MutableLiveData<>();

        data.setValue(Resource.loading(null));

        Log.d(TAG, "Calling API -> dashboardService.getDashboardStats()");

        dashboardService.getDashboardStats(ownerId)
                .enqueue(new Callback<DashboardStats>() {

                    @Override
                    public void onResponse(Call<DashboardStats> call,
                                           Response<DashboardStats> response) {

                        Log.d(TAG, "API Response Received");
                        Log.d(TAG, "Response Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            Log.d(TAG,"Revenue: " + response.body().getTotalRevenue());
                            Log.d(TAG,"Orders: " + response.body().getTotalOrders());
                            Log.d(TAG,"Customers: " + response.body().getActiveCustomers());

                            data.setValue(Resource.success(response.body()));

                        } else {

                            Log.e(TAG, "API ERROR ❌");
                            Log.e(TAG, "Response Body NULL or Not Successful");

                            data.setValue(Resource.error(
                                    "Error fetching stats",
                                    null
                            ));
                        }
                    }

                    @Override
                    public void onFailure(Call<DashboardStats> call, Throwable t) {

                        Log.e(TAG, "API FAILED ❌");
                        Log.e(TAG, "Error: " + t.getMessage());

                        data.setValue(Resource.error(
                                t.getMessage(),
                                null
                        ));
                    }
                });

        return data;
    }

    /* ===============================
       TOGGLE RESTAURANT OPEN/CLOSE
    =============================== */

    public MutableLiveData<Resource<ToggleResponse>> toggleRestaurant(String messId) {

        Log.d(TAG, "toggleRestaurant() called");

        if(messId == null){
            Log.e(TAG, "MessId NULL ❌");
        } else {
            Log.d(TAG, "MessId: " + messId);
        }

        MutableLiveData<Resource<ToggleResponse>> data = new MutableLiveData<>();

        data.setValue(Resource.loading(null));

        Log.d(TAG, "Calling API -> toggleRestaurant()");

        dashboardService.toggleRestaurant(messId)
                .enqueue(new Callback<ToggleResponse>() {

                    @Override
                    public void onResponse(Call<ToggleResponse> call,
                                           Response<ToggleResponse> response) {

                        Log.d(TAG, "Toggle API Response Code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            Log.d(TAG, "Toggle SUCCESS ✅");

                            data.setValue(Resource.success(response.body()));

                        } else {

                            Log.e(TAG, "Toggle FAILED ❌");

                            data.setValue(Resource.error(
                                    "Failed to update restaurant status",
                                    null
                            ));
                        }
                    }

                    @Override
                    public void onFailure(Call<ToggleResponse> call, Throwable t) {

                        Log.e(TAG, "Toggle API FAILURE ❌");
                        Log.e(TAG, "Error: " + t.getMessage());

                        data.setValue(Resource.error(
                                t.getMessage(),
                                null
                        ));
                    }
                });

        return data;
    }

}