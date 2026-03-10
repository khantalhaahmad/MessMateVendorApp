package com.vendorpro.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.Order;
import com.vendorpro.model.OrderResponse;
import com.vendorpro.network.OrderService;
import com.vendorpro.network.Resource;
import com.vendorpro.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    private final OrderService orderService;

    public OrderRepository(Context context) {

        orderService = RetrofitClient
                .getClient(context)
                .create(OrderService.class);
    }

    /* =============================
       GET ORDERS
    ============================== */

    public MutableLiveData<Resource<List<Order>>> getOrders(
            String ownerId,
            String status
    ) {

        MutableLiveData<Resource<List<Order>>> liveData =
                new MutableLiveData<>();

        liveData.setValue(Resource.loading(null));

        orderService.getOrders(ownerId, status)
                .enqueue(new Callback<OrderResponse>() {

                    @Override
                    public void onResponse(
                            Call<OrderResponse> call,
                            Response<OrderResponse> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getOrders() != null) {

                            liveData.setValue(
                                    Resource.success(
                                            response.body().getOrders()
                                    )
                            );

                        } else {

                            liveData.setValue(
                                    Resource.error(
                                            "No orders found",
                                            null
                                    )
                            );

                        }

                    }

                    @Override
                    public void onFailure(
                            Call<OrderResponse> call,
                            Throwable t
                    ) {

                        liveData.setValue(
                                Resource.error(
                                        t.getMessage(),
                                        null
                                )
                        );

                    }

                });

        return liveData;
    }

    /* =============================
       ACCEPT ORDER
    ============================== */

    public MutableLiveData<Resource<Boolean>> acceptOrder(String orderId) {

        MutableLiveData<Resource<Boolean>> liveData =
                new MutableLiveData<>();

        liveData.setValue(Resource.loading(null));

        orderService.acceptOrder(orderId)
                .enqueue(new Callback<Order>() {

                    @Override
                    public void onResponse(
                            Call<Order> call,
                            Response<Order> response
                    ) {

                        if (response.isSuccessful()) {

                            liveData.setValue(Resource.success(true));

                        } else {

                            liveData.setValue(
                                    Resource.error("Accept failed", false)
                            );
                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Order> call,
                            Throwable t
                    ) {

                        liveData.setValue(
                                Resource.error(t.getMessage(), false)
                        );

                    }

                });

        return liveData;
    }

    /* =============================
       REJECT ORDER
    ============================== */

    public MutableLiveData<Resource<Boolean>> rejectOrder(String orderId) {

        MutableLiveData<Resource<Boolean>> liveData =
                new MutableLiveData<>();

        liveData.setValue(Resource.loading(null));

        orderService.rejectOrder(orderId)
                .enqueue(new Callback<Order>() {

                    @Override
                    public void onResponse(
                            Call<Order> call,
                            Response<Order> response
                    ) {

                        if (response.isSuccessful()) {

                            liveData.setValue(Resource.success(true));

                        } else {

                            liveData.setValue(
                                    Resource.error("Reject failed", false)
                            );
                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Order> call,
                            Throwable t
                    ) {

                        liveData.setValue(
                                Resource.error(t.getMessage(), false)
                        );

                    }

                });

        return liveData;
    }

    /* =============================
       PREPARING ORDER
    ============================== */

    public MutableLiveData<Resource<Boolean>> preparingOrder(String orderId) {

        MutableLiveData<Resource<Boolean>> liveData =
                new MutableLiveData<>();

        liveData.setValue(Resource.loading(null));

        orderService.preparingOrder(orderId)
                .enqueue(new Callback<Order>() {

                    @Override
                    public void onResponse(
                            Call<Order> call,
                            Response<Order> response
                    ) {

                        if (response.isSuccessful()) {

                            liveData.setValue(Resource.success(true));

                        } else {

                            liveData.setValue(
                                    Resource.error("Preparing failed", false)
                            );
                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Order> call,
                            Throwable t
                    ) {

                        liveData.setValue(
                                Resource.error(t.getMessage(), false)
                        );

                    }

                });

        return liveData;
    }

    /* =============================
       READY ORDER
    ============================== */

    public MutableLiveData<Resource<Boolean>> readyOrder(String orderId) {

        MutableLiveData<Resource<Boolean>> liveData =
                new MutableLiveData<>();

        liveData.setValue(Resource.loading(null));

        orderService.readyOrder(orderId)
                .enqueue(new Callback<Order>() {

                    @Override
                    public void onResponse(
                            Call<Order> call,
                            Response<Order> response
                    ) {

                        if (response.isSuccessful()) {

                            liveData.setValue(Resource.success(true));

                        } else {

                            liveData.setValue(
                                    Resource.error("Ready failed", false)
                            );
                        }

                    }

                    @Override
                    public void onFailure(
                            Call<Order> call,
                            Throwable t
                    ) {

                        liveData.setValue(
                                Resource.error(t.getMessage(), false)
                        );

                    }

                });

        return liveData;
    }
}