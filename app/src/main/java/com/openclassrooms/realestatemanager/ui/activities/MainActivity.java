package com.openclassrooms.realestatemanager.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * Main activity of the app
 */

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    //For ui
    private ActivityMainBinding mBinding;

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    private void initUi() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
    }

    private void configureDrawer() {
        mBinding.toolbar.setNavigationOnClickListener(view -> mBinding.mainActivityLayout.open());
    }

    private void configureTextViewMain(){
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity(){
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(String.valueOf(quantity));
    }
}
