package com.openclassrooms.realestatemanager.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.database.Go4LunchDatabase;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
*Created by Anne Linger on 15/09/2022.
*/

@InstallIn(SingletonComponent.class)
@Module
public abstract class DatabaseModule {
    @Provides
    public static Go4LunchDatabase provideGo4LunchDatabase(@ApplicationContext Context context) {
        return Go4LunchDatabase.getInstance(context);
    }

    @Provides
    public static PropertyDao providePropertyDao(Go4LunchDatabase go4LunchDatabase) {
        return go4LunchDatabase.mPropertyDao();
    }

    @Provides
    public static PhotoDao providePhotoDao(Go4LunchDatabase go4LunchDatabase) {
        return go4LunchDatabase.mPhotoDao();
    }
}
