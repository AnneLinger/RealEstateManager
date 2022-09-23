package com.openclassrooms.realestatemanager.di;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
* Hilt module for injections for executor
*/

@InstallIn(SingletonComponent.class)
@Module
public abstract class ExecutorModule {
    @Provides
    public static Executor provideExecutor(@ApplicationContext Context context) {
        return Executors.newSingleThreadExecutor();
    }
}
