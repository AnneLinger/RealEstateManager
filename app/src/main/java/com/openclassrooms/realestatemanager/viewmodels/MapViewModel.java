package com.openclassrooms.realestatemanager.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.repositories.LocationRepositoryImpl;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for users
 */

@HiltViewModel
public class MapViewModel extends ViewModel {

    //For data
    private final LocationRepositoryImpl mLocationRepositoryImpl;

    @Inject
    public MapViewModel(LocationRepositoryImpl locationRepositoryImpl) {
        mLocationRepositoryImpl = locationRepositoryImpl;
    }

    //..........................For location.....................................................

    @SuppressLint("MissingPermission")
    public void getUserLocation(Context context) {
        mLocationRepositoryImpl.startLocationRequest(context);
    }

    public LiveData<Location> getLivedataLocation() {
        return mLocationRepositoryImpl.getLiveDataLocation();
    }

    public void stopLocationRequest() {
        mLocationRepositoryImpl.stopLocationRequest();
    }
}


