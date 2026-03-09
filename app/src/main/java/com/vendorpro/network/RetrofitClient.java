package com.vendorpro.network;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:4000/api/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(android.content.Context context) {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()

                    .addInterceptor(new AuthInterceptor(TokenManager.getInstance(context)))

                    .addInterceptor(chain -> {

                        Request request = chain.request();

                        Log.d("API_DEBUG","API URL: "+request.url());

                        return chain.proceed(request);

                    })

                    .build();

            retrofit = new Retrofit.Builder()

                    .baseUrl(BASE_URL)

                    .client(client)

                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }

        return retrofit;
    }
}