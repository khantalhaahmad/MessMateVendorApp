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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vendorpro.R;
import com.vendorpro.model.LoginResponse;
import com.vendorpro.network.AuthService;
import com.vendorpro.network.RetrofitClient;
import com.vendorpro.network.TokenManager;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber, etOtp;
    private Button btnSendOtp, btnVerifyOtp;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private String verificationId;
    private String normalizedPhone;

    private static final String PREF_AUTH = "auth_prefs";
    private static final String KEY_VERIFICATION_ID = "verification_id";
    private static final String KEY_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etOtp = findViewById(R.id.etOtp);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        progressBar = findViewById(R.id.progressBar);

        btnSendOtp.setOnClickListener(v -> sendOtp());
        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
    }

    // ================= SEND OTP =================
    private void sendOtp() {
        String input = etPhoneNumber.getText().toString().trim();

        if (input.isEmpty()) {
            etPhoneNumber.setError("Phone number required");
            return;
        }

        if (!input.startsWith("+91")) {
            normalizedPhone = "+91" + input;
        } else {
            normalizedPhone = input;
        }

        saveToPrefs(KEY_PHONE, normalizedPhone);
        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(normalizedPhone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // ================= FIREBASE CALLBACKS =================
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,
                            "Verification failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String s,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    verificationId = s;
                    saveToPrefs(KEY_VERIFICATION_ID, s);

                    progressBar.setVisibility(View.GONE);

                    etPhoneNumber.setVisibility(View.GONE);
                    btnSendOtp.setVisibility(View.GONE);
                    etOtp.setVisibility(View.VISIBLE);
                    btnVerifyOtp.setVisibility(View.VISIBLE);

                    Toast.makeText(LoginActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
            };

    // ================= VERIFY OTP =================
    private void verifyOtp() {
        String code = etOtp.getText().toString().trim();

        if (code.isEmpty()) {
            etOtp.setError("OTP required");
            return;
        }

        verificationId = getFromPrefs(KEY_VERIFICATION_ID);

        if (verificationId == null) {
            Toast.makeText(this, "Session expired. Please resend OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, code);

        signInWithPhoneAuthCredential(credential);
    }

    // ================= FIREBASE SIGN IN =================
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        backendLogin(); // 🔥 IMPORTANT
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this,
                                "OTP verification failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ================= BACKEND LOGIN (FINAL) =================
    private void backendLogin() {

        AuthService authService =
                RetrofitClient.getClient(this).create(AuthService.class);

        authService.firebaseLogin().enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {

                progressBar.setVisibility(View.GONE);
                Log.d("BACKEND_LOGIN", "Code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse res = response.body();

                    TokenManager.getInstance(LoginActivity.this)
                            .saveToken(res.getToken());

                    startActivity(new Intent(LoginActivity.this,
                            DashboardActivity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Backend login failed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ================= SHARED PREF HELPERS =================
    private void saveToPrefs(String key, String value) {
        SharedPreferences prefs =
                getSharedPreferences(PREF_AUTH, MODE_PRIVATE);
        prefs.edit().putString(key, value).apply();
    }

    private String getFromPrefs(String key) {
        SharedPreferences prefs =
                getSharedPreferences(PREF_AUTH, MODE_PRIVATE);
        return prefs.getString(key, null);
    }
}
