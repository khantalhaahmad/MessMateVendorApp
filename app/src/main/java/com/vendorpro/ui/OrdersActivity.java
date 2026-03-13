package com.vendorpro.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vendorpro.R;
import com.vendorpro.network.SocketManager;

import io.socket.client.Socket;

public class OrdersActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private OrdersPagerAdapter adapter;

    private Socket socket;

    public static final int ORDER_DETAIL_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initializeViews();
        setupViewPager();
        setupTabs();
        setupSocket();
    }

    /* =========================================
       INITIALIZE VIEWS
    ========================================== */

    private void initializeViews() {

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

    }

    /* =========================================
       SETUP VIEWPAGER
    ========================================== */

    private void setupViewPager() {

        adapter = new OrdersPagerAdapter(this);

        viewPager.setAdapter(adapter);

        // preload all tabs
        viewPager.setOffscreenPageLimit(4);

    }

    /* =========================================
       SETUP TABS
    ========================================== */

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

                        case 3:
                            tab.setText("Completed");
                            tab.setIcon(R.drawable.ic_delivered);
                            break;

                    }

                }
        ).attach();

    }

    /* =========================================
       SOCKET SETUP
    ========================================== */

    private void setupSocket() {

        socket = SocketManager.getSocket();

        if(socket == null) return;

        SocketManager.connect();

    }

    /* =========================================
       REFRESH AFTER ORDER UPDATE
    ========================================== */

    @Override
    protected void onResume() {

        super.onResume();

        if (adapter != null) {

            adapter.notifyDataSetChanged();

        }

    }

    /* =========================================
       HANDLE RESULT FROM DETAIL
    ========================================== */

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data
    ) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ORDER_DETAIL_REQUEST
                && resultCode == RESULT_OK) {

            refreshTabs();

        }

    }

    /* =========================================
       REFRESH TABS
    ========================================== */

    private void refreshTabs() {

        if(adapter != null){

            adapter.notifyDataSetChanged();

        }

    }

    /* =========================================
       CLEANUP
    ========================================== */

    @Override
    protected void onDestroy() {

        super.onDestroy();

        // keep socket alive for realtime updates
        // do NOT disconnect here unless app logout

    }

}