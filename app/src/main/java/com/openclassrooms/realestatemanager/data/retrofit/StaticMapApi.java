package com.openclassrooms.realestatemanager.data.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Interface to get bitmap from google static map
 */


public interface StaticMapApi {

    @GET("staticmap?")
    Call<ResponseBody> getBitmap(
            @Query("size") String size,
            @Query("center") String center,
            @Query("zoom") int zoom,
            @Query("key") String key);
}
