package com.vendorpro.network;

import com.vendorpro.model.MenuItem;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MenuService {

    /* =====================================
       🔹 GET MENU ITEMS (Vendor App)
    ===================================== */

    @GET("menu/{messId}")
    Call<List<MenuItem>> getMenu(
            @Path("messId") String messId
    );

    /* =====================================
       🔹 GET ONLY AVAILABLE ITEMS (User App)
    ===================================== */

    @GET("menu/{messId}")
    Call<List<MenuItem>> getAvailableMenu(
            @Path("messId") String messId,
            @Query("available") boolean available
    );

    /* =====================================
       🔹 ADD MENU ITEM WITH IMAGE
    ===================================== */

    @Multipart
    @POST("menu/{messId}")
    Call<MenuItem> addMenuItem(

            @Path("messId") String messId,

            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("isVeg") RequestBody isVeg,
            @Part("category") RequestBody category,

            @Part MultipartBody.Part image
    );

    /* =====================================
       🔹 UPDATE MENU ITEM WITH IMAGE
    ===================================== */

    @Multipart
    @PUT("menu/{itemId}")
    Call<MenuItem> updateMenuItem(

            @Path("itemId") String itemId,

            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("isVeg") RequestBody isVeg,
            @Part("category") RequestBody category,

            @Part MultipartBody.Part image
    );

    /* =====================================
       🔹 DELETE MENU ITEM
    ===================================== */

    @DELETE("menu/{itemId}")
    Call<Void> deleteMenuItem(
            @Path("itemId") String itemId
    );

    /* =====================================
       🔹 TOGGLE AVAILABILITY
    ===================================== */

    @PATCH("menu/{itemId}/availability")
    Call<MenuItem> toggleAvailability(
            @Path("itemId") String itemId
    );
}