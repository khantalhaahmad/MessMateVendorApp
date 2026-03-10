package com.vendorpro.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    private static final int TAB_NEW = 0;
    private static final int TAB_PREPARING = 1;
    private static final int TAB_READY = 2;

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            /* =============================
               NEW ORDERS (Pending)
            ============================== */
            case TAB_NEW:
                return OrderListFragment.newInstance("pending");


            /* =============================
               PREPARING ORDERS
               (Accepted + Preparing)
            ============================== */
            case TAB_PREPARING:
                return OrderListFragment.newInstance("accepted,preparing");


            /* =============================
               READY ORDERS
            ============================== */
            case TAB_READY:
                return OrderListFragment.newInstance("ready");


            default:
                return OrderListFragment.newInstance("pending");
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

}