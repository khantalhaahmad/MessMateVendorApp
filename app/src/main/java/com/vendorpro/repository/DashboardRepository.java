package com.vendorpro.repository;

import android.content.Context;

import com.vendorpro.model.DashboardStats;
import com.vendorpro.network.DashboardService;
import com.vendorpro.network.Resource;
import com.vendorpro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.lifecycle.MutableLiveData;

public class DashboardRepository {

    private DashboardService dashboardService;

    public DashboardRepository(Context context) {
        dashboardService = RetrofitClient.getClient(context).create(DashboardService.class);
    }

    public MutableLiveData<Resource<DashboardStats>> getDashboardStats(String ownerId) {
        MutableLiveData<Resource<DashboardStats>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        
        dashboardService.getDashboardStats(ownerId).enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                if (response.isSuccessful()) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error("Error fetching stats", null));
                }
            }

            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });
        
        return data;
    }
}
