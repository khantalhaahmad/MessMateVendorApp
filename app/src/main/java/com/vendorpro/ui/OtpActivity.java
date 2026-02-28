package com.vendorpro.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vendorpro.R;
import com.vendorpro.model.LoginResponse;
import com.vendorpro.network.AuthService;
import com.vendorpro.network.RetrofitClient;
import com.vendorpro.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerifyOtp;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private String verificationId;

    private static final String PREF_AUTH = "auth_prefs";
    private static final String KEY_VERIFICATION_ID = "verification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();

        etOtp = findViewById(R.id.etOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        progressBar = findViewById(R.id.progressBar);

        verificationId = getVerificationId();

        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
    }

    // ================= VERIFY OTP =================
    private void verifyOtp() {
        String code = etOtp.getText().toString().trim();

        if (code.length() != 6) {
            etOtp.setError("Enter valid OTP");
            return;
        }

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, code);

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> backendLogin())
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this,
                            "Invalid OTP", Toast.LENGTH_SHORT).show();
                });
    }

    // ================= BACKEND LOGIN =================
    private void backendLogin() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        user.getIdToken(true).addOnSuccessListener(result -> {

            String firebaseToken = result.getToken();
            Log.d("FIREBASE_TOKEN", firebaseToken);

            AuthService service =
                    RetrofitClient.getClient(this).create(AuthService.class);

            service.firebaseLogin("Bearer " + firebaseToken)
                    .enqueue(new Callback<LoginResponse>() {

                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               Response<LoginResponse> response) {

                            progressBar.setVisibility(View.GONE);

                            if (response.isSuccessful() && response.body() != null) {

                                TokenManager.getInstance(OtpActivity.this)
                                        .saveToken(response.body().getToken());

                                startActivity(new Intent(
                                        OtpActivity.this,
                                        DashboardActivity.class
                                ));
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(OtpActivity.this,
                                    t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private String getVerificationId() {
        SharedPreferences prefs =
                getSharedPreferences(PREF_AUTH, MODE_PRIVATE);
        return prefs.getString(KEY_VERIFICATION_ID, null);
    }
}