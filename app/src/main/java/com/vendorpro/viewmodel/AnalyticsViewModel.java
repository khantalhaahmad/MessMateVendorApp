package com.vendorpro.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vendorpro.model.AnalyticsResponse;
import com.vendorpro.repository.AnalyticsRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsViewModel extends ViewModel {

    private final MutableLiveData<AnalyticsResponse> analyticsData = new MutableLiveData<>();

    private AnalyticsRepository repository;

    public void init(Context context) {

        repository = new AnalyticsRepository(context);
    }

    public LiveData<AnalyticsResponse> getAnalytics() {

        return analyticsData;
    }

    public void loadAnalytics(String ownerId) {

        Log.d("ANALYTICS_DEBUG","ViewModel API call for ownerId = "+ownerId);

        repository.getAnalytics(ownerId)
                .enqueue(new Callback<AnalyticsResponse>() {

                    @Override
                    public void onResponse(Call<AnalyticsResponse> call,
                                           Response<AnalyticsResponse> response) {

                        Log.d("ANALYTICS_DEBUG","Response code = "+response.code());

                        if (response.isSuccessful() && response.body() != null) {

                            Log.d("ANALYTICS_DEBUG","Analytics data received");

                            analyticsData.setValue(response.body());

                        } else {

                            Log.e("ANALYTICS_DEBUG","Response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<AnalyticsResponse> call,
                                          Throwable t) {

                        Log.e("ANALYTICS_DEBUG","API ERROR: "+t.getMessage());
                    }
                });
    }
}