package com.openclassrooms.realestatemanager.ui.main;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditDetailedFragment;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
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
    private DetailsFragment mDetailsFragment;
    private SearchFragment mSearchFragment;
    private SimulatorFragment mSimulatorFragment;
    private AddEditGeneralFragment mAddEditGeneralFragment;
    private AddEditDetailedFragment mAddEditDetailedFragment;

    //For navigation
    private NavController mNavController;

    //For data

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureDrawer();
        configureBottomNav();
        //navigateToPropertyDetails();
    }

    private void initUi() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, mNavController);
        this.mListViewFragment = ListViewFragment.newInstance();
    }

    private void configureDrawer() {
        //To open drawer
        mBinding.toolbar.setNavigationOnClickListener(view -> mBinding.mainActivityLayout.open());

        //Listener on menu
        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searchFragment:
                    navigateToSearchFragment();
                    return true;
                case R.id.simulatorFragment:
                    navigateToSimulatorFragment();
                    return true;
            }
            mBinding.mainActivityLayout.close();
            return false;
        });
    }

    //Clear focus when user touches anywhere
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
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
                    mNavController.navigate(R.id.action_mapViewFragment3_to_listViewFragment);
                }
                return true;
            case R.id.item_map_view:
                if (mMapViewFragment == null) {
                    this.mMapViewFragment = MapViewFragment.newInstance();
                }
                if (!mMapViewFragment.isVisible()) {
                    mNavController.navigate(R.id.action_listViewFragment_to_mapViewFragment3);
                }
                return true;
        }
        return false;
    }

    //TODO a generic method to navigate with parameter !!
    private void navigateToSearchFragment() {
        if (mSearchFragment == null) {
            this.mSearchFragment = SearchFragment.newInstance();
        }
        if (!mSearchFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mSearchFragment).commit();
        }
        mBinding.bottomNav.setVisibility(View.INVISIBLE);
        mBinding.mainActivityLayout.closeDrawers();
        mBinding.toolbar.setTitle(this.getString(R.string.search_title));
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    //TODO change call to this method from adapter
    public void navigateToPropertyDetails() {
        //TODO remove button and manage this from adapter
        /**mBinding.fabSaveProperty.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }

    private void navigateToSimulatorFragment() {
        if (mSimulatorFragment == null) {
            this.mSimulatorFragment = SimulatorFragment.newInstance();
        }
        if (!mSimulatorFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mSimulatorFragment).commit();
        }
        mBinding.bottomNav.setVisibility(View.GONE);
        mBinding.mainActivityLayout.closeDrawers();
        mBinding.toolbar.setTitle(this.getString(R.string.simulator_title));
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
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
