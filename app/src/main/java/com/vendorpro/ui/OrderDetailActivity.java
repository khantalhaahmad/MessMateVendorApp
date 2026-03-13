package com.vendorpro.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.vendorpro.R;
import com.vendorpro.model.Order;
import com.vendorpro.model.OrderItem;
import com.vendorpro.network.Resource;
import com.vendorpro.viewmodel.OrderViewModel;

import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;
    private OrderViewModel viewModel;

    private TextView tvCustomerName, tvOrderId, tvStatus, tvItemsList, tvTotal;

    private LinearLayout layoutPendingActions;
    private Button btnAccept, btnReject;
    private Button btnPreparing;
    private Button btnReady;
    private Button btnDelivered;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order = (Order) getIntent().getSerializableExtra("order");

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        initViews();
        populateData();
        setupActions();
    }

    /* =============================
       INIT VIEWS
    ============================== */

    private void initViews() {

        tvCustomerName = findViewById(R.id.tvDetailCustomerName);
        tvOrderId = findViewById(R.id.tvDetailOrderId);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvItemsList = findViewById(R.id.tvItemsList);
        tvTotal = findViewById(R.id.tvDetailTotal);

        layoutPendingActions = findViewById(R.id.layoutActions);

        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnPreparing = findViewById(R.id.btnPreparing);
        btnReady = findViewById(R.id.btnReady);
        btnDelivered = findViewById(R.id.btnDelivered);

        progressBar = findViewById(R.id.progressBar);
    }

    /* =============================
       POPULATE DATA
    ============================== */

    private void populateData() {

        if (order == null) return;

        tvCustomerName.setText(
                order.getCustomerName() != null
                        ? order.getCustomerName()
                        : "Customer"
        );

        if (order.getId() != null) {

            String shortId = order.getId();

            if (shortId.length() > 8) {
                shortId = shortId.substring(0, 8);
            }

            tvOrderId.setText("Order #" + shortId);
        }

        updateStatusUI(order.getStatus());

        tvTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        order.getTotalAmount()
                )
        );

        buildItemsList(order.getItems());

        updateButtonsVisibility(order.getStatus());
    }

    /* =============================
       UPDATE STATUS UI
    ============================== */

    private void updateStatusUI(String status){

        if(status == null) status = "pending";

        tvStatus.setText(status.toUpperCase());
    }

    /* =============================
       BUILD ITEMS LIST
    ============================== */

    private void buildItemsList(List<OrderItem> items) {

        StringBuilder builder = new StringBuilder();

        if (items != null && !items.isEmpty()) {

            for (OrderItem item : items) {

                builder.append(item.getQuantity())
                        .append(" x ")
                        .append(item.getName())
                        .append(" - ₹")
                        .append(String.format(Locale.getDefault(), "%.2f", item.getPrice()))
                        .append("\n");
            }

        } else {

            builder.append("No items");
        }

        tvItemsList.setText(builder.toString());
    }

    /* =============================
       BUTTON VISIBILITY
    ============================== */

    private void updateButtonsVisibility(String status) {

        layoutPendingActions.setVisibility(LinearLayout.GONE);
        btnPreparing.setVisibility(Button.GONE);
        btnReady.setVisibility(Button.GONE);
        btnDelivered.setVisibility(Button.GONE);

        if ("pending".equalsIgnoreCase(status)) {

            layoutPendingActions.setVisibility(LinearLayout.VISIBLE);

        } else if ("accepted".equalsIgnoreCase(status)) {

            btnPreparing.setVisibility(Button.VISIBLE);

        } else if ("preparing".equalsIgnoreCase(status)) {

            btnReady.setVisibility(Button.VISIBLE);

        } else if ("ready".equalsIgnoreCase(status)) {

            btnDelivered.setVisibility(Button.VISIBLE);
        }
    }

    /* =============================
       BUTTON ACTIONS
    ============================== */

    private void setupActions() {

        btnAccept.setOnClickListener(v -> acceptOrder());
        btnReject.setOnClickListener(v -> rejectOrder());
        btnPreparing.setOnClickListener(v -> startPreparing());
        btnReady.setOnClickListener(v -> markReady());
        btnDelivered.setOnClickListener(v -> markDelivered());
    }

    /* =============================
       LOADING STATE
    ============================== */

    private void setLoading(boolean loading) {

        progressBar.setVisibility(
                loading ? ProgressBar.VISIBLE : ProgressBar.GONE
        );

        btnAccept.setEnabled(!loading);
        btnReject.setEnabled(!loading);
        btnPreparing.setEnabled(!loading);
        btnReady.setEnabled(!loading);
        btnDelivered.setEnabled(!loading);
    }

    /* =============================
       ACCEPT ORDER
    ============================== */

    private void acceptOrder() {

        setLoading(true);

        viewModel.acceptOrder(order.getId()).observe(this, resource -> {

            setLoading(false);

            if (resource.status == Resource.Status.SUCCESS) {

                order.setStatus("accepted");

                updateStatusUI("accepted");
                updateButtonsVisibility("accepted");

                Toast.makeText(this,"Order Accepted",Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
            }
        });
    }

    /* =============================
       REJECT ORDER
    ============================== */

    private void rejectOrder() {

        setLoading(true);

        viewModel.rejectOrder(order.getId()).observe(this, resource -> {

            setLoading(false);

            if (resource.status == Resource.Status.SUCCESS) {

                order.setStatus("cancelled");

                Toast.makeText(this,"Order Rejected",Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);

                finish();
            }
        });
    }

    /* =============================
       START PREPARING
    ============================== */

    private void startPreparing() {

        setLoading(true);

        viewModel.preparingOrder(order.getId()).observe(this, resource -> {

            setLoading(false);

            if (resource.status == Resource.Status.SUCCESS) {

                order.setStatus("preparing");

                updateStatusUI("preparing");
                updateButtonsVisibility("preparing");

                Toast.makeText(this,"Cooking Started",Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
            }
        });
    }

    /* =============================
       MARK ORDER READY
    ============================== */

    private void markReady() {

        setLoading(true);

        viewModel.readyOrder(order.getId()).observe(this, resource -> {

            setLoading(false);

            if (resource.status == Resource.Status.SUCCESS) {

                order.setStatus("ready");

                updateStatusUI("ready");
                updateButtonsVisibility("ready");

                Toast.makeText(this,"Order Ready",Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);
            }
        });
    }

    /* =============================
       MARK ORDER DELIVERED
    ============================== */

    private void markDelivered() {

        setLoading(true);

        viewModel.deliveredOrder(order.getId()).observe(this, resource -> {

            setLoading(false);

            if (resource.status == Resource.Status.SUCCESS) {

                order.setStatus("delivered");

                updateStatusUI("delivered");

                Toast.makeText(this,"Order Delivered",Toast.LENGTH_SHORT).show();

                setResult(RESULT_OK);

                finish();
            }
        });
    }
}