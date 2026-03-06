package com.vendorpro.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vendorpro.R;

public class AnalyticsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Analytics");
    }
}