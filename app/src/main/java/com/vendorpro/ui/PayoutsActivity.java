package com.vendorpro.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.vendorpro.R;
import com.vendorpro.model.GenericResponse;
import com.vendorpro.model.WithdrawRequest;
import com.vendorpro.network.PayoutService;
import com.vendorpro.network.RetrofitClient;
import com.vendorpro.network.TokenManager;
import com.vendorpro.repository.PayoutRepository;
import com.vendorpro.viewmodel.PayoutViewModel;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayoutsActivity extends BaseActivity {

    private TextView tvTitle, tvWallet, tvPending, tvPaid, tvProcessing;
    private Button btnWithdraw;

    private double currentWallet = 0;

    private PayoutService service;
    private String userId;

    private PayoutRepository repository;
    private PayoutViewModel viewModel;

    private final DecimalFormat df = new DecimalFormat("₹ 0.00");

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
        tvProcessing = findViewById(R.id.tvProcessing); // 🔥 NEW
        btnWithdraw = findViewById(R.id.btnWithdraw);

        tvTitle.setText("Payouts");

        /* ============================
           GET USER ID
        ============================ */

        userId = TokenManager
                .getInstance(this)
                .getUserId();

        /* ============================
           SETUP NETWORK + MVVM
        ============================ */

        service = RetrofitClient
                .getClient(this)
                .create(PayoutService.class);

        repository = new PayoutRepository(service);
        viewModel = new ViewModelProvider(this).get(PayoutViewModel.class);

        /* ============================
           OBSERVE DATA
        ============================ */

        viewModel.getWalletData().observe(this, data -> {

            if (data != null && data.isSuccess()) {

                currentWallet = data.getWallet();

                tvWallet.setText(df.format(data.getWallet()));
                tvPending.setText(df.format(data.getPending()));
                tvPaid.setText(df.format(data.getPaid()));

                // 🔥 NEW FIELD
                if (tvProcessing != null) {
                    tvProcessing.setText(df.format(data.getProcessing()));
                }

                handleWithdrawButton();

            } else {

                tvWallet.setText("₹ 0.00");
                tvPending.setText("₹ 0.00");
                tvPaid.setText("₹ 0.00");

                if (tvProcessing != null) {
                    tvProcessing.setText("₹ 0.00");
                }

                handleWithdrawButton();
            }
        });

        fetchWallet();

        btnWithdraw.setOnClickListener(v -> showWithdrawDialog());
    }

    /* ============================
       🔄 REFRESH WALLET
    ============================ */

    private void fetchWallet() {
        viewModel.fetchWallet(repository, userId);
    }

    /* ============================
       🔥 ENABLE / DISABLE BUTTON
    ============================ */

    private void handleWithdrawButton() {

        if (currentWallet <= 0) {
            btnWithdraw.setEnabled(false);
            btnWithdraw.setAlpha(0.5f);
        } else {
            btnWithdraw.setEnabled(true);
            btnWithdraw.setAlpha(1f);
        }
    }

    /* ============================
       💬 WITHDRAW DIALOG
    ============================ */

    private void showWithdrawDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_withdraw, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etAmount = view.findViewById(R.id.etAmount);
        TextView btnCancel = view.findViewById(R.id.btnCancel);
        TextView btnAll = view.findViewById(R.id.btnAll);
        TextView btnWithdrawDialog = view.findViewById(R.id.btnWithdraw);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        /* WITHDRAW ALL */
        btnAll.setOnClickListener(v -> {

            if (currentWallet <= 0) {
                Toast.makeText(this, "No balance", Toast.LENGTH_SHORT).show();
                return;
            }

            callWithdrawAPI(currentWallet);
            dialog.dismiss();
        });

        /* WITHDRAW CUSTOM */
        btnWithdrawDialog.setOnClickListener(v -> {

            String amountStr = etAmount.getText().toString().trim();

            if (TextUtils.isEmpty(amountStr)) {
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;

            try {
                amount = Double.parseDouble(amountStr);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount > currentWallet) {
                Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                return;
            }

            callWithdrawAPI(amount);
            dialog.dismiss();
        });

        dialog.show();
    }

    /* ============================
       🚀 CALL WITHDRAW API
    ============================ */

    private void callWithdrawAPI(double amount) {

        btnWithdraw.setEnabled(false);
        btnWithdraw.setText("Processing...");

        WithdrawRequest request = new WithdrawRequest(userId, amount);

        service.withdraw(request).enqueue(new Callback<GenericResponse>() {

            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {

                btnWithdraw.setEnabled(true);
                btnWithdraw.setText("Withdraw Money");

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {

                    Toast.makeText(PayoutsActivity.this, "Withdraw Requested ✅", Toast.LENGTH_SHORT).show();
                    fetchWallet();

                } else {
                    Toast.makeText(PayoutsActivity.this, "Withdraw failed ❌", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {

                btnWithdraw.setEnabled(true);
                btnWithdraw.setText("Withdraw Money");

                Toast.makeText(PayoutsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}