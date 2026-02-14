package com.vendorpro.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return OrderListFragment.newInstance("PENDING");
            case 1: return OrderListFragment.newInstance("ACCEPTED");
            case 2: return OrderListFragment.newInstance("COMPLETED");
            default: return OrderListFragment.newInstance("PENDING");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
