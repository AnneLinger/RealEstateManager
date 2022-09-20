package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.concurrent.Executor;

import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
* ViewModel to add a property
*/

@HiltViewModel
public class AddViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public AddViewModel(PropertyRepositoryImpl propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
    }

    private void addPropertyToRoomDatabase(Property property) {
        mExecutor.execute(() -> {
            mPropertyRepository.addProperty(property);
        });    }

    public void createProperty(String type, int price, int surface, int roomNumber, @Nullable String description, String address, String city, boolean onSale, String entryDate, String soldDate, String agent) {
        Property property = new Property(type, price, surface, roomNumber, description, address, city, onSale, entryDate, soldDate, agent);
        addPropertyToRoomDatabase(property);
    }
}