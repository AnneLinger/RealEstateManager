package com.openclassrooms.realestatemanager.ui.addedit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.databinding.ActivityAddEditBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.AddViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 *Activity to create a new property
 */

@AndroidEntryPoint
public class AddEditActivity extends AppCompatActivity {

    //For ui
    private ActivityAddEditBinding mBinding;

    //For data
    private AddViewModel mAddViewModel;

    //TODO Manage display of data and of title depends on data or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureViewModel();
        saveProperty();
    }

    private void initUi() {
        mBinding = ActivityAddEditBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        configureActionBar();
    }

    //TODO : create an alert dialog to confirm cancel add property
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
        Intent intent = new Intent(AddEditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
