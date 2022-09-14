package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.databinding.ActivitySimulatorBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
*Activity for home loan simulator
*/

@AndroidEntryPoint
public class SimulatorActivity extends AppCompatActivity {

    //For ui
    private ActivitySimulatorBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
    }

    private void initUi() {
        mBinding = ActivitySimulatorBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        configureActionBar();
    }

    private void configureActionBar() {
        mBinding.simulatorToolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SimulatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
