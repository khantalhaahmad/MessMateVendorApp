package com.vendorpro.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorpro.R;
import com.vendorpro.model.QuickAction;

import java.util.List;

public class DashboardQuickActionAdapter
        extends RecyclerView.Adapter<DashboardQuickActionAdapter.ViewHolder> {

    private final List<QuickAction> actions;
    private final OnActionClickListener listener;

    // Click Interface
    public interface OnActionClickListener {
        void onActionClick(QuickAction action);
    }

    public DashboardQuickActionAdapter(List<QuickAction> actions,
                                       OnActionClickListener listener) {
        this.actions = actions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_quick_action, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        QuickAction action = actions.get(position);

        holder.tvTitle.setText(action.getTitle());
        holder.icon.setImageResource(action.getIcon());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionClick(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return actions != null ? actions.size() : 0;
    }

    // ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView tvTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.actionIcon);
            tvTitle = itemView.findViewById(R.id.actionTitle);
        }
    }
}