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
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.Gson;

import com.vendorpro.R;
import com.vendorpro.model.DashboardStats;
import com.vendorpro.model.Order;
import com.vendorpro.model.QuickAction;
import com.vendorpro.model.ToggleResponse;
import com.vendorpro.network.Resource;
import com.vendorpro.network.SocketManager;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.DashboardViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends BaseActivity {

    private DashboardViewModel dashboardViewModel;

    private TextView tvRestaurantName;
    private TextView tvTotalRevenue, tvTotalOrders, tvActiveCustomers, tvAverageRating;
    private TextView tvRestaurantStatus;
    private TextView btnToggleStatus;

    private ProgressBar progressBar;
    private BarChart barChart;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView rvQuickActions;

    private boolean isRestaurantOpen = true;

    /* ============================================================
       ACTIVITY CREATE
    ============================================================ */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();

        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        setupQuickActions();

        loadDashboardData();

        setupSocket();   // 🔥 REALTIME ORDER LISTENER

        swipeRefreshLayout.setOnRefreshListener(this::loadDashboardData);
    }

    /* ============================================================
       SOCKET SETUP
    ============================================================ */

    private void setupSocket(){

        String ownerId = TokenManager.getInstance(this).getUserId();

        if(ownerId == null) return;

        SocketManager.connect();

        SocketManager.joinOwner(ownerId);

        SocketManager.on("new_order", args -> {

            runOnUiThread(() -> {

                try{

                    JSONObject data = (JSONObject) args[0];

                    Gson gson = new Gson();

                    Order order = gson.fromJson(
                            data.toString(),
                            Order.class
                    );

                    showNewOrderPopup(order);

                }catch(Exception e){

                    e.printStackTrace();

                }

            });

        });

    }

    /* ============================================================
       NEW ORDER POPUP
    ============================================================ */

    private void showNewOrderPopup(Order order){

        NewOrderDialog dialog =
                new NewOrderDialog(
                        this,
                        order,
                        new NewOrderDialog.Listener() {

                            @Override
                            public void onAccept(Order order) {

                                android.widget.Toast.makeText(
                                        DashboardActivity.this,
                                        "Order Accepted",
                                        android.widget.Toast.LENGTH_SHORT
                                ).show();

                            }

                            @Override
                            public void onReject(Order order) {

                                android.widget.Toast.makeText(
                                        DashboardActivity.this,
                                        "Order Rejected",
                                        android.widget.Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                );

        dialog.show();
    }

    /* ============================================================
       INITIALIZE VIEWS
    ============================================================ */

    private void initializeViews() {

        tvRestaurantName = findViewById(R.id.tvRestaurantName);

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

    /* ============================================================
       QUICK ACTIONS
    ============================================================ */

    private void setupQuickActions() {

        rvQuickActions.setLayoutManager(new GridLayoutManager(this,4));

        List<QuickAction> actions = new ArrayList<>();

        actions.add(new QuickAction("Orders",R.drawable.ic_orders));
        actions.add(new QuickAction("Menu",R.drawable.ic_menu));
        actions.add(new QuickAction("Analytics",R.drawable.ic_analytics));
        actions.add(new QuickAction("Payouts",R.drawable.ic_payout));

        DashboardQuickActionAdapter adapter =
                new DashboardQuickActionAdapter(actions,action -> {

                    switch (action.getTitle()){

                        case "Orders":
                            startActivity(new Intent(this,OrdersActivity.class));
                            break;

                        case "Menu":
                            startActivity(new Intent(this,MenuActivity.class));
                            break;

                        case "Analytics":

                            String vendorId = TokenManager.getInstance(this).getUserId();

                            Intent intent = new Intent(this,AnalyticsActivity.class);
                            intent.putExtra("ownerId",vendorId);

                            startActivity(intent);
                            break;

                        case "Payouts":
                            startActivity(new Intent(this,PayoutsActivity.class));
                            break;
                    }

                });

        rvQuickActions.setAdapter(adapter);
    }

    /* ============================================================
       CHART STYLE
    ============================================================ */

    private void setupChartStyle(){

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);
    }

    /* ============================================================
       LOAD DASHBOARD
    ============================================================ */

    private void loadDashboardData(){

        String vendorId = TokenManager.getInstance(this).getUserId();

        if(vendorId == null){
            showError("User session expired");
            return;
        }

        dashboardViewModel.refreshStats(vendorId);

        dashboardViewModel.getDashboardStats(vendorId).observe(this,resource -> {

            if(resource == null) return;

            switch (resource.status){

                case LOADING:

                    progressBar.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(true);
                    break;

                case SUCCESS:

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if(resource.data != null){

                        TokenManager.getInstance(this)
                                .saveMessId(resource.data.getMessId());

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

    }

    /* ============================================================
       UPDATE UI
    ============================================================ */

    private void updateUI(DashboardStats stats){

        tvRestaurantName.setText(stats.getMessName());

        isRestaurantOpen = stats.isOpen();

        updateRestaurantStatusUI();

        tvTotalRevenue.setText(
                String.format(Locale.getDefault(),"₹%.2f",stats.getRevenueToday())
        );

        tvTotalOrders.setText(String.valueOf(stats.getOrdersToday()));

        tvActiveCustomers.setText(String.valueOf(stats.getCustomersToday()));

        tvAverageRating.setText(
                String.format(Locale.getDefault(),"%.1f",stats.getAvgRating())
        );

        if(stats.getWeeklyOrders()!=null){

            setupChart(stats.getWeeklyOrders());

        }

    }

    /* ============================================================
       WEEKLY CHART
    ============================================================ */

    private void setupChart(List<Integer> weeklyOrders){

        if(weeklyOrders == null) return;

        List<BarEntry> entries = new ArrayList<>();

        for(int i=0;i<weeklyOrders.size();i++){

            entries.add(new BarEntry(i,weeklyOrders.get(i)));

        }

        BarDataSet dataSet = new BarDataSet(entries,"");

        dataSet.setColor(Color.parseColor("#7B61FF"));
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);

        barChart.setData(data);

        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

        barChart.getXAxis()
                .setValueFormatter(new IndexAxisValueFormatter(days));

        barChart.animateY(800);

        barChart.invalidate();

    }

    /* ============================================================
       TOGGLE RESTAURANT
    ============================================================ */

    private void toggleRestaurantStatus(){

        String messId = TokenManager.getInstance(this).getMessId();

        dashboardViewModel.toggleRestaurant(messId)
                .observe(this,resource -> {

                    if(resource.status == Resource.Status.SUCCESS){

                        ToggleResponse response = resource.data;

                        if(response!=null){

                            isRestaurantOpen = response.isOpen();

                            updateRestaurantStatusUI();

                        }

                    }else if(resource.status == Resource.Status.ERROR){

                        showError(resource.message);

                    }

                });

    }

    private void updateRestaurantStatusUI(){

        if(isRestaurantOpen){

            tvRestaurantStatus.setText("Restaurant Open");
            tvRestaurantStatus.setTextColor(Color.parseColor("#4CAF50"));
            btnToggleStatus.setText("Close");

        }else{

            tvRestaurantStatus.setText("Restaurant Closed");
            tvRestaurantStatus.setTextColor(Color.RED);
            btnToggleStatus.setText("Open");

        }

    }

    /* ============================================================
       CLEANUP
    ============================================================ */

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SocketManager.off("new_order");
    }

}