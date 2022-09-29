package com.openclassrooms.realestatemanager.di;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Hilt module for injections for executor
 */

@InstallIn(SingletonComponent.class)
@Module
public abstract class ExecutorModule {
    @Provides
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
