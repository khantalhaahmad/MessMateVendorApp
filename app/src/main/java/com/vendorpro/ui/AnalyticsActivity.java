package com.vendorpro.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.vendorpro.R;
import com.vendorpro.model.AnalyticsResponse;
import com.vendorpro.model.TopItem;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.AnalyticsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnalyticsActivity extends BaseActivity {

    private AnalyticsViewModel viewModel;

    private TextView tvTitle;
    private TextView tvRevenue;
    private TextView tvOrders;
    private TextView tvCustomers;
    private TextView tvRating;

    // NEW
    private TextView tvAverageOrderValue;
    private TextView tvTopItems;

    private BarChart weeklyChart;
    private LineChart revenueChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        initViews();

        viewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        viewModel.init(this);

        String ownerId = getIntent().getStringExtra("ownerId");

        if(ownerId == null || ownerId.isEmpty()){
            ownerId = TokenManager.getInstance(this).getUserId();
        }

        Log.d("ANALYTICS_DEBUG","Owner ID: "+ownerId);

        viewModel.loadAnalytics(ownerId);

        observeAnalytics();
    }

    private void initViews() {

        tvTitle = findViewById(R.id.tvTitle);
        tvRevenue = findViewById(R.id.tvRevenue);
        tvOrders = findViewById(R.id.tvOrders);
        tvCustomers = findViewById(R.id.tvCustomers);
        tvRating = findViewById(R.id.tvRating);

        tvAverageOrderValue = findViewById(R.id.tvAverageOrderValue);
        tvTopItems = findViewById(R.id.tvTopItems);

        weeklyChart = findViewById(R.id.weeklyChart);
        revenueChart = findViewById(R.id.revenueChart);

        tvTitle.setText("Analytics");
    }

    private void observeAnalytics() {

        viewModel.getAnalytics().observe(this, data -> {

            if (data == null) {
                Log.e("ANALYTICS_DEBUG","Analytics data NULL");
                return;
            }

            updateCards(data);

            if (data.weeklyOrders != null && !data.weeklyOrders.isEmpty()) {
                setWeeklyChart(data.weeklyOrders);
            }

            if (data.monthlyRevenue != null && !data.monthlyRevenue.isEmpty()) {
                setRevenueChart(data.monthlyRevenue);
            }

            if (data.topItems != null && !data.topItems.isEmpty()) {
                showTopItems(data.topItems);
            }

        });
    }

    private void updateCards(AnalyticsResponse data) {

        tvRevenue.setText(
                String.format(Locale.getDefault(),"₹%.2f",data.totalRevenue)
        );

        tvOrders.setText(String.valueOf(data.totalOrders));

        tvCustomers.setText(String.valueOf(data.activeCustomers));

        tvRating.setText(
                String.format(Locale.getDefault(),"%.1f ⭐",data.avgRating)
        );

        tvAverageOrderValue.setText(
                String.format(Locale.getDefault(),"₹%.2f",data.averageOrderValue)
        );
    }

    /* =========================================================
       TOP ITEMS
    ========================================================= */

    private void showTopItems(List<TopItem> items){

        StringBuilder builder = new StringBuilder();

        for(int i=0;i<items.size();i++){

            TopItem item = items.get(i);

            builder.append(i+1)
                    .append(". ")
                    .append(item.name)
                    .append(" (")
                    .append(item.count)
                    .append(")\n");
        }

        tvTopItems.setText(builder.toString());
    }

    /* =========================================================
       WEEKLY ORDERS CHART
    ========================================================= */

    private void setWeeklyChart(List<Integer> orders) {

        List<BarEntry> entries = new ArrayList<>();

        for(int i = 0; i < orders.size(); i++){
            entries.add(new BarEntry(i, orders.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries,"Orders");

        dataSet.setColor(Color.parseColor("#6C2BD9"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);

        weeklyChart.setData(data);

        weeklyChart.getDescription().setEnabled(false);
        weeklyChart.getAxisRight().setEnabled(false);

        Legend legend = weeklyChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = weeklyChart.getXAxis();

        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = weeklyChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        weeklyChart.animateY(1200);
        weeklyChart.invalidate();
    }

    /* =========================================================
       MONTHLY REVENUE CHART
    ========================================================= */

    private void setRevenueChart(List<Integer> revenue) {

        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < revenue.size(); i++){
            entries.add(new Entry(i, revenue.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries,"Revenue");

        dataSet.setColor(Color.parseColor("#FF6B00"));
        dataSet.setCircleColor(Color.parseColor("#FF6B00"));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);

        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#33FF6B00"));

        LineData data = new LineData(dataSet);

        revenueChart.setData(data);

        revenueChart.getDescription().setEnabled(false);
        revenueChart.getAxisRight().setEnabled(false);

        Legend legend = revenueChart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = revenueChart.getXAxis();

        String[] weeks = {"1-7","8-14","15-21","22-31"};

        xAxis.setValueFormatter(new IndexAxisValueFormatter(weeks));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = revenueChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        revenueChart.animateY(1200);
        revenueChart.invalidate();
    }
}