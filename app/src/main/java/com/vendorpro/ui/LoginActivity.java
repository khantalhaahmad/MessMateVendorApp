package com.vendorpro.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vendorpro.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private Button btnSendOtp;
    private ProgressBar progressBar;
    private ImageView imgHero;

    private ViewPager2 viewPager;
    private TabLayout tabIndicator;

    private FirebaseAuth mAuth;

    private static final String PREF_AUTH = "auth_prefs";
    private static final String KEY_VERIFICATION_ID = "verification_id";

    private Handler sliderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        makeStatusBarTransparent();

        mAuth = FirebaseAuth.getInstance();

        imgHero = findViewById(R.id.imgHero);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        progressBar = findViewById(R.id.progressBar);

        viewPager = findViewById(R.id.viewPager);
        tabIndicator = findViewById(R.id.tabIndicator);

        btnSendOtp.setEnabled(false);

        loadHeroImage();
        setupPhoneWatcher();
        setupSlider();

        btnSendOtp.setOnClickListener(v -> sendOtp());
    }

    // ================= STATUS BAR =================
    private void makeStatusBarTransparent() {
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    // ================= HERO IMAGE =================
    private void loadHeroImage() {
        Glide.with(this)
                .load("https://images.unsplash.com/photo-1555396273-367ea4eb4db5")
                .centerCrop()
                .into(imgHero);
    }

    // ================= SLIDER SETUP =================
    private void setupSlider() {

        List<String> sliderTexts = Arrays.asList(
                "Increase your online orders",
                "Reach customers far away from you",
                "Access powerful vendor tools",
                "Grow your business with VendorPro"
        );

        SliderAdapter adapter = new SliderAdapter(sliderTexts);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabIndicator, viewPager,
                (tab, position) -> {}).attach();

        sliderHandler = new Handler(Looper.getMainLooper());

        Runnable sliderRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % sliderTexts.size();
                viewPager.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 3000);
            }
        };

        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    // ================= PHONE WATCHER =================
    private void setupPhoneWatcher() {
        etPhoneNumber.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean isValid = s.length() == 10;

                btnSendOtp.setEnabled(isValid);
                btnSendOtp.setBackgroundResource(
                        isValid ? R.drawable.bg_button_zomato_enabled :
                                R.drawable.bg_button_zomato_disabled
                );
            }

            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // ================= SEND OTP =================
    private void sendOtp() {

        String input = etPhoneNumber.getText().toString().trim();

        if (input.length() != 10) {
            etPhoneNumber.setError("Enter valid 10-digit number");
            return;
        }

        showLoading(true);

        String phone = "+91" + input;

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void showLoading(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
        btnSendOtp.setEnabled(!state);
    }

    // ================= FIREBASE CALLBACKS =================
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(
                        @NonNull com.google.firebase.auth.PhoneAuthCredential credential) {}

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    showLoading(false);
                    Toast.makeText(LoginActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String verificationId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    saveVerificationId(verificationId);
                    showLoading(false);

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