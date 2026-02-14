package com.vendorpro.network;

import com.vendorpro.model.Order;
import com.vendorpro.model.UpdateStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    @GET("vendor/{vendorId}/orders")
    Call<List<Order>> getOrders(@Path("vendorId") String vendorId, @Query("status") String status);

    @PUT("orders/{orderId}/status")
    Call<Order> updateOrderStatus(@Path("orderId") String orderId, @Body UpdateStatusRequest request);
}
