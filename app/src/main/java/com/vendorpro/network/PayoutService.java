package com.vendorpro.network;

import com.vendorpro.model.WalletResponse;
import com.vendorpro.model.WithdrawRequest;
import com.vendorpro.model.GenericResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PayoutService {

    /* ============================
       💰 GET WALLET
    ============================ */

    @GET("/api/payouts/wallet")
    Call<WalletResponse> getWallet(
            @Query("userId") String userId
    );

    /* ============================
       📤 WITHDRAW REQUEST
    ============================ */

    @POST("/api/payouts/withdraw")
    Call<GenericResponse> withdraw(
            @Body WithdrawRequest request
    );

}