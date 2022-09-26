package com.openclassrooms.realestatemanager.data.repositories;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
*Created by Anne Linger on 26/09/2022.
*/

public class StaticMapRepositoryImpl {

    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private final int zoom = 13;
    private final String size = "180x220";
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();
    final MutableLiveData<Bitmap> mBitmapMutableLiveData = new MutableLiveData<>();

    @Inject
    public StaticMapRepositoryImpl(){
    }

    public LiveData<Bitmap> getBitmapLiveData() {
        return mBitmapMutableLiveData;
    }

    public void getBitmapFromApi(double latitude, double longitude) {
        Log.e("getBitmapRepo", "getBR");
        String center = String.valueOf(latitude) + "," + String.valueOf(longitude);
        Log.e("center in repo", center);

        StaticMapApi staticMapApi = mRetrofitBuilder.buildRetrofit();
        Call<ResponseBody> call = staticMapApi.getBitmap(size, center, zoom, GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                assert response.body() != null;
                Log.e("bitmap after observe", "Response ok");
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
