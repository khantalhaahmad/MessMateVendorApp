package com.vendorpro.network;

import com.vendorpro.model.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MenuService {

    // 🔹 Get menu items for a mess
    @GET("menu/{messId}")
    Call<List<MenuItem>> getMenu(@Path("messId") String messId);

    // 🔹 Add menu item to a mess
    @POST("menu/{messId}")
    Call<MenuItem> addMenuItem(@Path("messId") String messId, @Body MenuItem menuItem);

    // 🔹 Update menu item
    @PUT("menu/{itemId}")
    Call<MenuItem> updateMenuItem(@Path("itemId") String itemId, @Body MenuItem menuItem);

    // 🔹 Delete menu item
    @DELETE("menu/{itemId}")
    Call<Void> deleteMenuItem(@Path("itemId") String itemId);
}