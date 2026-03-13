package com.vendorpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vendorpro.model.Order;
import com.vendorpro.network.Resource;
import com.vendorpro.repository.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private final OrderRepository repository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderRepository(application);
    }

    /* ============================================================
       GET ORDERS BY STATUS
    ============================================================ */

    public LiveData<Resource<List<Order>>> getOrders(
            String ownerId,
            String status
    ) {
        return repository.getOrders(ownerId, status);
    }

    /* ============================================================
       GET ALL ORDERS
    ============================================================ */

    public LiveData<Resource<List<Order>>> getAllOrders(String ownerId) {
        return repository.getAllOrders(ownerId);
    }

    /* ============================================================
       GET SINGLE ORDER
    ============================================================ */

    public LiveData<Resource<Order>> getOrderById(String orderId) {
        return repository.getOrderById(orderId);
    }

    /* ============================================================
       ACCEPT ORDER
    ============================================================ */

    public LiveData<Resource<Boolean>> acceptOrder(String orderId) {
        return repository.acceptOrder(orderId);
    }

    /* ============================================================
       REJECT ORDER
    ============================================================ */

    public LiveData<Resource<Boolean>> rejectOrder(String orderId) {
        return repository.rejectOrder(orderId);
    }

    /* ============================================================
       PREPARING ORDER
    ============================================================ */

    public LiveData<Resource<Boolean>> preparingOrder(String orderId) {
        return repository.preparingOrder(orderId);
    }

    /* ============================================================
       READY ORDER
    ============================================================ */

    public LiveData<Resource<Boolean>> readyOrder(String orderId) {
        return repository.readyOrder(orderId);
    }

    /* ============================================================
       DELIVERED ORDER (NEW)
       ready → delivered
    ============================================================ */

    public LiveData<Resource<Boolean>> deliveredOrder(String orderId) {
        return repository.deliveredOrder(orderId);
    }

    /* ============================================================
       CANCEL ORDER
    ============================================================ */

    public LiveData<Resource<Boolean>> cancelOrder(String orderId) {
        return repository.cancelOrder(orderId);
    }
}