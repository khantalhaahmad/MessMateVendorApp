package com.vendorpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vendorpro.model.MenuItem;
import com.vendorpro.repository.MenuRepository;

import java.util.List;

public class MenuViewModel extends AndroidViewModel {

    private MenuRepository repository;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = new MenuRepository(application);
    }

    public LiveData<com.vendorpro.network.Resource<List<MenuItem>>> getMenu(String vendorId) {
        return repository.getMenu(vendorId);
    }

    public LiveData<com.vendorpro.network.Resource<MenuItem>> addMenuItem(String vendorId, MenuItem menuItem) {
        return repository.addMenuItem(vendorId, menuItem);
    }

    public LiveData<com.vendorpro.network.Resource<MenuItem>> updateMenuItem(String itemId, MenuItem menuItem) {
        return repository.updateMenuItem(itemId, menuItem);
    }

    public LiveData<com.vendorpro.network.Resource<Boolean>> deleteMenuItem(String itemId) {
        return repository.deleteMenuItem(itemId);
    }
}
