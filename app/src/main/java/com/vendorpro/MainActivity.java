package com.vendorpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.vendorpro.model.FcmTokenRequest;
import com.vendorpro.network.AuthService;
import com.vendorpro.network.RetrofitClient;
import com.vendorpro.network.TokenManager;
import com.vendorpro.ui.DashboardActivity;
import com.vendorpro.ui.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String jwtToken = TokenManager.getInstance(this).getToken();

        if (jwtToken != null) {

            // ✅ User already logged in → update FCM token
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.e("FCM", "Failed to get FCM token");
                            return;
                        }
                        String fcmToken = task.getResult();
                        sendFcmToken(fcmToken, jwtToken);
                    });

            // ✅ Go to Dashboard
            startActivity(new Intent(this, DashboardActivity.class));
            finish();

        } else {
            // ❌ Not logged in → Login screen
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    // ================= SEND FCM TOKEN =================
    private void sendFcmToken(String fcmToken, String jwtToken) {

        AuthService authService =
                RetrofitClient.getClient(this)
                        .create(AuthService.class);

        authService.updateFcmToken(
                "Bearer " + jwtToken,
                new FcmTokenRequest(fcmToken)
        ).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("FCM", "FCM token updated successfully");
                } else {
                    Log.e("FCM", "FCM update failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FCM", "FCM update error", t);
            }
        });
    }
}
