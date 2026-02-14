package com.vendorpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vendorpro.network.TokenManager;
import com.vendorpro.ui.DashboardActivity;
import com.vendorpro.ui.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (TokenManager.getInstance(this).getToken() != null) {
            // Update FCM Token if logged in
            com.google.firebase.messaging.FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = task.getResult();
                    sendFcmToken(token);
                });

            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void sendFcmToken(String token) {
        com.vendorpro.network.AuthService authService = com.vendorpro.network.RetrofitClient.getClient(this).create(com.vendorpro.network.AuthService.class);
        authService.updateFcmToken(new com.vendorpro.model.FcmTokenRequest(token)).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                // Token updated
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                // Failed to update token
            }
        });
    }
}
