package com.vendorpro.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vendorpro.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private final List<String> titles;

    public SliderAdapter(List<String> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slider, parent, false);

        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {

        holder.txtTitle.setText(titles.get(position));

        View[] bars = {holder.bar1, holder.bar2, holder.bar3, holder.bar4};

        for (int i = 0; i < bars.length; i++) {

            if (i == position) {
                bars[i].setBackgroundColor(Color.WHITE);
            } else {
                bars[i].setBackgroundColor(Color.parseColor("#55FFFFFF"));
            }

        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;

        View bar1, bar2, bar3, bar4;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);

            bar1 = itemView.findViewById(R.id.bar1);
            bar2 = itemView.findViewById(R.id.bar2);
            bar3 = itemView.findViewById(R.id.bar3);
            bar4 = itemView.findViewById(R.id.bar4);
        }
    }
}