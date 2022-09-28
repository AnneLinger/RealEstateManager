package com.openclassrooms.realestatemanager.data.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

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
    @RawQuery
    List<Property> getPropertyResearch(SupportSQLiteQuery query);
}
