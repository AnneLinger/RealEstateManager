package com.openclassrooms.realestatemanager.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditActivity;
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.mapview.MapViewFragment;
import com.openclassrooms.realestatemanager.ui.search.SearchFragment;
import com.openclassrooms.realestatemanager.ui.simulator.SimulatorFragment;
import com.openclassrooms.realestatemanager.utils.Utils;

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
    private DetailsFragment mDetailsFragment = DetailsFragment.newInstance();
    private SearchFragment mSearchFragment = SearchFragment.newInstance();
    private SimulatorFragment mSimulatorFragment = SimulatorFragment.newInstance();

    //For data

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureDrawer();
        configureBottomNav();
        navigateToPropertyDetails();
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
                    navigateToSearchFragment();
                    return true;
                case R.id.simulator:
                    navigateToSimulatorFragment();
                    return true;
            }
            mBinding.mainActivityLayout.close();
            return false;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navigateToAddActivity();
        return true;
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
    private void navigateToSearchFragment() {
        if (!mSearchFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mSearchFragment).commit();
        }
        mBinding.bottomNav.setVisibility(View.GONE);
        mBinding.mainActivityLayout.closeDrawers();
        mBinding.toolbar.setTitle(this.getString(R.string.search_title));
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    //TODO change call to this method from adapter
    public void navigateToPropertyDetails() {
        //TODO remove button and manage this from adapter
        mBinding.fabSaveProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDetailsFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mDetailsFragment).commit();
                }
                mBinding.bottomNav.setVisibility(View.GONE);
                mBinding.mainActivityLayout.closeDrawers();
                mBinding.toolbar.setTitle(getResources().getString(R.string.property_details_title));
                mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
            }
        });
    }

    private void navigateToSimulatorFragment() {
        if (!mSimulatorFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mSimulatorFragment).commit();
        }
        mBinding.bottomNav.setVisibility(View.GONE);
        mBinding.mainActivityLayout.closeDrawers();
        mBinding.toolbar.setTitle(this.getString(R.string.simulator_title));
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    private void navigateToAddActivity() {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
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
