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

    private final List<Order> orders = new ArrayList<>();

    public OrderAdapter(List<Order> initialOrders) {

        if (initialOrders != null) {
            orders.addAll(initialOrders);
        }
    }

    /* ============================================================
       UPDATE LIST (Efficient)
    ============================================================ */

    public void updateOrders(List<Order> newOrders) {

        orders.clear();

        if (newOrders != null) {
            orders.addAll(newOrders);
        }

        notifyDataSetChanged();
    }

    /* ============================================================
       CREATE VIEW HOLDER
    ============================================================ */

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

    /* ============================================================
       BIND VIEW HOLDER
    ============================================================ */

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder,
            int position) {

        Order order = orders.get(position);

        /* -----------------------------
           CUSTOMER NAME
        ----------------------------- */

        String customerName =
                order.getCustomerName() != null
                        ? order.getCustomerName()
                        : "Customer";

        holder.tvCustomerName.setText(customerName);

        /* -----------------------------
           ORDER ID
        ----------------------------- */

        String shortId = "N/A";

        if (order.getId() != null && !order.getId().isEmpty()) {

            shortId = order.getId().substring(
                    0,
                    Math.min(order.getId().length(), 8)
            );
        }

        holder.tvOrderId.setText("#" + shortId);

        /* -----------------------------
           DATE
        ----------------------------- */

        String date =
                order.getCreatedAt() != null
                        ? order.getCreatedAt()
                        : "-";

        holder.tvOrderDate.setText(date);

        /* -----------------------------
           AMOUNT
        ----------------------------- */

        double amount = order.getTotalAmount();

        holder.tvTotalAmount.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        amount
                )
        );

        /* -----------------------------
           STATUS
        ----------------------------- */

        String status =
                order.getStatus() != null
                        ? order.getStatus()
                        : "pending";

        holder.tvStatus.setText(status.toUpperCase());

        switch (status.toLowerCase()) {

            case "pending":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#FF9800")
                );

                break;

            case "accepted":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#2196F3")
                );

                break;

            case "preparing":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#9C27B0")
                );

                break;

            case "ready":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#4CAF50")
                );

                break;

            case "picked":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#3F51B5")
                );

                break;

            case "delivered":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#2E7D32")
                );

                break;

            case "cancelled":

                holder.tvStatus.setTextColor(
                        Color.parseColor("#F44336")
                );

                break;

            default:

                holder.tvStatus.setTextColor(Color.GRAY);

        }

        /* -----------------------------
           OPEN ORDER DETAILS
        ----------------------------- */

        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(
                    v.getContext(),
                    OrderDetailActivity.class
            );

            intent.putExtra("order", order);

            v.getContext().startActivity(intent);

        });

    }

    /* ============================================================
       ITEM COUNT
    ============================================================ */

    @Override
    public int getItemCount() {
        return orders.size();
    }

    /* ============================================================
       VIEW HOLDER
    ============================================================ */

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