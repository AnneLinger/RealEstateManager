package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.repositories.PhotoRepositoryImpl;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
*Created by Anne Linger on 23/09/2022.
*/

@HiltViewModel
public class AddEditPhotoViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;
    private final PhotoRepositoryImpl mPhotoRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public AddEditPhotoViewModel(PropertyRepositoryImpl propertyRepository, PhotoRepositoryImpl photoRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mPhotoRepository = photoRepository;
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
}
