package com.openclassrooms.realestatemanager.di;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Injection of Hilt for app
 */

@SuppressWarnings("EmptyMethod")
@HiltAndroidApp
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
