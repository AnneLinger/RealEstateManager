package com.openclassrooms.realestatemanager.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.domain.models.Property;

/**
* Abstract class to instance the Room db
*/

@Database(entities =  {Property.class}, version = 1, exportSchema = false)
public abstract class Go4LunchDatabase extends RoomDatabase {

    //Singleton
    private static Go4LunchDatabase INSTANCE;

    //Dao
    public abstract PropertyDao mPropertyDao();

    //Instance
    public static Go4LunchDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Go4LunchDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                        context.getApplicationContext(),
                                        Go4LunchDatabase.class, "Go4LunchDatabase.db")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
