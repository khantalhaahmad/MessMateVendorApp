package com.vendorpro.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorpro.R;
import com.vendorpro.model.Order;
import java.time.Instant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
       UPDATE LIST
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
           ORDER TIME
        ----------------------------- */

        String orderTime = "-";

        try {

            if(order.getCreatedAt() != null){

                Date date = new Date(order.getCreatedAt());

                SimpleDateFormat sdf =
                        new SimpleDateFormat("HH:mm", Locale.getDefault());

                orderTime = sdf.format(date);
            }

        } catch (Exception ignored){}

        holder.tvOrderDate.setText(orderTime);

        /* -----------------------------
           TOTAL AMOUNT
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

                startTimer(holder, order);
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
       ORDER TIMER (Pending Orders)
    ============================================================ */

    private void startTimer(OrderViewHolder holder, Order order){

        if(order.getOrderExpiresAt() == null) return;

        long expiresAt;

        try{

            Instant instant = Instant.parse(order.getOrderExpiresAt());
            expiresAt = instant.toEpochMilli();

        }catch(Exception e){
            e.printStackTrace();
            return;
        }

        long remaining = expiresAt - System.currentTimeMillis();

        if(remaining <= 0) return;

        new CountDownTimer(remaining,1000){

            @Override
            public void onTick(long millisUntilFinished){

                long sec = millisUntilFinished / 1000;

                holder.timer.setText(sec + "s");

            }

            @Override
            public void onFinish(){

                holder.timer.setText("Expired");

            }

        }.start();
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
        TextView timer;   // 🔥 countdown timer

        OrderViewHolder(@NonNull View itemView) {

            super(itemView);

            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            // ⏱ Order accept timer (60 sec)
            timer = itemView.findViewById(R.id.tvOrderTimer);
        }
    }
}