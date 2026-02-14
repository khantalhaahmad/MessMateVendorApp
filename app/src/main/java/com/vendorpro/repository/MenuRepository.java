package com.vendorpro.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import com.vendorpro.model.MenuItem;
import com.vendorpro.network.MenuService;
import com.vendorpro.network.Resource;
import com.vendorpro.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuRepository {

    private MenuService menuService;

    public MenuRepository(Context context) {
        menuService = RetrofitClient.getClient(context).create(MenuService.class);
    }

    public MutableLiveData<Resource<List<MenuItem>>> getMenu(String vendorId) {
        MutableLiveData<Resource<List<MenuItem>>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));
        menuService.getMenu(vendorId).enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful()) {
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

    public MutableLiveData<Resource<MenuItem>> addMenuItem(String vendorId, MenuItem menuItem) {
        MutableLiveData<Resource<MenuItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        menuService.addMenuItem(vendorId, menuItem).enqueue(new Callback<MenuItem>() {
            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                if (response.isSuccessful()) {
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

    public MutableLiveData<Resource<MenuItem>> updateMenuItem(String itemId, MenuItem menuItem) {
        MutableLiveData<Resource<MenuItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        menuService.updateMenuItem(itemId, menuItem).enqueue(new Callback<MenuItem>() {
            @Override
            public void onResponse(Call<MenuItem> call, Response<MenuItem> response) {
                if (response.isSuccessful()) {
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
}
