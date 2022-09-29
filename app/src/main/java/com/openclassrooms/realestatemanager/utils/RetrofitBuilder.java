package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.data.retrofit.GoogleApi;
import com.openclassrooms.realestatemanager.data.retrofit.StaticMapApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Utils class to manage Retrofit builder
 **/

public class RetrofitBuilder {

    public static final String STATIC_MAP_BASE_URL = "https://maps.google.com/maps/api/";
    public static final String GOOGLE_API_BASE_URL = "https://maps.googleapis.com/maps/api/place/";

    private Retrofit mRetrofit;

    public StaticMapApi buildRetrofitForStaticMap() {
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(STATIC_MAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .build();
        return mRetrofit.create(StaticMapApi.class);
    }

    public GoogleApi buildRetrofitForGoogleApi() {
        mRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(GOOGLE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)).build())
                .build();
        return mRetrofit.create(GoogleApi.class);
    }
}
