package com.vendorpro.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.Order;
import com.vendorpro.model.UpdateStatusRequest;
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

    // ================= GET ORDERS =================
    public MutableLiveData<Resource<List<Order>>> getOrders(String vendorId, String status) {

        MutableLiveData<Resource<List<Order>>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        orderService.getOrders(vendorId, status)
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(
                            Call<List<Order>> call,
                            Response<List<Order>> response) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            liveData.setValue(
                                    Resource.success(response.body())
                            );

                        } else {
                            liveData.setValue(
                                    Resource.error("No orders found", null)
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Order>> call,
                            Throwable t) {

                        liveData.setValue(
                                Resource.error(t.getMessage(), null)
                        );
                    }
                });

        return liveData;
    }

    // ================= UPDATE ORDER STATUS =================
    public MutableLiveData<Resource<Boolean>> updateOrderStatus(
            String orderId,
            String status) {

        MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
        liveData.setValue(Resource.loading(null));

        orderService.updateOrderStatus(
                        orderId,
                        new UpdateStatusRequest(status)
                )
                .enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(
                            Call<Order> call,
                            Response<Order> response) {

                        if (response.isSuccessful()) {

                            liveData.setValue(
                                    Resource.success(true)
                            );

                        } else {
                            liveData.setValue(
                                    Resource.error(
                                            "Failed to update status",
                                            false
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Order> call,
                            Throwable t) {

                        liveData.setValue(
                                Resource.error(t.getMessage(), false)
                        );
                    }
                });

        return liveData;
    }
}
