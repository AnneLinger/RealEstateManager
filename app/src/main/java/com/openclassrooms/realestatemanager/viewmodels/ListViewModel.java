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
*Created by Anne Linger on 15/09/2022.
*/

@HiltViewModel
public class ListViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;
    private final PhotoRepositoryImpl mPhotoRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public ListViewModel(PropertyRepositoryImpl propertyRepository, PhotoRepositoryImpl photoRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mPhotoRepository = photoRepository;
        mExecutor = executor;
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
