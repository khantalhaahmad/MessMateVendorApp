package com.vendorpro.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorpro.R;
import com.vendorpro.model.MenuItem;

import java.util.List;
import java.util.Locale;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(MenuItem item);
        void onDeleteClick(MenuItem item);
        void onToggleClick(MenuItem item);
    }

    public MenuAdapter(Context context, List<MenuItem> menuItems, OnItemClickListener listener) {
        this.context = context;
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.item_menu, parent, false);

        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {

        MenuItem item = menuItems.get(position);

        holder.tvName.setText(item.getName());
        holder.tvDescription.setText(item.getDescription());

        holder.tvPrice.setText(
                String.format(Locale.getDefault(), "₹%.2f", item.getPrice())
        );

        /* ================================
           Availability Status
        ================================ */

        if (item.isAvailable()) {

            holder.tvStatus.setText("Available");
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32")); // green

        } else {

            holder.tvStatus.setText("Unavailable");
            holder.tvStatus.setTextColor(Color.parseColor("#C62828")); // red
        }

        /* ================================
           Click Listeners
        ================================ */

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(item));

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));

        // 🔹 Toggle availability
        holder.tvStatus.setOnClickListener(v -> listener.onToggleClick(item));
    }

    @Override
    public int getItemCount() {
        return menuItems != null ? menuItems.size() : 0;
    }

    /* =====================================
       ViewHolder
    ===================================== */

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvDescription, tvPrice, tvStatus;
        ImageButton btnEdit, btnDelete;

        public MenuViewHolder(@NonNull View itemView) {

            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}