package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.databinding.ActivityAddBinding;
import com.openclassrooms.realestatemanager.viewmodels.AddViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 *Activity to create a new property
 */

@AndroidEntryPoint
public class AddActivity extends AppCompatActivity {

    //For ui
    private ActivityAddBinding mBinding;

    //For data
    private AddViewModel mAddViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        saveProperty();
    }

    private void initUi() {
        mBinding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        configureActionBar();
    }

    private void configureActionBar() {
        mBinding.addToolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void configureViewModel() {
        mAddViewModel = new ViewModelProvider(this).get(AddViewModel.class);
    }

    private void saveProperty() {
        mBinding.fabSaveProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddViewModel.createProperty("House", 120000, 80, 5, null, "12 avenue de la République, 35000 Rennes", true, "13 octobre 2022", null, "Bob Stuart");
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
