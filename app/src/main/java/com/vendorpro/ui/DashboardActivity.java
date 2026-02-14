package com.vendorpro.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.vendorpro.R;
import com.vendorpro.model.DashboardStats;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends BaseActivity {

    private DashboardViewModel dashboardViewModel;
    private TextView tvTotalRevenue, tvTotalOrders, tvActiveCustomers, tvAverageRating;
    private BarChart barChart;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setupChart(null); // Initial setup

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        loadDashboardData();

        swipeRefreshLayout.setOnRefreshListener(this::loadDashboardData);
    }

    private void initializeViews() {
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalOrders = findViewById(R.id.tvTotalOrders);
        tvActiveCustomers = findViewById(R.id.tvActiveCustomers);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        barChart = findViewById(R.id.barChart);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        findViewById(R.id.btnViewOrders).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, OrdersActivity.class));
        });

        findViewById(R.id.btnManageMenu).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, MenuActivity.class));
        });

        // Customize Chart
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
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
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        swipeRefreshLayout.setRefreshing(true);
                        break;
                    case SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        if (resource.data != null) {
                            updateUI(resource.data);
                        }
                        break;
                    case ERROR:
                        swipeRefreshLayout.setRefreshing(false);
                        showError(resource.message);
                        break;
                }
            }
        });
        
        // Trigger refresh if needed
        dashboardViewModel.refreshStats(vendorId);
    }

    private void updateUI(DashboardStats stats) {
        tvTotalRevenue.setText(String.format(Locale.getDefault(), "$%.2f", stats.getTotalRevenue()));
        tvTotalOrders.setText(String.valueOf(stats.getTotalOrders()));
        tvActiveCustomers.setText(String.valueOf(stats.getActiveCustomers()));
        tvAverageRating.setText(String.format(Locale.getDefault(), "%.1f", stats.getAverageRating()));

        setupChart(stats.getWeeklyOrders());
    }

    private void setupChart(List<Integer> weeklyOrders) {
        if (weeklyOrders == null || weeklyOrders.isEmpty()) return;

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < weeklyOrders.size(); i++) {
            entries.add(new BarEntry(i, weeklyOrders.get(i)));
            labels.add("Day " + (i + 1));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Orders");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.invalidate(); // refresh
    }
}
