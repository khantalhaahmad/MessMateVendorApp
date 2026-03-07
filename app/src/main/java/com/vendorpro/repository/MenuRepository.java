package com.vendorpro.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.MenuItem;
import com.vendorpro.network.MenuService;
import com.vendorpro.network.Resource;
import com.vendorpro.network.RetrofitClient;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuRepository {

    private final MenuService menuService;

    public MenuRepository(Context context) {
        menuService = RetrofitClient
                .getClient(context)
                .create(MenuService.class);
    }

    /* =====================================
       🔹 GET MENU (Vendor App)
    ===================================== */

    public MutableLiveData<Resource<List<MenuItem>>> getMenu(String messId) {

        MutableLiveData<Resource<List<MenuItem>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        menuService.getMenu(messId).enqueue(new Callback<List<MenuItem>>() {

            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(Resource.success(response.body()));
                } else {
                    data.setValue(Resource.error("Error fetching menu", null));
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                data.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return data;
    }

    /* =====================================
       🔹 GET AVAILABLE MENU (User App)
    ===================================== */

    public MutableLiveData<Resource<List<MenuItem>>> getAvailableMenu(String messId) {

        MutableLiveData<Resource<List<MenuItem>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        menuService.getAvailableMenu(messId, true)
                .enqueue(new Callback<List<MenuItem>>() {

                    @Override
                    public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(Resource.success(response.body()));
                        } else {
                            data.setValue(Resource.error("Error fetching available menu", null));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                        data.setValue(Resource.error(t.getMessage(), null));
                    }
                });

        return data;
    }

    /* =====================================
       🔹 ADD MENU ITEM (WITH IMAGE)
    ===================================== */

    public MutableLiveData<Resource<MenuItem>> addMenuItem(
            String messId,
            RequestBody name,
            RequestBody description,
            RequestBody price,
            RequestBody isVeg,
            RequestBody category,
            MultipartBody.Part image
    ) {

        MutableLiveData<Resource<MenuItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        menuService.addMenuItem(
                messId,
                name,
                description,
                price,
                isVeg,
                category,
                image
        ).enqueue(new Callback<MenuItem>() {

            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {

                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Error adding item", null));
                }
            }

            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /* =====================================
       🔹 UPDATE MENU ITEM (WITH IMAGE)
    ===================================== */

    public MutableLiveData<Resource<MenuItem>> updateMenuItem(
            String itemId,
            RequestBody name,
            RequestBody description,
            RequestBody price,
            RequestBody isVeg,
            RequestBody category,
            MultipartBody.Part image
    ) {

        MutableLiveData<Resource<MenuItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        menuService.updateMenuItem(
                itemId,
                name,
                description,
                price,
                isVeg,
                category,
                image
        ).enqueue(new Callback<MenuItem>() {

            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {

                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Error updating item", null));
                }
            }

            @Override
            public void onFailure(Call<MenuItem> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /* =====================================
       🔹 DELETE MENU ITEM
    ===================================== */

    public MutableLiveData<Resource<Boolean>> deleteMenuItem(String itemId) {

        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        menuService.deleteMenuItem(itemId).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    result.setValue(Resource.success(true));
                } else {
                    result.setValue(Resource.error("Error deleting item", false));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), false));
            }
        });

        return result;
    }

    /* =====================================
       🔹 TOGGLE AVAILABILITY
    ===================================== */

    public MutableLiveData<Resource<MenuItem>> toggleAvailability(String itemId) {

        MutableLiveData<Resource<MenuItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        menuService.toggleAvailability(itemId)
                .enqueue(new Callback<MenuItem>() {

                    @Override
                    public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(Resource.success(response.body()));
                        } else {
                            result.setValue(Resource.error("Failed to update availability", null));
                        }
                    }

                    @Override
                    public void onFailure(Call<MenuItem> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage(), null));
                    }
                });

        return result;
    }
}