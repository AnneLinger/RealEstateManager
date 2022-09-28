package com.openclassrooms.realestatemanager.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;

/**
* Abstract class to instance the Room db
*/

@Database(entities =  {Property.class, Photo.class}, version = 1, exportSchema = false)
public abstract class RealEstateManagerDatabase extends RoomDatabase {

    //Singleton
    private static RealEstateManagerDatabase INSTANCE;

    //Dao
    public abstract PropertyDao mPropertyDao();
    public abstract PhotoDao mPhotoDao();

    //Instance
    public static RealEstateManagerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateManagerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                        context.getApplicationContext(),
                                        RealEstateManagerDatabase.class, "Go4LunchDatabase.db")
                                    .allowMainThreadQueries()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
