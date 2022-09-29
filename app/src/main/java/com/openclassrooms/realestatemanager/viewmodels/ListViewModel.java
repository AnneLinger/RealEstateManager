package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.repositories.PhotoRepositoryImpl;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for list view fragment
 */

@SuppressWarnings("unused")
@HiltViewModel
public class ListViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;
    private final PhotoRepositoryImpl mPhotoRepository;

    @Inject
    public ListViewModel(PropertyRepositoryImpl propertyRepository, PhotoRepositoryImpl photoRepository) {
        mPropertyRepository = propertyRepository;
        mPhotoRepository = photoRepository;
    }

    //.....................................For properties...........................................

    public LiveData<List<Property>> getProperties() {
        return mPropertyRepository.getProperties();
    }

    //.....................................For photos...............................................

    public LiveData<List<Photo>> getAllPhotos() {
        return mPhotoRepository.getAllPhotos();
    }

    public LiveData<List<Photo>> getPropertyPhotos(Long propertyId) {
        return mPhotoRepository.getPropertyPhotos(propertyId);
    }
}
