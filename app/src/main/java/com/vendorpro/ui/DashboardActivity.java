package com.vendorpro.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.vendorpro.R;
import com.vendorpro.model.DashboardStats;
import com.vendorpro.model.QuickAction;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.DashboardViewModel;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends BaseActivity {

    private DashboardViewModel dashboardViewModel;

    private TextView tvTotalRevenue, tvTotalOrders, tvActiveCustomers, tvAverageRating;
    private TextView tvRestaurantStatus;
    private View btnToggleStatus;

    private ProgressBar progressBar;
    private BarChart barChart;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView rvQuickActions;

    private boolean isRestaurantOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        setupQuickActions();

        loadDashboardData();

        swipeRefreshLayout.setOnRefreshListener(this::loadDashboardData);
    }

    private void initializeViews() {

        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvActiveCustomers = findViewById(R.id.tvActiveCustomers);
        tvAverageRating = findViewById(R.id.tvAverageRating);

        tvRestaurantStatus = findViewById(R.id.tvRestaurantStatus);
        btnToggleStatus = findViewById(R.id.btnToggleStatus);

        progressBar = findViewById(R.id.progressBar);

        barChart = findViewById(R.id.barChart);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        rvQuickActions = findViewById(R.id.rvQuickActions);

        btnToggleStatus.setOnClickListener(v -> toggleRestaurantStatus());

        setupChartStyle();
    }

    private void setupQuickActions() {

        rvQuickActions.setLayoutManager(new GridLayoutManager(this, 4));

        List<QuickAction> actions = new ArrayList<>();

        actions.add(new QuickAction("Orders", R.drawable.ic_orders));
        actions.add(new QuickAction("Menu", R.drawable.ic_menu));
        actions.add(new QuickAction("Analytics", R.drawable.ic_analytics));
        actions.add(new QuickAction("Payouts", R.drawable.ic_payout));

        DashboardQuickActionAdapter adapter =
                new DashboardQuickActionAdapter(actions, action -> {

                    switch (action.getTitle()) {

                        case "Orders":
                            startActivity(new Intent(this, OrdersActivity.class));
                            break;

                        case "Menu":
                            startActivity(new Intent(this, MenuActivity.class));
                            break;

                        case "Analytics":
                            startActivity(new Intent(this, AnalyticsActivity.class));
                            break;

                        case "Payouts":
                            startActivity(new Intent(this, PayoutsActivity.class));
                            break;
                    }
                });

        rvQuickActions.setAdapter(adapter);
    }

    private void setupChartStyle() {

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.getLegend().setEnabled(false);
    }

    private void loadDashboardData() {

        String vendorId = TokenManager.getInstance(this).getUserId();

        if (vendorId == null) {
            showError("User ID not found. Please login again.");
            return;
        }

        dashboardViewModel.getDashboardStats(vendorId).observe(this, resource -> {

            if (resource == null) return;

            switch (resource.status) {

                case LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    break;

                case SUCCESS:

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (resource.data != null) {

                        Log.d("DASHBOARD_DEBUG", "Stats received");

                        String messId = resource.data.getMessId();

                        Log.d("DASHBOARD_DEBUG", "Mess ID from API: " + messId);

                        if (messId != null) {

                            TokenManager
                                    .getInstance(this)
                                    .saveMessId(messId);

                            Log.d("DASHBOARD_DEBUG", "Mess ID saved in TokenManager");

                        } else {

                            Log.e("DASHBOARD_DEBUG", "Mess ID NULL from API");
                        }

                        updateUI(resource.data);
                    }

                    break;

                case ERROR:
                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    showError(resource.message);
                    break;
            }
        });

        dashboardViewModel.refreshStats(vendorId);
    }

    private void updateUI(DashboardStats stats) {

        tvTotalRevenue.setText(
                String.format(Locale.getDefault(), "₹%.2f", stats.getTotalRevenue())
        );

        tvTotalOrders.setText(String.valueOf(stats.getTotalOrders()));
        tvActiveCustomers.setText(String.valueOf(stats.getActiveCustomers()));

        // 🔥 FIXED METHOD
        tvAverageRating.setText(
                String.format(Locale.getDefault(), "%.1f", stats.getAvgRating())
        );

        setupChart(stats.getWeeklyOrders());
    }

    private void setupChart(List<Integer> weeklyOrders) {

        if (weeklyOrders == null || weeklyOrders.isEmpty()) return;

        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < weeklyOrders.size(); i++) {
            entries.add(new BarEntry(i, weeklyOrders.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Orders");

        dataSet.setColor(Color.parseColor("#6C2BD9"));
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(days));

        barChart.invalidate();
    }

    private void toggleRestaurantStatus() {

        isRestaurantOpen = !isRestaurantOpen;

        if (isRestaurantOpen) {

            tvRestaurantStatus.setText("Restaurant Open");
            tvRestaurantStatus.setTextColor(Color.parseColor("#4CAF50"));

        } else {

            tvRestaurantStatus.setText("Restaurant Closed");
            tvRestaurantStatus.setTextColor(Color.RED);
        }
    }
}