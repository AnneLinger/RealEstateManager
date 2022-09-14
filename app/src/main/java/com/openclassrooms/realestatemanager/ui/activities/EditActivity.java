package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivityEditBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
*Created by Anne Linger on 14/09/2022.
*/

@AndroidEntryPoint
public class EditActivity extends AppCompatActivity {

    //For ui
    private ActivityEditBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        mBinding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        configureActionBar();
    }

    private void configureActionBar() {
        mBinding.editToolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
