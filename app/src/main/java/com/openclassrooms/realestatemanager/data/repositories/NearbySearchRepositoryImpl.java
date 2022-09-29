package com.openclassrooms.realestatemanager.data.repositories;

import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.data.retrofit.GoogleApi;
import com.openclassrooms.realestatemanager.domain.models.nearbysearchpojo.NearbySearchResponse;
import com.openclassrooms.realestatemanager.domain.models.nearbysearchpojo.Result;
import com.openclassrooms.realestatemanager.utils.RetrofitBuilder;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for NearbySearch queries
 */

public class NearbySearchRepositoryImpl {

    //For data
    private static final String GOOGLE_PLACE_API_KEY = BuildConfig.MAPS_API_KEY;
    private final int radius = 600;
    private String type;
    final MutableLiveData<List<Result>> mNearbySearchSchoolResponseLiveData = new MutableLiveData<>();
    final MutableLiveData<List<Result>> mNearbySearchSupermarketResponseLiveData = new MutableLiveData<>();
    final MutableLiveData<List<Result>> mNearbySearchParkResponseLiveData = new MutableLiveData<>();
    private final RetrofitBuilder mRetrofitBuilder = new RetrofitBuilder();

    @Inject
    public NearbySearchRepositoryImpl() {
    }

    //.....................................For schools..............................................

    public LiveData<List<Result>> getNearbySearchSchoolsResponseLiveData() {
        return mNearbySearchSchoolResponseLiveData;
    }

    public void fetchNearbySearchSchools(String location) {
        LruCache<String, NearbySearchResponse> mCache = new LruCache<>(2000);
        NearbySearchResponse existing = mCache.get("nearbySearchSchools");

        if (existing != null) {
            mNearbySearchSchoolResponseLiveData.setValue(existing.getResults());
        } else {
            GoogleApi googleApi = mRetrofitBuilder.buildRetrofitForGoogleApi();
            type = "primary_school";
            Call<NearbySearchResponse> call = googleApi.getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    assert response.body() != null;
                    mCache.put("nearbySearchSchools", response.body());
                    mNearbySearchSchoolResponseLiveData.setValue(response.body().getResults());
                }

                @Override
                public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    //.....................................For supermarkets..........................................

    public LiveData<List<Result>> getNearbySearchSupermarketResponseLiveData() {
        return mNearbySearchSupermarketResponseLiveData;
    }

    public void fetchNearbySearchSupermarket(String location) {
        LruCache<String, NearbySearchResponse> mCache = new LruCache<>(2000);
        NearbySearchResponse existing = mCache.get("nearbySearchSupermarket");

        if (existing != null) {
            mNearbySearchSupermarketResponseLiveData.setValue(existing.getResults());
        } else {
            GoogleApi googleApi = mRetrofitBuilder.buildRetrofitForGoogleApi();
            type = "supermarket";
            Call<NearbySearchResponse> call = googleApi.getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    assert response.body() != null;
                    mCache.put("nearbySearchSupermarket", response.body());
                    mNearbySearchSupermarketResponseLiveData.setValue(response.body().getResults());
                }

                @Override
                public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    //.....................................For parks................................................

    public LiveData<List<Result>> getNearbySearchParksResponseLiveData() {
        return mNearbySearchParkResponseLiveData;
    }

    public void fetchNearbySearchParks(String location) {
        LruCache<String, NearbySearchResponse> mCache = new LruCache<>(2000);
        NearbySearchResponse existing = mCache.get("nearbySearchParks");

        if (existing != null) {
            mNearbySearchParkResponseLiveData.setValue(existing.getResults());
        } else {
            GoogleApi googleApi = mRetrofitBuilder.buildRetrofitForGoogleApi();
            type = "park";
            Call<NearbySearchResponse> call = googleApi.getNearbySearchResponse(location, radius, type, GOOGLE_PLACE_API_KEY);
            call.enqueue(new Callback<NearbySearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                    assert response.body() != null;
                    mCache.put("nearbySearchParks", response.body());
                    mNearbySearchParkResponseLiveData.setValue(response.body().getResults());
                }

                @Override
                public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
