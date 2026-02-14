package com.vendorpro.network;

import com.vendorpro.model.LoginRequest;
import com.vendorpro.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/vendor/login")
    Call<LoginResponse> loginVendor(@Body LoginRequest loginRequest);

    @retrofit2.http.PUT("vendor/fcm-token")
    Call<Void> updateFcmToken(@Body com.vendorpro.model.FcmTokenRequest request);
}
