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
import com.vendorpro.network.Resource;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.OrderViewModel;

public class OrderListFragment extends Fragment {

    private static final String ARG_STATUS = "status";

    private String status;
    private String vendorId;

    private OrderViewModel viewModel;
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

        viewModel = new ViewModelProvider(this).get(OrderViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_order_list, container, false);
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

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // ✅ CORRECT: use singleton TokenManager
        vendorId = TokenManager
                .getInstance(requireContext())
                .getUserId();

        loadOrders();

        swipeRefreshLayout.setOnRefreshListener(this::loadOrders);
    }

    private void loadOrders() {
        if (vendorId == null || vendorId.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        viewModel.getOrders(vendorId, status)
                .observe(getViewLifecycleOwner(), resource -> {

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if (resource == null) {
                        recyclerView.setAdapter(null);
                        tvEmpty.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (resource.status == Resource.Status.SUCCESS
                            && resource.data != null
                            && !resource.data.isEmpty()) {

                        recyclerView.setAdapter(
                                new OrderAdapter(resource.data)
                        );
                        tvEmpty.setVisibility(View.GONE);

                    } else {
                        recyclerView.setAdapter(null);
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }
}
