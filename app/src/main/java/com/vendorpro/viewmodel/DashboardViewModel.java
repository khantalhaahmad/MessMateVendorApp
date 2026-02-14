package com.vendorpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.DashboardStats;
import com.vendorpro.repository.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {

    private DashboardRepository repository;
    private MutableLiveData<com.vendorpro.network.Resource<DashboardStats>> dashboardStats;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        repository = new DashboardRepository(application);
    }

    public LiveData<com.vendorpro.network.Resource<DashboardStats>> getDashboardStats(String ownerId) {
        if (dashboardStats == null) {
            dashboardStats = repository.getDashboardStats(ownerId);
        }
        return dashboardStats;
    }
    
    public void refreshStats(String ownerId) {
        dashboardStats = repository.getDashboardStats(ownerId);
    }
}
