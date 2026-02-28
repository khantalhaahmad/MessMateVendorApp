package com.vendorpro.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vendorpro.R;
import com.vendorpro.viewmodel.SplashViewModel;

public class SplashActivity extends AppCompatActivity {

    private SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashViewModel = new ViewModelProvider(this)
                .get(SplashViewModel.class);

        observeNavigation();
    }

    private void observeNavigation() {

        splashViewModel.getNavigationEvent()
                .observe(this, destination -> {

                    if (destination == SplashViewModel.Destination.DASHBOARD) {
                        startActivity(new Intent(this, DashboardActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    finish();
                });
    }
}