package com.vendorpro.network;

import com.vendorpro.model.Order;
import com.vendorpro.model.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {

    /* =========================
       GET OWNER ORDERS
    ========================= */

    @GET("orders/owner/{ownerId}")
    Call<OrderResponse> getOrders(
            @Path("ownerId") String ownerId,
            @Query("status") String status
    );

    /* =========================
       ACCEPT ORDER
    ========================= */

    @PATCH("orders/{id}/accept")
    Call<Order> acceptOrder(
            @Path("id") String orderId
    );

    /* =========================
       REJECT ORDER
    ========================= */

    @PATCH("orders/{id}/reject")
    Call<Order> rejectOrder(
            @Path("id") String orderId
    );

    /* =========================
       PREPARING ORDER
    ========================= */

    @PATCH("orders/{id}/preparing")
    Call<Order> preparingOrder(
            @Path("id") String orderId
    );

    /* =========================
       READY ORDER
    ========================= */

    @PATCH("orders/{id}/ready")
    Call<Order> readyOrder(
            @Path("id") String orderId
    );

}