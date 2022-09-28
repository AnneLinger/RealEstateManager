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
import androidx.sqlite.db.SimpleSQLiteQuery;

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
    private String minPrice;
    private String maxPrice;
    private int minRoomNumber;
    private int maxRoomNumber;
    private String minSurface;
    private String maxSurface;
    private boolean onSale = true;
    private TextInputEditText typeEditText;
    private TextInputEditText minPriceEditText;
    private TextInputEditText maxPriceEditText;
    private TextInputEditText minSurfaceEditText;
    private TextInputEditText maxSurfaceEditText;
    private TextInputEditText minRoomNumberEditText;
    private TextInputEditText maxRoomNumberEditText;
    private final String BUNDLE_KEY = "search_properties";
    private String query = "SELECT * FROM property_table";
    private List<Object> args = new ArrayList<>();

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

    private void buildQueryForSearch() {
        Log.e("Anne", "Build query");
        boolean isFirstConditionDone = false;
        if(type!=null){
            Log.e("Anne", "Add type");
            query += " WHERE type LIKE :";
            query += type;
            args.add(type);
            isFirstConditionDone = true;
        }
        if(minPrice!=null){
            if(isFirstConditionDone) {
                query += " AND price >= :";
            }
            else {
                query += " WHERE price >= :";
                isFirstConditionDone = true;
            }
            query += minPrice;
            args.add(minPrice);
        }
        if(maxPrice!=null){
            if(isFirstConditionDone) {
                query += " AND price <= :";
            }
            else {
                query += " WHERE price <= :";
                isFirstConditionDone = true;
            }
            query += maxPrice;
            args.add(maxPrice);
        }
        if(minSurface!=null){
            if(isFirstConditionDone) {
                query += " AND surface >= :";
            }
            else {
                query += " WHERE surface >= :";
                isFirstConditionDone = true;
            }
            query += minSurface;
            args.add(minSurface);
        }
        if(maxSurface!=null){
            if(isFirstConditionDone) {
                query += " AND surface <= :";
            }
            else {
                query += " WHERE surface <= :";
                isFirstConditionDone = true;
            }
            query += maxSurface;
            args.add(maxSurface);
        }
        if(minRoomNumber!=0){
            if(isFirstConditionDone) {
                query += " AND room_number >= :";
            }
            else {
                query += " WHERE room_number >= :";
                isFirstConditionDone = true;
            }
            query += minRoomNumber;
            args.add(minRoomNumber);
        }
        if(maxRoomNumber!=0){
            if(isFirstConditionDone) {
                query += " AND room_number <= :";
            }
            else {
                query += " WHERE room_number <= :";
                isFirstConditionDone = true;
            }
            query += maxRoomNumber;
            args.add(maxRoomNumber);
        }
        if(!onSale){
            if(isFirstConditionDone) {
                query += " AND on_sale = :";
            }
            else {
                query += " WHERE on_sale = :";
                isFirstConditionDone = true;
            }
            query += onSale;
            args.add(onSale);
        }
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
        buildQueryForSearch();
        Log.e("query before query", query);
        SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query, args.toArray());
        List<Property> properties = mSearchViewModel.getSearchProperties(simpleSQLiteQuery);
        getSearchPropertiesForListViewFragment(properties);
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
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
