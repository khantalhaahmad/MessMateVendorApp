package com.vendorpro.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.vendorpro.R;
import com.vendorpro.model.Order;
import com.vendorpro.network.Resource;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.OrderViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {

    private static final String ARG_STATUS = "status";

    private String status;
    private String ownerId;

    private OrderViewModel viewModel;
    private OrderAdapter adapter;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static OrderListFragment newInstance(String status) {

        OrderListFragment fragment = new OrderListFragment();

        Bundle args = new Bundle();
        args.putString(ARG_STATUS, status);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            status = getArguments().getString(ARG_STATUS);
        }

        viewModel = new ViewModelProvider(requireActivity())
                .get(OrderViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_order_list,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        adapter = new OrderAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        ownerId = TokenManager
                .getInstance(requireContext())
                .getUserId();

        loadOrders();

        swipeRefreshLayout.setOnRefreshListener(this::loadOrders);
    }

    /* =========================================
       REFRESH WHEN USER RETURNS FROM DETAIL
    ========================================== */

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    /* =========================================
       LOAD ORDERS
    ========================================== */

    private void loadOrders() {

        if (ownerId == null || ownerId.isEmpty()) {

            showEmpty();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        viewModel.getOrders(ownerId, null)
                .observe(getViewLifecycleOwner(), resource -> {

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (resource == null || resource.data == null) {

                        showEmpty();
                        return;
                    }

                    filterAndShowOrders(resource.data);

                });
    }

    /* =========================================
       FILTER ORDERS BY TAB STATUS
    ========================================== */

    private void filterAndShowOrders(List<Order> orders) {

        List<Order> filtered = new ArrayList<>();

        String[] statuses = status.split(",");

        for (Order order : orders) {

            if (order.getStatus() == null) continue;

            for (String s : statuses) {

                if (order.getStatus().equalsIgnoreCase(s.trim())) {

                    filtered.add(order);
                    break;
                }
            }
        }

        if (filtered.isEmpty()) {

            showEmpty();

        } else {

            showOrders(filtered);
        }
    }

    /* =========================================
       SHOW ORDERS
    ========================================== */

    private void showOrders(List<Order> orders) {

        adapter.updateOrders(orders);

        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }

    /* =========================================
       EMPTY STATE
    ========================================== */

    private void showEmpty() {

        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }
}