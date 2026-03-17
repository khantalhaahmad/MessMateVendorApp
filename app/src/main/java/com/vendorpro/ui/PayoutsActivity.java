package com.vendorpro.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vendorpro.R;
import com.vendorpro.model.WalletResponse;
import com.vendorpro.network.PayoutService;
import com.vendorpro.network.RetrofitClient;
import com.vendorpro.network.TokenManager;
import com.vendorpro.repository.PayoutRepository;
import com.vendorpro.viewmodel.PayoutViewModel;

public class PayoutsActivity extends BaseActivity {

    private TextView tvTitle, tvWallet, tvPending, tvPaid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payouts);

        /* ============================
           INIT VIEWS
        ============================ */

        tvTitle = findViewById(R.id.tvTitle);
        tvWallet = findViewById(R.id.tvWallet);
        tvPending = findViewById(R.id.tvPending);
        tvPaid = findViewById(R.id.tvPaid);

        tvTitle.setText("Payouts");

        /* ============================
           GET USER ID
        ============================ */

        String userId = TokenManager
                .getInstance(this)
                .getUserId();


        /* ============================
           SETUP MVVM
        ============================ */

        PayoutService service = RetrofitClient
                .getClient(this)
                .create(PayoutService.class);
        PayoutRepository repository = new PayoutRepository(service);

        PayoutViewModel viewModel = new ViewModelProvider(this).get(PayoutViewModel.class);

        /* ============================
           OBSERVE DATA
        ============================ */

        viewModel.getWalletData().observe(this, data -> {

            if (data != null && data.isSuccess()) {

                tvWallet.setText("Wallet: ₹ " + data.getWallet());
                tvPending.setText("Pending: ₹ " + data.getPending());
                tvPaid.setText("Paid: ₹ " + data.getPaid());

            } else {

                tvWallet.setText("Wallet: ₹ 0");
                tvPending.setText("Pending: ₹ 0");
                tvPaid.setText("Paid: ₹ 0");
            }

        });

        /* ============================
           FETCH DATA
        ============================ */

        viewModel.fetchWallet(repository, userId);
    }
}