package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.fragments.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.fragments.MapViewFragment;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.ListViewModel;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * Main activity of the app
 */

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    //For ui
    private ActivityMainBinding mBinding;
    private ListViewFragment mListViewFragment;
    private MapViewFragment mMapViewFragment;

    //For data

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureDrawer();
        configureBottomNav();
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
        getSupportFragmentManager().beginTransaction().add(R.id.activity_places_frame_layout, new ListViewFragment()).commit();
        this.mListViewFragment = ListViewFragment.newInstance();
    }

    private void configureDrawer() {
        //To open drawer
        mBinding.toolbar.setNavigationOnClickListener(view -> mBinding.mainActivityLayout.open());

        //Listener on menu
        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search:
                    navigateToSearchActivity();
                    return true;
                case R.id.simulator:
                    navigateToSimulatorActivity();
                    return true;
            }
            mBinding.mainActivityLayout.close();
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_icon:
                //TODO if fragment detail is show method
                navigateToEditActivity();
                return true;
            case R.id.add_icon:
                navigateToAddActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void manageEditIcon() {
        //TODO fragment detail show method
    }

    private void configureBottomNav() {
        mBinding.bottomNav.setOnItemSelectedListener(this::selectBottomNavItem);
    }

    public boolean selectBottomNavItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_list_view:
                if (mListViewFragment == null) {
                    this.mListViewFragment = ListViewFragment.newInstance();
                }
                if (!mListViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mListViewFragment).commit();
                }
                return true;
            case R.id.item_map_view:
                if (mMapViewFragment == null) {
                    this.mMapViewFragment = MapViewFragment.newInstance();
                }
                if (!mMapViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mMapViewFragment).commit();
                }
                return true;
        }
        return false;
    }

    //TODO a generic method to navigate with parameter !!
    private void navigateToSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSimulatorActivity() {
        Intent intent = new Intent(MainActivity.this, SimulatorActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToAddActivity() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToEditActivity() {
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        startActivity(intent);
        finish();
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
