package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;

import javax.inject.Inject;

/**
* Repository for properties
*/

public class PropertyRepositoryImpl {

    //For data
    private final PropertyDao mPropertyDao;

    @Inject
    public PropertyRepositoryImpl(PropertyDao propertyDao) {
        mPropertyDao = propertyDao;
    }

    //Get the list of the properties
    public LiveData<List<Property>> getProperties() {
        return mPropertyDao.getProperties();
    }

    //Get a property
    public LiveData<Property> getProperty(int propertyId) {
        return mPropertyDao.getProperty(propertyId);
    }

    //TODO set property attributes to null when no necessary in addProperty and editProperty
    //Create a property
    public void addProperty(Property property) {
        mPropertyDao.addProperty(property);
    }

    //Update a property
    public void editProperty(Property property) {
        mPropertyDao.updateProperty(property);
    }
}
