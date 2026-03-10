package com.vendorpro.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vendorpro.R;

public class OrdersActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrdersPagerAdapter adapter;

    public static final int ORDER_DETAIL_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initializeViews();
        setupViewPager();
        setupTabs();
    }

    private void initializeViews() {

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

    }

    /* =============================
       SETUP VIEWPAGER
    ============================== */

    private void setupViewPager() {

        adapter = new OrdersPagerAdapter(this);

        viewPager.setAdapter(adapter);

        // preload all tabs
        viewPager.setOffscreenPageLimit(3);

    }

    /* =============================
       SETUP TABS
    ============================== */

    private void setupTabs() {

        new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {

                    switch (position) {

                        case 0:
                            tab.setText("New");
                            tab.setIcon(R.drawable.ic_pending);
                            break;

                        case 1:
                            tab.setText("Preparing");
                            tab.setIcon(R.drawable.ic_accepted);
                            break;

                        case 2:
                            tab.setText("Ready");
                            tab.setIcon(R.drawable.ic_ready);
                            break;

                    }

                }
        ).attach();

    }

    /* =============================
       REFRESH AFTER ORDER UPDATE
    ============================== */

    @Override
    protected void onResume() {

        super.onResume();

        if (adapter != null) {

            adapter.notifyDataSetChanged();

        }

    }

    /* =============================
       HANDLE RESULT FROM DETAIL
    ============================== */

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data
    ) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ORDER_DETAIL_REQUEST
                && resultCode == RESULT_OK) {

            // refresh fragments
            recreate();

        }

    }

}