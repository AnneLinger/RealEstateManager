package com.openclassrooms.realestatemanager.data.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;

/**
 * Dao interface to manage CRUD actions on property table
 */

@Dao
public interface PropertyDao {

    //Recover all the properties
    @Query("SELECT * FROM property_table")
    LiveData<List<Property>> getProperties();

    //Recover a property from the db with its id
    @Query("SELECT * FROM property_table WHERE id = :propertyId")
    LiveData<Property> getProperty(int propertyId);

    //Add a new property to the db
    @Insert
    void addProperty(Property property);

    //Update a property in the db
    @Update
    void updateProperty(Property property);

    //Search properties depends on multi criteria
    @Query("SELECT * FROM property_table " +
            "WHERE type IS :type " +
            "AND price BETWEEN :minPrice and :maxPrice " +
            "AND surface BETWEEN :minSurface AND :maxSurface " +
            "AND room_number BETWEEN :minRoomNumber AND :maxRoomNumber " +
            "AND address LIKE :address " +
            "AND city LIKE :address " +
            "AND on_sale IS :onSale")
    LiveData<List<Property>> getPropertyResearch(
            @Nullable String type,
            String minPrice,
            String maxPrice,
            String minSurface,
            String maxSurface,
            int minRoomNumber,
            int maxRoomNumber,
            String address,
            boolean onSale);
}
