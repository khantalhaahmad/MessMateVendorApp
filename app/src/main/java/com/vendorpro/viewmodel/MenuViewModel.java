package com.vendorpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.vendorpro.model.MenuItem;
import com.vendorpro.network.Resource;
import com.vendorpro.repository.MenuRepository;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MenuViewModel extends AndroidViewModel {

    private final MenuRepository repository;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        repository = new MenuRepository(application);
    }

    /* =====================================
       🔹 GET MENU (Vendor App)
    ===================================== */

    public LiveData<Resource<List<MenuItem>>> getMenu(String messId) {
        return repository.getMenu(messId);
    }

    /* =====================================
       🔹 GET AVAILABLE MENU (User App)
    ===================================== */

    public LiveData<Resource<List<MenuItem>>> getAvailableMenu(String messId) {
        return repository.getAvailableMenu(messId);
    }

    /* =====================================
       🔹 ADD MENU ITEM WITH IMAGE
    ===================================== */

    public LiveData<Resource<MenuItem>> addMenuItem(
            String messId,
            RequestBody name,
            RequestBody description,
            RequestBody price,
            RequestBody isVeg,
            RequestBody category,
            MultipartBody.Part image
    ) {

        return repository.addMenuItem(
                messId,
                name,
                description,
                price,
                isVeg,
                category,
                image
        );
    }

    /* =====================================
       🔹 UPDATE MENU ITEM WITH IMAGE
    ===================================== */

    public LiveData<Resource<MenuItem>> updateMenuItem(
            String itemId,
            RequestBody name,
            RequestBody description,
            RequestBody price,
            RequestBody isVeg,
            RequestBody category,
            MultipartBody.Part image
    ) {

        return repository.updateMenuItem(
                itemId,
                name,
                description,
                price,
                isVeg,
                category,
                image
        );
    }

    /* =====================================
       🔹 DELETE MENU ITEM
    ===================================== */

    public LiveData<Resource<Boolean>> deleteMenuItem(String itemId) {
        return repository.deleteMenuItem(itemId);
    }

    /* =====================================
       🔹 TOGGLE AVAILABILITY
    ===================================== */

    public LiveData<Resource<MenuItem>> toggleAvailability(String itemId) {
        return repository.toggleAvailability(itemId);
    }
}