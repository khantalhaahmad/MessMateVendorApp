package com.vendorpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.Order;
import com.vendorpro.repository.OrderRepository;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository repository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        repository = new OrderRepository(application);
    }

    public LiveData<com.vendorpro.network.Resource<List<Order>>> getOrders(String vendorId, String status) {
        return repository.getOrders(vendorId, status);
    }

    public LiveData<com.vendorpro.network.Resource<Boolean>> updateOrderStatus(String orderId, String status) {
        return repository.updateOrderStatus(orderId, status);
    }
}
