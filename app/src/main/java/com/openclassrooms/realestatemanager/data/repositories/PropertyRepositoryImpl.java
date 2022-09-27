package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
* Repository for properties
*/

public class PropertyRepositoryImpl {

    //For data
    private final PropertyDao mPropertyDao;
    long lastPropertyId;

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

    //Create a property
    public void addProperty(Property property) {
        mPropertyDao.addProperty(property);
    }

    //Update a property
    public void editProperty(Property property) {
        mPropertyDao.updateProperty(property);
    }

    //Search properties
    public LiveData<List<Property>> getSearchProperties(@Nullable String type,
                                                        String minPrice,
                                                        String maxPrice,
                                                        String minSurface,
                                                        String maxSurface,
                                                        int minRoomNumber,
                                                        int maxRoomNumber,
                                                        String address,
                                                        boolean onSale) {
        return mPropertyDao.getPropertyResearch(type, minPrice, maxPrice, minSurface, maxSurface, minRoomNumber, maxRoomNumber, address, onSale);
    }
}
