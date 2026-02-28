package com.vendorpro.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vendorpro.network.TokenManager;

public class SplashViewModel extends AndroidViewModel {

    public enum Destination {
        LOGIN,
        DASHBOARD
    }

    private final MutableLiveData<Destination> navigationEvent =
            new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application) {
        super(application);
        decideNavigation();
    }

    private void decideNavigation() {

        // ⏳ Startup delay (branding + cold start)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            String token =
                    TokenManager.getInstance(getApplication()).getToken();

            if (token != null && !token.isEmpty()) {
                navigationEvent.setValue(Destination.DASHBOARD);
            } else {
                navigationEvent.setValue(Destination.LOGIN);
            }

        }, 2000); // 2 sec splash
    }

    public LiveData<Destination> getNavigationEvent() {
        return navigationEvent;
    }
}