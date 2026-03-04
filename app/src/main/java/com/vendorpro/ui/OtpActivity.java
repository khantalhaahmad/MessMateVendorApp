package com.vendorpro.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

    private EditText etOtp, et1, et2, et3, et4, et5, et6;
    private Button btnVerifyOtp;
    private ProgressBar progressBar;
    private TextView txtTimer, txtResend;

    private FirebaseAuth mAuth;
    private String verificationId;
    private CountDownTimer countDownTimer;

    private static final String PREF_AUTH = "auth_prefs";
    private static final String KEY_VERIFICATION_ID = "verification_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        mAuth = FirebaseAuth.getInstance();

        etOtp = findViewById(R.id.etOtp);
        et1 = findViewById(R.id.etOtp1);
        et2 = findViewById(R.id.etOtp2);
        et3 = findViewById(R.id.etOtp3);
        et4 = findViewById(R.id.etOtp4);
        et5 = findViewById(R.id.etOtp5);
        et6 = findViewById(R.id.etOtp6);

        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        progressBar = findViewById(R.id.progressBar);
        txtTimer = findViewById(R.id.txtTimer);
        txtResend = findViewById(R.id.txtResend);

        verificationId = getVerificationId();
        btnVerifyOtp.setEnabled(false);

        setupOtpBoxes();
        startTimer();

        btnVerifyOtp.setOnClickListener(v -> verifyOtp());
    }

    // ================= PREMIUM TIMER =================
    private void startTimer() {

        txtResend.setAlpha(0.4f);
        txtResend.setEnabled(false);

        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTimer.setText("00:" + String.format("%02d", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                txtTimer.setText("00:00");
                txtResend.setAlpha(1f);
                txtResend.setEnabled(true);
            }
        }.start();
    }

    // ================= PREMIUM OTP HANDLING =================
    private void setupOtpBoxes() {

        EditText[] boxes = {et1, et2, et3, et4, et5, et6};

        for (int i = 0; i < boxes.length; i++) {

            int index = i;
            boxes[i].addTextChangedListener(new android.text.TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (s.length() == 1 && index < boxes.length - 1) {
                        boxes[index + 1].requestFocus();
                    }

                    if (s.length() == 0 && index > 0) {
                        boxes[index - 1].requestFocus();
                    }

                    updateOtpState(boxes);
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {}
            });
        }
    }

    private void updateOtpState(EditText[] boxes) {

        StringBuilder otp = new StringBuilder();
        for (EditText box : boxes) {
            otp.append(box.getText().toString());
        }

        etOtp.setText(otp.toString());

        boolean valid = otp.length() == 6;

        btnVerifyOtp.setEnabled(valid);
        btnVerifyOtp.setBackgroundResource(
                valid ? R.drawable.bg_button_zomato_enabled :
                        R.drawable.bg_button_zomato_disabled
        );

        if (valid) {
            btnVerifyOtp.animate().scaleX(1.05f).scaleY(1.05f).setDuration(120).withEndAction(() ->
                    btnVerifyOtp.animate().scaleX(1f).scaleY(1f).setDuration(120));
        }
    }

    // ================= VERIFY OTP =================
    private void verifyOtp() {

        String code = etOtp.getText().toString().trim();

        if (code.length() != 6) {
            premiumError();
            return;
        }

        showLoading(true);

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(verificationId, code);

        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> backendLogin())
                .addOnFailureListener(e -> {
                    showLoading(false);
                    premiumError();
                });
    }

    // ================= PREMIUM ERROR EFFECT =================
    private void premiumError() {

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (v != null) v.vibrate(100);

        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_fall);
        findViewById(R.id.otpContainer).startAnimation(shake);

        clearBoxes();
    }

    private void clearBoxes() {
        et1.setText(""); et2.setText(""); et3.setText("");
        et4.setText(""); et5.setText(""); et6.setText("");
        etOtp.setText("");
        et1.requestFocus();
        btnVerifyOtp.setEnabled(false);
    }

    // ================= BACKEND LOGIN =================
    private void backendLogin() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            showLoading(false);
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
            return;
        }

        user.getIdToken(true).addOnSuccessListener(result -> {

            String firebaseToken = result.getToken();

            AuthService service =
                    RetrofitClient.getClient(this).create(AuthService.class);

            service.firebaseLogin("Bearer " + firebaseToken)
                    .enqueue(new Callback<LoginResponse>() {

                        @Override
                        public void onResponse(Call<LoginResponse> call,
                                               Response<LoginResponse> response) {

                            showLoading(false);

                            if (response.isSuccessful() && response.body() != null) {

                                TokenManager tokenManager =
                                        TokenManager.getInstance(OtpActivity.this);

                                // ✅ Save JWT token
                                tokenManager.saveToken(response.body().getToken());

                                // ✅ Save Refresh Token (optional but recommended)
                                if (response.body().getRefreshToken() != null) {
                                    tokenManager.saveRefreshToken(response.body().getRefreshToken());
                                }

                                // 🔥 IMPORTANT FIX → Save MongoDB userId from backend
                                if (response.body().getUser() != null) {
                                    String userId = response.body().getUser().getId();
                                    tokenManager.saveUserId(userId);
                                }

                                startActivity(new Intent(
                                        OtpActivity.this,
                                        DashboardActivity.class
                                ));
                                finish();

                            } else {
                                Toast.makeText(OtpActivity.this,
                                        "Login failed. Try again.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            showLoading(false);
                            premiumError();
                        }
                    });

        }).addOnFailureListener(e -> {
            showLoading(false);
            Toast.makeText(this, "Token error", Toast.LENGTH_SHORT).show();
        });
    }

    private void showLoading(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
        btnVerifyOtp.setEnabled(!state);
    }

    private String getVerificationId() {
        SharedPreferences prefs =
                getSharedPreferences(PREF_AUTH, MODE_PRIVATE);
        return prefs.getString(KEY_VERIFICATION_ID, null);
    }
}