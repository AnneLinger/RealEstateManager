package com.openclassrooms.realestatemanager.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.data.repositories.PhotoRepositoryImpl;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.data.repositories.StaticMapRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
*Created by Anne Linger on 21/09/2022.
*/

@HiltViewModel
public class DetailsViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;
    private final PhotoRepositoryImpl mPhotoRepository;
    private final StaticMapRepositoryImpl mStaticMapRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public DetailsViewModel(PropertyRepositoryImpl propertyRepository, PhotoRepositoryImpl photoRepository, StaticMapRepositoryImpl staticMapRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mPhotoRepository = photoRepository;
        mStaticMapRepository = staticMapRepository;
        mExecutor = executor;
    }

    //.....................................For properties...........................................

    public LiveData<List<Property>> getProperties() {
        return mPropertyRepository.getProperties();
    }

    //.....................................For photos...............................................

    public LiveData<List<Photo>> getPropertyPhotos(Long propertyId) {
        return mPhotoRepository.getPropertyPhotos(propertyId);
    }

    private void addPhotoToRoomDatabase(Photo photo) {
        mExecutor.execute(() -> {
            mPhotoRepository.addPhoto(photo);
        });    }

    public Photo createPhoto(Long propertyId, String photoUri, String photoLabel){
        Photo photo = new Photo(propertyId, photoUri, photoLabel);
        addPhotoToRoomDatabase(photo);
        return photo;
    }

    public void deletePhoto(int photoId) {
        mExecutor.execute(() -> {
            mPhotoRepository.deletePhoto(photoId);
        });
    }

    //.....................................For static map............................................

    public void getBitmapFromApi(double latitude, double longitude) {
        mStaticMapRepository.getBitmapFromApi(latitude, longitude);
    }

    public LiveData<Bitmap> getBitmapLiveData() {
        return mStaticMapRepository.getBitmapLiveData();
    }
}
