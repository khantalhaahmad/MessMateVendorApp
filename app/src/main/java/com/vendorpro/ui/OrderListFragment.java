package com.vendorpro.ui;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
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

import com.google.gson.Gson;
import com.vendorpro.R;
import com.vendorpro.model.Order;
import com.vendorpro.network.Resource;
import com.vendorpro.network.SocketManager;
import com.vendorpro.network.TokenManager;
import com.vendorpro.viewmodel.OrderViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;

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

    private Socket socket;

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

        setupSocket();

        loadOrders();

        swipeRefreshLayout.setOnRefreshListener(this::loadOrders);
    }

    /* =========================================
       SOCKET SETUP
    ========================================== */

    private void setupSocket() {

        socket = SocketManager.getSocket();

        if (socket == null) return;

        SocketManager.connect();
        SocketManager.joinOwner(ownerId);

        /* ================================
           NEW ORDER EVENT
        ================================= */

        SocketManager.on("new_order", args -> {

            if(getActivity()==null) return;

            getActivity().runOnUiThread(() -> {

                try{

                    playOrderAlert();

                    if(args.length == 0) return;

                    JSONObject data = (JSONObject) args[0];

                    Order order = new Gson()
                            .fromJson(data.toString(), Order.class);

                    showNewOrderDialog(order);

                    // IMPORTANT: refresh list
                    loadOrders();

                }catch(Exception e){

                    e.printStackTrace();

                }

            });

        });

        /* ================================
           AUTO CANCEL EVENT
        ================================= */

        SocketManager.on("order_auto_cancelled", args -> {

            if(getActivity()==null) return;

            getActivity().runOnUiThread(this::loadOrders);

        });

        /* ================================
           ORDER STATUS UPDATE
        ================================= */

        SocketManager.on("order_status", args -> {

            if(getActivity()==null) return;

            getActivity().runOnUiThread(this::loadOrders);

        });

    }

    /* =========================================
       SHOW NEW ORDER POPUP
    ========================================== */

    private void showNewOrderDialog(Order order){

        if(order == null) return;

        NewOrderDialog dialog = new NewOrderDialog(
                requireContext(),
                order,
                new NewOrderDialog.Listener() {

                    @Override
                    public void onAccept(Order order) {

                        viewModel.acceptOrder(order.getId())
                                .observe(getViewLifecycleOwner(), r -> {

                                    if(r.status == Resource.Status.SUCCESS){

                                        loadOrders();

                                    }

                                });

                    }

                    @Override
                    public void onReject(Order order) {

                        viewModel.rejectOrder(order.getId())
                                .observe(getViewLifecycleOwner(), r -> {

                                    if(r.status == Resource.Status.SUCCESS){

                                        loadOrders();

                                    }

                                });

                    }
                }
        );

        dialog.show();

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

        System.out.println("Fragment loading orders");
        System.out.println("OwnerId: " + ownerId);
        System.out.println("Status: " + status);

        viewModel.getOrders(ownerId, status)
                .observe(getViewLifecycleOwner(), resource -> {

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    if(resource == null){

                        showEmpty();
                        return;
                    }

                    if(resource.status == Resource.Status.SUCCESS
                            && resource.data != null){

                        showOrders(resource.data);

                    }else{

                        showEmpty();
                    }

                });
    }

    /* =========================================
       SHOW ORDERS
    ========================================== */

    private void showOrders(List<Order> orders) {

        if(orders == null || orders.isEmpty()){

            showEmpty();
            return;
        }

        adapter.updateOrders(orders);

        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
    }

    /* =========================================
       EMPTY STATE
    ========================================== */

    private void showEmpty(){

        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    /* =========================================
       ALERT SOUND
    ========================================== */

    private void playOrderAlert(){

        try{

            MediaPlayer mp = MediaPlayer.create(requireContext(),R.raw.new_order);

            mp.setOnCompletionListener(MediaPlayer::release);

            mp.start();

            Vibrator v = (Vibrator) requireContext()
                    .getSystemService(Context.VIBRATOR_SERVICE);

            if(v!=null){
                v.vibrate(1200);
            }

        }catch(Exception e){

            e.printStackTrace();

        }

    }

    /* =========================================
       REFRESH WHEN BACK
    ========================================== */

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    /* =========================================
       SOCKET CLEANUP
    ========================================== */

    @Override
    public void onDestroyView() {

        super.onDestroyView();

        SocketManager.off("new_order");
        SocketManager.off("order_auto_cancelled");
        SocketManager.off("order_status");

    }

}