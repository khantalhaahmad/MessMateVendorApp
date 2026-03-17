package com.vendorpro.repository;

import com.vendorpro.model.WalletResponse;
import com.vendorpro.network.PayoutService;

import retrofit2.Call;

public class PayoutRepository {

    private final PayoutService payoutService;

    public PayoutRepository(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    public Call<WalletResponse> getWallet(String userId) {
        return payoutService.getWallet(userId);
    }
}