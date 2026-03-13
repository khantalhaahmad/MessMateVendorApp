package com.vendorpro.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/* ============================================================
   ORDERS PAGER ADAPTER
   Controls Vendor Order Tabs
============================================================ */

public class OrdersPagerAdapter extends FragmentStateAdapter {

    /* ============================================================
       TAB INDEXES
    ============================================================ */

    private static final int TAB_NEW = 0;
    private static final int TAB_PREPARING = 1;
    private static final int TAB_READY = 2;
    private static final int TAB_COMPLETED = 3;

    /* ============================================================
       ORDER STATUS FILTERS
       Must match backend order status
    ============================================================ */

    private static final String STATUS_NEW = "pending";

    private static final String STATUS_PREPARING = "preparing";

    private static final String STATUS_READY = "ready";

    private static final String STATUS_COMPLETED = "delivered";

    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /* ============================================================
       CREATE FRAGMENT FOR EACH TAB
    ============================================================ */

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            /* =============================
               NEW ORDERS
               pending
            ============================== */

            case TAB_NEW:

                return OrderListFragment.newInstance(
                        STATUS_NEW
                );

            /* =============================
               PREPARING ORDERS
               preparing
            ============================== */

            case TAB_PREPARING:

                return OrderListFragment.newInstance(
                        STATUS_PREPARING
                );

            /* =============================
               READY ORDERS
               ready
            ============================== */

            case TAB_READY:

                return OrderListFragment.newInstance(
                        STATUS_READY
                );

            /* =============================
               COMPLETED ORDERS
               delivered
            ============================== */

            case TAB_COMPLETED:

                return OrderListFragment.newInstance(
                        STATUS_COMPLETED
                );

            /* =============================
               FALLBACK
            ============================== */

            default:

                return OrderListFragment.newInstance(
                        STATUS_NEW
                );
        }

    }

    /* ============================================================
       TOTAL TABS
    ============================================================ */

    @Override
    public int getItemCount() {
        return 4;
    }

}