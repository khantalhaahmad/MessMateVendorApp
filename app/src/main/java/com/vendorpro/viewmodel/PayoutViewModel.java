package com.vendorpro.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.vendorpro.model.WalletResponse;
import com.vendorpro.repository.PayoutRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayoutViewModel extends ViewModel {

    private final MutableLiveData<WalletResponse> walletData = new MutableLiveData<>();

    public LiveData<WalletResponse> getWalletData() {
        return walletData;
    }

    public void fetchWallet(PayoutRepository repository, String userId) {

        repository.getWallet(userId).enqueue(new Callback<WalletResponse>() {
            @Override
            public void onResponse(Call<WalletResponse> call, Response<WalletResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    walletData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<WalletResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}