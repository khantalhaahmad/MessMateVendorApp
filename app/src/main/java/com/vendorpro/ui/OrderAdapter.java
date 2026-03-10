package com.vendorpro.ui;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorpro.R;
import com.vendorpro.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders = new ArrayList<>();

    public OrderAdapter(List<Order> orders) {
        if (orders != null) {
            this.orders = orders;
        }
    }

    /* =============================
       UPDATE LIST
    ============================== */

    public void updateOrders(List<Order> newOrders) {

        this.orders.clear();

        if (newOrders != null) {
            this.orders.addAll(newOrders);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder,
            int position) {

        Order order = orders.get(position);

        /* =============================
           CUSTOMER NAME
        ============================== */

        String customerName =
                order.getCustomerName() != null
                        ? order.getCustomerName()
                        : "Customer";

        holder.tvCustomerName.setText(customerName);

        /* =============================
           ORDER ID
        ============================== */

        String orderId = "N/A";

        if (order.getId() != null && !order.getId().isEmpty()) {

            orderId = order.getId()
                    .substring(0,
                            Math.min(order.getId().length(), 8));

        }

        holder.tvOrderId.setText("#" + orderId);

        /* =============================
           DATE
        ============================== */

        holder.tvOrderDate.setText(
                order.getCreatedAt() != null
                        ? order.getCreatedAt()
                        : "-"
        );

        /* =============================
           AMOUNT
        ============================== */

        holder.tvTotalAmount.setText(

                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        order.getTotalAmount()
                )

        );

        /* =============================
           STATUS
        ============================== */

        String status = order.getStatus();

        holder.tvStatus.setText(status);

        if ("pending".equalsIgnoreCase(status)) {

            holder.tvStatus.setTextColor(Color.parseColor("#FF9800"));

        } else if ("accepted".equalsIgnoreCase(status)) {

            holder.tvStatus.setTextColor(Color.parseColor("#2196F3"));

        } else if ("ready".equalsIgnoreCase(status)) {

            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));

        } else {

            holder.tvStatus.setTextColor(Color.GRAY);

        }

        /* =============================
           CLICK → ORDER DETAILS
        ============================== */

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    OrderDetailActivity.class
            );

            intent.putExtra("order", order);

            v.getContext().startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    /* =============================
       VIEW HOLDER
    ============================== */

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName;
        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvTotalAmount;
        TextView tvStatus;

        OrderViewHolder(@NonNull View itemView) {

            super(itemView);

            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}