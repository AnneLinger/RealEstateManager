package com.openclassrooms.realestatemanager.di;

import android.content.Context;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.database.RealEstateManagerDatabase;

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
    public static RealEstateManagerDatabase provideGo4LunchDatabase(@ApplicationContext Context context) {
        return RealEstateManagerDatabase.getInstance(context);
    }

    @Provides
    public static PropertyDao providePropertyDao(RealEstateManagerDatabase go4LunchDatabase) {
        return go4LunchDatabase.mPropertyDao();
    }

    @Provides
    public static PhotoDao providePhotoDao(RealEstateManagerDatabase go4LunchDatabase) {
        return go4LunchDatabase.mPhotoDao();
    }
}
