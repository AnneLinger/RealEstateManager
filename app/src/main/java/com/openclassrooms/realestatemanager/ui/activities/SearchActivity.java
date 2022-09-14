package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.AnimatorRes;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivitySearchBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
*Activity to make researches on properties
*/

@AndroidEntryPoint
public class SearchActivity extends AppCompatActivity {

    //For ui
    private ActivitySearchBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        mBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        configureActionBar();
    }

    private void configureActionBar() {
        mBinding.searchToolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
