package com.vendorpro.network;

import com.vendorpro.model.LoginResponse;
import com.vendorpro.model.FcmTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthService {

    // 🔥 OTP / Firebase login (MAIN)
    @POST("auth/firebase-login")
    Call<LoginResponse> firebaseLogin(
            @Header("Authorization") String bearerToken
    );

    // 🔔 Update FCM token (after login)
    @PUT("vendor/fcm-token")
    Call<Void> updateFcmToken(
            @Header("Authorization") String bearerToken,
            @Body FcmTokenRequest request
    );
}
