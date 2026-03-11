package com.vendorpro.network;

import com.vendorpro.model.Order;
import com.vendorpro.model.OrderResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {

    /* ============================================================
       GET OWNER ORDERS (Vendor Dashboard)
    ============================================================ */

    @GET("orders/owner/{ownerId}")
    Call<OrderResponse> getOrders(
            @Path("ownerId") String ownerId,
            @Query("status") String status
    );

    /* ============================================================
       ACCEPT ORDER
       pending → accepted
    ============================================================ */

    @PATCH("orders/{id}/accept")
    Call<OrderResponse> acceptOrder(
            @Path("id") String orderId
    );

    /* ============================================================
       REJECT ORDER
       pending → cancelled
    ============================================================ */

    @PATCH("orders/{id}/reject")
    Call<OrderResponse> rejectOrder(
            @Path("id") String orderId
    );

    /* ============================================================
       PREPARING ORDER
       accepted → preparing
    ============================================================ */

    @PATCH("orders/{id}/preparing")
    Call<OrderResponse> preparingOrder(
            @Path("id") String orderId
    );

    /* ============================================================
       ORDER READY
       preparing → ready
    ============================================================ */

    @PATCH("orders/{id}/ready")
    Call<OrderResponse> readyOrder(
            @Path("id") String orderId
    );

    /* ============================================================
       ORDER PICKED (Delivery Agent)
       ready → picked
    ============================================================ */

    @PATCH("orders/{id}/picked")
    Call<OrderResponse> pickedOrder(
            @Path("id") String orderId
    );

    /* ============================================================
       ORDER DELIVERED
       picked → delivered
    ============================================================ */

    @PATCH("orders/{id}/delivered")
    Call<OrderResponse> deliveredOrder(
            @Path("id") String orderId
    );

    /* ============================================================
       CANCEL ORDER (Vendor / Admin)
    ============================================================ */

    @PATCH("orders/{id}/cancel")
    Call<OrderResponse> cancelOrder(
            @Path("id") String orderId
    );
}