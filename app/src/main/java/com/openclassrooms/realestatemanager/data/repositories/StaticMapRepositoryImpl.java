package com.openclassrooms.realestatemanager.data.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.data.retrofit.StaticMapApi;
import com.openclassrooms.realestatemanager.utils.RetrofitBuilder;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for static map
 */

public class StaticMapRepositoryImpl {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();
    final MutableLiveData<Bitmap> mBitmapMutableLiveData = new MutableLiveData<>();

    @Inject
    public StaticMapRepositoryImpl() {
    }

    public LiveData<Bitmap> getBitmapLiveData() {
        return mBitmapMutableLiveData;
    }

    public void getBitmapFromApi(double latitude, double longitude) {
        String center = latitude + "," + longitude;

        StaticMapApi staticMapApi = mRetrofitBuilder.buildRetrofitForStaticMap();
        int zoom = 12;
        String size = "180x220";
        Call<ResponseBody> call = staticMapApi.getBitmap(size, center, zoom, GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                assert response.body() != null;
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                mBitmapMutableLiveData.setValue(bitmap);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
