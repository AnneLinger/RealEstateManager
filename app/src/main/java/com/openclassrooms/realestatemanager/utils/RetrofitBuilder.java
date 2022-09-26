package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.data.retrofit.StaticMapApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utils class to manage Retrofit builder
 **/

public class RetrofitBuilder {

    public static final String BASE_URL = "https://maps.google.com/maps/api/";

    private Retrofit mRetrofit;

    public StaticMapApi buildRetrofit() {
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .build();
        return mRetrofit.create(StaticMapApi.class);
    }
}
