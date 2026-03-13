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

    private static final String TAG = "ANALYTICS_DEBUG";

    private final MutableLiveData<AnalyticsResponse> analyticsData = new MutableLiveData<>();

    private AnalyticsRepository repository;

    /* =========================
       INIT VIEWMODEL
    ========================= */

    public void init(Context context) {

        Log.d(TAG,"AnalyticsViewModel init() called");

        if(repository == null){

            Log.d(TAG,"Repository initialized");

            repository = new AnalyticsRepository(context);

        }else{

            Log.d(TAG,"Repository already initialized");
        }
    }

    /* =========================
       OBSERVE ANALYTICS DATA
    ========================= */

    public LiveData<AnalyticsResponse> getAnalytics() {

        Log.d(TAG,"getAnalytics() observer attached");

        return analyticsData;
    }

    /* =========================
       LOAD ANALYTICS DATA
    ========================= */

    public void loadAnalytics(String ownerId) {

        Log.d(TAG,"loadAnalytics() called");

        if(ownerId == null || ownerId.isEmpty()){
            Log.e(TAG,"OwnerId is NULL or EMPTY ❌");
            return;
        }

        Log.d(TAG,"OwnerId = "+ownerId);

        if(repository == null){
            Log.e(TAG,"Repository is NULL ❌ (init() not called)");
            return;
        }

        Log.d(TAG,"Calling repository.getAnalytics() API");

        repository.getAnalytics(ownerId)
                .enqueue(new Callback<AnalyticsResponse>() {

                    @Override
                    public void onResponse(Call<AnalyticsResponse> call,
                                           Response<AnalyticsResponse> response) {

                        Log.d(TAG,"API RESPONSE RECEIVED");
                        Log.d(TAG,"Response Code = "+response.code());

                        if(response.body()!=null){
                            Log.d(TAG,"Response Body Not Null ✅");
                        }else{
                            Log.e(TAG,"Response Body NULL ❌");
                        }

                        if (response.isSuccessful() && response.body() != null) {

                            AnalyticsResponse data = response.body();

                            Log.d(TAG, "Revenue = " + data.getTotalRevenue());
                            Log.d(TAG, "Orders = " + data.getTotalOrders());
                            Log.d(TAG, "Customers = " + data.getActiveCustomers());
                            analyticsData.setValue(data);

                            Log.d(TAG,"LiveData updated successfully");

                        } else {

                            Log.e(TAG,"Response not successful ❌");
                        }
                    }

                    @Override
                    public void onFailure(Call<AnalyticsResponse> call,
                                          Throwable t) {

                        Log.e(TAG,"API FAILURE ❌");
                        Log.e(TAG,"Error message = "+t.getMessage());
                    }
                });
    }
}