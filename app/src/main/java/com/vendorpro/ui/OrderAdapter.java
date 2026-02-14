package com.vendorpro.ui;

import android.content.Intent;
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

    public OrderAdapter(List<Order> orders) {
        if (orders != null) {
            this.orders.addAll(orders);
        }
    }

    // 🔄 Call this instead of creating adapter again
    public void updateList(List<Order> newOrders) {
        orders.clear();
        if (newOrders != null) {
            orders.addAll(newOrders);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OrderViewHolder holder,
            int position) {

        Order order = orders.get(position);

        holder.tvCustomerName.setText(
                order.getCustomerName() != null
                        ? order.getCustomerName()
                        : "Unknown"
        );

        // 🔐 Safe Order ID
        String orderId = "N/A";
        if (order.getId() != null && order.getId().length() > 0) {
            orderId = order.getId()
                    .substring(0, Math.min(order.getId().length(), 8));
        }
        holder.tvOrderId.setText("#" + orderId);

        holder.tvOrderDate.setText(
                order.getCreatedAt() != null
                        ? order.getCreatedAt()
                        : ""
        );

        holder.tvTotalAmount.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        order.getTotalAmount()
                )
        );

        holder.tvStatus.setText(order.getStatus());

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

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName, tvOrderId, tvOrderDate, tvTotalAmount, tvStatus;

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
