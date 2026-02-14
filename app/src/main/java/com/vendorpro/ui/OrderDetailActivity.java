package com.vendorpro.ui;

import android.os.Bundle;
import android.view.View;
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

import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;
    private OrderViewModel viewModel;

    private TextView tvCustomerName, tvOrderId, tvStatus, tvItemsList, tvTotal;
    private LinearLayout layoutActions;
    private Button btnAccept, btnReject, btnComplete;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order = (Order) getIntent().getSerializableExtra("order");
        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        initializeViews();
        populateData();
        setupActions();
    }

    private void initializeViews() {
        tvCustomerName = findViewById(R.id.tvDetailCustomerName);
        tvOrderId = findViewById(R.id.tvDetailOrderId);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvItemsList = findViewById(R.id.tvItemsList);
        tvTotal = findViewById(R.id.tvDetailTotal);

        layoutActions = findViewById(R.id.layoutActions);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnComplete = findViewById(R.id.btnComplete);

        progressBar = findViewById(R.id.progressBar);
    }

    private void populateData() {
        if (order == null) return;

        tvCustomerName.setText(
                order.getCustomerName() != null
                        ? order.getCustomerName()
                        : "Unknown"
        );

        tvOrderId.setText(
                order.getId() != null
                        ? "Order #" + order.getId()
                        : "Order #N/A"
        );

        tvStatus.setText(order.getStatus());

        tvTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "Total: ₹%.2f",
                        order.getTotalAmount()
                )
        );

        StringBuilder itemsBuilder = new StringBuilder();
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            for (OrderItem item : order.getItems()) {
                itemsBuilder.append(item.getQuantity())
                        .append(" x ")
                        .append(item.getName())
                        .append(" - ₹")
                        .append(String.format(
                                Locale.getDefault(),
                                "%.2f",
                                item.getPrice()
                        ))
                        .append("\n");
            }
        } else {
            itemsBuilder.append("No items");
        }

        tvItemsList.setText(itemsBuilder.toString());

        updateButtonsVisibility(order.getStatus());
    }

    private void updateButtonsVisibility(String status) {
        if ("PENDING".equalsIgnoreCase(status)) {
            layoutActions.setVisibility(View.VISIBLE);
            btnComplete.setVisibility(View.GONE);
        } else if ("ACCEPTED".equalsIgnoreCase(status)) {
            layoutActions.setVisibility(View.GONE);
            btnComplete.setVisibility(View.VISIBLE);
        } else {
            layoutActions.setVisibility(View.GONE);
            btnComplete.setVisibility(View.GONE);
        }
    }

    private void setupActions() {
        btnAccept.setOnClickListener(v -> updateStatus("ACCEPTED"));
        btnReject.setOnClickListener(v -> updateStatus("REJECTED"));
        btnComplete.setOnClickListener(v -> updateStatus("COMPLETED"));
    }

    private void setActionsEnabled(boolean enabled) {
        btnAccept.setEnabled(enabled);
        btnReject.setEnabled(enabled);
        btnComplete.setEnabled(enabled);
    }

    private void updateStatus(String status) {
        if (order == null) return;

        progressBar.setVisibility(View.VISIBLE);
        setActionsEnabled(false);

        viewModel.updateOrderStatus(order.getId(), status)
                .observe(this, resource -> {

                    progressBar.setVisibility(View.GONE);
                    setActionsEnabled(true);

                    if (resource == null) {
                        Toast.makeText(
                                this,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    if (resource.status == Resource.Status.SUCCESS
                            && Boolean.TRUE.equals(resource.data)) {

                        Toast.makeText(
                                this,
                                "Order " + status,
                                Toast.LENGTH_SHORT
                        ).show();

                        order.setStatus(status);
                        tvStatus.setText(status);
                        updateButtonsVisibility(status);
                        setResult(RESULT_OK);

                    } else if (resource.status == Resource.Status.ERROR) {

                        Toast.makeText(
                                this,
                                resource.message != null
                                        ? resource.message
                                        : "Failed to update status",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}
