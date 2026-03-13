package com.vendorpro.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.DashboardStats;
import com.vendorpro.model.ToggleResponse;
import com.vendorpro.network.Resource;
import com.vendorpro.repository.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {

    private static final String TAG = "DASHBOARD_DEBUG";

    private DashboardRepository repository;

    private MutableLiveData<Resource<DashboardStats>> dashboardStats;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        Log.d(TAG, "DashboardViewModel Initialized");

        repository = new DashboardRepository(application);
    }

    /* =========================
       FETCH DASHBOARD STATS
    ========================= */

    public LiveData<Resource<DashboardStats>> getDashboardStats(String ownerId) {

        Log.d(TAG, "getDashboardStats() called");

        if(ownerId == null){
            Log.e(TAG, "OwnerId is NULL ❌");
        }else{
            Log.d(TAG, "OwnerId: " + ownerId);
        }

        if (dashboardStats == null) {

            Log.d(TAG, "DashboardStats LiveData is NULL -> fetching from repository");

            dashboardStats = repository.getDashboardStats(ownerId);

        } else {

            Log.d(TAG, "DashboardStats already exists -> returning cached data");

        }

        return dashboardStats;
    }

    public void refreshStats(String ownerId) {

        Log.d(TAG, "refreshStats() called");

        if(ownerId == null){
            Log.e(TAG, "OwnerId NULL in refreshStats ❌");
        }

        dashboardStats = repository.getDashboardStats(ownerId);
    }

    /* =========================
       TOGGLE RESTAURANT STATUS
    ========================= */

    public LiveData<Resource<ToggleResponse>> toggleRestaurant(String messId){

        Log.d(TAG, "toggleRestaurant() called");

        if(messId == null){
            Log.e(TAG, "MessId is NULL ❌");
        }else{
            Log.d(TAG, "MessId: " + messId);
        }

        return repository.toggleRestaurant(messId);
    }
}