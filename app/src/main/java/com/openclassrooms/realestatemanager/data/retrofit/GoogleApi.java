package com.openclassrooms.realestatemanager.data.retrofit;

import com.openclassrooms.realestatemanager.domain.models.nearbysearchpojo.NearbySearchResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get data from Google places API
 */

public interface GoogleApi {

    @GET("nearbysearch/json?")
    Call<NearbySearchResponse> getNearbySearchResponse(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key);
}
