package com.vendorpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vendorpro.model.MenuItem;
import com.vendorpro.repository.MenuRepository;
import com.vendorpro.network.Resource;

import java.util.List;

public class MenuViewModel extends AndroidViewModel {

    private final MenuRepository repository;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = new MenuRepository(application);
    }

    // 🔹 Get menu using messId
    public LiveData<Resource<List<MenuItem>>> getMenu(String messId) {
        return repository.getMenu(messId);
    }

    // 🔹 Add menu item
    public LiveData<Resource<MenuItem>> addMenuItem(String messId, MenuItem menuItem) {
        return repository.addMenuItem(messId, menuItem);
    }

    // 🔹 Update menu item
    public LiveData<Resource<MenuItem>> updateMenuItem(String itemId, MenuItem menuItem) {
        return repository.updateMenuItem(itemId, menuItem);
    }

    // 🔹 Delete menu item
    public LiveData<Resource<Boolean>> deleteMenuItem(String itemId) {
        return repository.deleteMenuItem(itemId);
    }
}