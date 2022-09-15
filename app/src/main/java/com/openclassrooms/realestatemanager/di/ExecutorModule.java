package com.openclassrooms.realestatemanager.di;

import android.content.Context;

import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.database.Go4LunchDatabase;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepository;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.viewmodels.AddViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
* Hilt module for injections for property
*/

@InstallIn(SingletonComponent.class)
@Module
public abstract class ExecutorModule {
    @Provides
    public static Executor provideExecutor(@ApplicationContext Context context) {
        return Executors.newSingleThreadExecutor();
    }
}
