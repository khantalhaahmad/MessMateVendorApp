package com.vendorpro.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vendorpro.R;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private Button btnSendOtp;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private static final String PREF_AUTH = "auth_prefs";
    private static final String KEY_VERIFICATION_ID = "verification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        progressBar = findViewById(R.id.progressBar);

        btnSendOtp.setOnClickListener(v -> sendOtp());
    }

    // ================= SEND OTP =================
    private void sendOtp() {
        String input = etPhoneNumber.getText().toString().trim();

        if (input.length() != 10) {
            etPhoneNumber.setError("Enter valid 10-digit number");
            return;
        }

        String phone = "+91" + input;
        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // ================= FIREBASE CALLBACKS =================
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(
                        @NonNull com.google.firebase.auth.PhoneAuthCredential credential) {
                    // auto verification (rare)
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    saveVerificationId(verificationId);
                    progressBar.setVisibility(View.GONE);

                    // 👉 GO TO OTP PAGE
                    startActivity(new Intent(
                            LoginActivity.this,
                            OtpActivity.class
                    ));
                }
            };

    private void saveVerificationId(String id) {
        SharedPreferences prefs =
                getSharedPreferences(PREF_AUTH, MODE_PRIVATE);
        prefs.edit().putString(KEY_VERIFICATION_ID, id).apply();
    }
}