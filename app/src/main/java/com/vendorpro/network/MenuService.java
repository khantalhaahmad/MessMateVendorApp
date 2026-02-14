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
    @GET("vendor/{vendorId}/menu")
    Call<List<MenuItem>> getMenu(@Path("vendorId") String vendorId);

    @POST("vendor/{vendorId}/menu")
    Call<MenuItem> addMenuItem(@Path("vendorId") String vendorId, @Body MenuItem menuItem);

    @PUT("menu/{itemId}")
    Call<MenuItem> updateMenuItem(@Path("itemId") String itemId, @Body MenuItem menuItem);

    @DELETE("menu/{itemId}")
    Call<Void> deleteMenuItem(@Path("itemId") String itemId);
}
