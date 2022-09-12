package com.openclassrooms.realestatemanager.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;

import dagger.hilt.android.AndroidEntryPoint;

/**
 *Activity to create a new property
 */

@AndroidEntryPoint
public class PropertyCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_creation);
    }
}
