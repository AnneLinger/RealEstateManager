package com.openclassrooms.realestatemanager.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.listview.ListViewFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

/**
*Activity to make researches on properties
*/

public class SearchFragment extends Fragment {

    //For ui
    private FragmentSearchBinding mBinding;

    //For data
    private SearchViewModel mSearchViewModel;
    private String type;
    private String minPrice = "0";
    private String maxPrice = "10000000000";
    private int minRoomNumber = 0;
    private int maxRoomNumber = 100;
    private String minSurface = "0";
    private String maxSurface = "800";
    private String location = "";
    private boolean onSale = true;
    private TextInputEditText typeEditText;
    private TextInputEditText minPriceEditText;
    private TextInputEditText maxPriceEditText;
    private TextInputEditText minSurfaceEditText;
    private TextInputEditText maxSurfaceEditText;
    private TextInputEditText minRoomNumberEditText;
    private TextInputEditText maxRoomNumberEditText;
    private TextInputEditText locationEditText;
    private final String BUNDLE_KEY = "search_properties";

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false);
        typeEditText = mBinding.etSearchType;
        minPriceEditText = mBinding.etSearchPriceFrom;
        maxPriceEditText = mBinding.etSearchPriceTo;
        minSurfaceEditText = mBinding.etSearchSurfaceFrom;
        maxSurfaceEditText = mBinding.etSearchSurfaceTo;
        minRoomNumberEditText = mBinding.etSearchRoomNumberFrom;
        maxRoomNumberEditText = mBinding.etSearchRoomNumberTo;
        locationEditText = mBinding.etSearchLocation;
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
        configureViewModel();
        getDataFromForm();
        launchResearch();
    }

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> showDialogToConfirmCancel());
    }

    private void configureViewModel() {
        mSearchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
    }

    private void getDataFromForm() {
        getDataFromEditText(typeEditText);
        getDataFromEditText(minPriceEditText);
        getDataFromEditText(maxPriceEditText);
        getDataFromEditText(minSurfaceEditText);
        getDataFromEditText(maxSurfaceEditText);
        getDataFromEditText(minRoomNumberEditText);
        getDataFromEditText(maxRoomNumberEditText);
        getDataFromEditText(locationEditText);
        getPropertySale();
    }

    private void getDataFromEditText(TextInputEditText textInputEditText){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s==typeEditText.getEditableText()){
                    type = textInputEditText.getText().toString();
                }
                else if(s==minPriceEditText.getEditableText()){
                    minPrice = textInputEditText.getText().toString();
                }
                else if(s==maxPriceEditText.getEditableText()){
                    maxPrice = textInputEditText.getText().toString();
                }
                else if(s==minSurfaceEditText.getEditableText()){
                    minSurface = textInputEditText.getText().toString();
                }
                else if(s==maxSurfaceEditText.getEditableText()) {
                    maxSurface = textInputEditText.getText().toString();
                }
                else if(s==minRoomNumberEditText.getEditableText()){
                    minRoomNumber = Integer.parseInt(textInputEditText.getText().toString());
                }
                else if(s==maxRoomNumberEditText.getEditableText()){
                    maxRoomNumber = Integer.parseInt(textInputEditText.getText().toString());
                }
                else if(s==locationEditText.getEditableText()){
                    location = textInputEditText.getText().toString();
                }
            }
        });
    }


    private void getPropertySale() {
        mBinding.switchSearchSold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    onSale = false;
                }
                else {
                    onSale = true;
                }
            }
        });
    }

    private void checkDataValuesBeforeSearch() {

    }

    private void launchResearch() {
        mBinding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearchPropertiesFromDao();
            }
        });
    }

    private void getSearchPropertiesFromDao() {
        Log.e("type before query", type);
        mSearchViewModel.getSearchProperties(type, minPrice, maxPrice, minSurface, maxSurface, minRoomNumber, maxRoomNumber, location, onSale).observe(requireActivity(), this::getSearchPropertiesForListViewFragment);
    }

    private void getSearchPropertiesForListViewFragment(List<Property> properties) {
        List<String> propertyIdStringList = new ArrayList<>();
        for(Property property : properties) {
            Log.e("properties", properties.toString());
            propertyIdStringList.add(String.valueOf(property.getId()));
        }
        Log.e("propertiesString", propertyIdStringList.toString());
        navigateToMainActivityWithSearchProperties(propertyIdStringList);
    }

    //Dialog to alert about the research annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_research)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> {
                    navigateToMainActivity();
                })
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void navigateToMainActivityWithSearchProperties(List<String> propertyIdStrings) {
        final Bundle bundle = new Bundle();
        bundle.putStringArrayList(BUNDLE_KEY, (ArrayList<String>) propertyIdStrings);
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        /**if (mListViewFragment == null) {
            mListViewFragment = ListViewFragment.newInstance();
            mListViewFragment.setArguments(bundle);
        }
        if (!mListViewFragment.isVisible()) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mListViewFragment).commit();
        }*/
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
