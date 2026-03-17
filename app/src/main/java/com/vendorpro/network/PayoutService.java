package com.vendorpro.network;

import com.vendorpro.model.WalletResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PayoutService {

    @GET("/api/payouts/wallet")
    Call<WalletResponse> getWallet(
            @Query("userId") String userId
    );

}