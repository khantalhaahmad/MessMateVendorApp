package com.vendorpro.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.vendorpro.R;

public class AuthLandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_landing);

        // 🔥 Zomato-style background image load
        ImageView bgImage = findViewById(R.id.bgImage);

        Glide.with(this)
                .load("https://images.unsplash.com/photo-1589302168068-964664d93dc0")
                .centerCrop()
                .into(bgImage);

        initClicks();
    }

    private void initClicks() {

        // 👉 Login button → LoginActivity
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            startActivity(new Intent(
                    AuthLandingActivity.this,
                    LoginActivity.class
            ));
        });

        // 👉 Partner with VendorPro → Open website
        findViewById(R.id.btnPartner).setOnClickListener(v -> {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://10.0.2.2:5173/partner-with-us")
            );
            startActivity(intent);
        });
    }
}