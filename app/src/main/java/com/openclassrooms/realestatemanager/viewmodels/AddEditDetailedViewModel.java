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
* ViewModel to add a property
*/

@HiltViewModel
public class AddEditDetailedViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;
    private final PhotoRepositoryImpl mPhotoRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public AddEditDetailedViewModel(PropertyRepositoryImpl propertyRepository, PhotoRepositoryImpl photoRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mPhotoRepository = photoRepository;
        mExecutor = executor;
    }

    //.....................................For properties...........................................

    private void addPropertyToRoomDatabase(Property property) {
        mExecutor.execute(() -> {
            mPropertyRepository.addProperty(property);
        });    }

    public void createProperty(Property property) {
        addPropertyToRoomDatabase(property);
    }

    public LiveData<List<Property>> getProperties() {
        return mPropertyRepository.getProperties();
    }

    public void editProperty(Property property) {
        mExecutor.execute(() -> {
            mPropertyRepository.editProperty(property);
        });
    }

    //.....................................For photos...............................................

    public void editPhoto(Photo photo) {
        mExecutor.execute(() -> {
            mPhotoRepository.editPhoto(photo);
        });
    }

    public void deletePhoto(int photoId) {
        mExecutor.execute(() -> {
            mPhotoRepository.deletePhoto(photoId);
        });
    }
}
