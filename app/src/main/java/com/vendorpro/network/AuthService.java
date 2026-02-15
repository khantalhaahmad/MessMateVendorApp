package com.vendorpro.network;

import com.vendorpro.model.LoginResponse;
import com.vendorpro.model.FcmTokenRequest;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Body;

public interface AuthService {

    // 🔥 OTP / Firebase login (MAIN)
    @POST("auth/firebase-login")
    Call<LoginResponse> firebaseLogin();

    // 🔔 Update FCM token (after login)
    @PUT("vendor/fcm-token")
    Call<Void> updateFcmToken(@Body FcmTokenRequest request);
}
