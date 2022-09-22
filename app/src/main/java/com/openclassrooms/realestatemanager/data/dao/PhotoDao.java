package com.openclassrooms.realestatemanager.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.domain.models.Photo;

import java.util.List;

/**
 *  Dao interface to manage CRUD actions on photo table
 */

@Dao
public interface PhotoDao {

    //Recover all the photos from the db for a property
    @Query("SELECT * FROM photo_table WHERE property_id = :propertyId")
    LiveData<List<Photo>> getPhotos(int propertyId);

    //Add a new photo to the db
    @Insert
    void addPhoto(Photo photo);

    //Delete a photo from the db
    @Query("DELETE FROM photo_table WHERE photo_id = :photoId")
    void deleteTask(int photoId);
}
