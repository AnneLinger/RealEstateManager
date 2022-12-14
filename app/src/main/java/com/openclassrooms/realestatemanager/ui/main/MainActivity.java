package com.openclassrooms.realestatemanager.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.mapview.MapViewFragment;
import com.openclassrooms.realestatemanager.ui.search.SearchFragment;
import com.openclassrooms.realestatemanager.ui.simulator.SimulatorFragment;
import com.openclassrooms.realestatemanager.utils.Utils;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * Main activity of the app
 */

@SuppressWarnings("unused")
@RequiresApi(api = Build.VERSION_CODES.M)
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    //For ui
    private ActivityMainBinding mBinding;
    private ListViewFragment mListViewFragment;
    private MapViewFragment mMapViewFragment;
    private SearchFragment mSearchFragment;
    private SimulatorFragment mSimulatorFragment;

    //For data
    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        configureDrawer();
        configureBottomNav();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mListViewFragment = ListViewFragment.newInstance();
            mListViewFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mListViewFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment, new ListViewFragment()).commit();
            this.mListViewFragment = ListViewFragment.newInstance();
        }
    }

    private void initUi() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
    }

    @SuppressLint("NonConstantResourceId")
    private void configureDrawer() {
        //To open drawer
        mBinding.toolbar.setNavigationOnClickListener(view -> mBinding.mainActivityLayout.open());

        //Listener on menu
        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searchFragment:
                    if (mSearchFragment == null) {
                        mSearchFragment = SearchFragment.newInstance();
                    }
                    navigateToFragment(mSearchFragment, R.string.search_title);
                    return true;
                case R.id.simulatorFragment:
                    if (mSimulatorFragment == null) {
                        mSimulatorFragment = SimulatorFragment.newInstance();
                    }
                    navigateToFragment(mSimulatorFragment, R.string.simulator_title);
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

    @SuppressLint("NonConstantResourceId")
    public boolean selectBottomNavItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_list_view:
                if (mListViewFragment == null) {
                    this.mListViewFragment = ListViewFragment.newInstance();
                }
                if (!mListViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mListViewFragment).commit();
                }
                return true;
            case R.id.item_map_view:
                if (mMapViewFragment == null) {
                    this.mMapViewFragment = MapViewFragment.newInstance();
                }
                if (!mMapViewFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mMapViewFragment).commit();
                }
                return true;
        }
        return false;
    }

    private void navigateToFragment(Fragment fragment, int title) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }
        mBinding.bottomNav.setVisibility(View.INVISIBLE);
        mBinding.mainActivityLayout.closeDrawers();
        mBinding.toolbar.setTitle(getString(title));
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
    }

    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText(R.string.text_view_main);
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        this.textViewQuantity.setText(String.valueOf(quantity));
    }
}
