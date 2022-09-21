package com.openclassrooms.realestatemanager.ui.addedit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentGeneralDataBinding;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.AddEditDetailedViewModel;
import com.openclassrooms.realestatemanager.viewmodels.AddEditGeneralViewModel;

import java.util.List;
import java.util.Objects;

/**
*Fragment to add or edit general information about property
*/

public class AddEditGeneralFragment extends Fragment {

    //For ui
    private FragmentGeneralDataBinding mBinding;

    //For navigation
    private NavController mNavController;

    //For data
    private AddEditDetailedFragment mAddEditDetailedFragment;
    private AddEditGeneralViewModel mAddEditGeneralViewModel;
    private String type;
    private final String TYPE = "type";
    private String price;
    private final String PRICE = "price";
    private String surface = null;
    private final String SURFACE = "surface";
    private String address = null;
    private final String ADDRESS = "address";
    private String city;
    private final String CITY = "city";
    private TextInputEditText typeEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText surfaceEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText cityEditText;
    private final String ID = "id";
    private int mPropertyId;
    private Property mProperty;
    private List<Property> mProperties;

    public static AddEditGeneralFragment newInstance() {
        return new AddEditGeneralFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentGeneralDataBinding.inflate(inflater, container, false);
        typeEditText = mBinding.etType;
        priceEditText = mBinding.etPrice;
        surfaceEditText = mBinding.etSurface;
        addressEditText = mBinding.etAddress;
        cityEditText = mBinding.etCity;
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //this.mNavController = Navigation.findNavController(view);
        initUi();
        configureViewModel();
        checkIfPropertyAlreadyExists();
        getDataFromForm();
        getPropertyPhotos();
        navigateToNextFragmentAddEdit();
    }

    private void initUi() {
        configureToolbar();
        configureBottomNav();
        mBinding.btNextAddEdit.setEnabled(false);
    }

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.add_edit_general_title));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> showDialogToConfirmCancel());
    }

    private void configureBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    //TODO attacher le VM au fragment
    private void configureViewModel() {
        mAddEditGeneralViewModel = new ViewModelProvider(requireActivity()).get(AddEditGeneralViewModel.class);
    }

    private void checkIfPropertyAlreadyExists() {
        if(getArguments()!=null) {
            mPropertyId = getArguments().getInt(ID);
            observeProperties();
        }
    }

    private void observeProperties(){
        Log.e("", "observeProperties");
        mAddEditGeneralViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        Log.e("", propertiesList.toString());
        mProperties = propertiesList;
        getProperty();
    }

    private void getProperty() {
        for(Property property : mProperties) {
            if(property.getId()==mPropertyId){
                mProperty = property;
            }
        }
        getPropertyData();
    }

    private void getPropertyData() {
        type = mProperty.getType();
        price = mProperty.getPrice();
        surface = mProperty.getSurface();
        address = mProperty.getAddress();
        city = mProperty.getCity();
        fillFormWithPropertyData();
    }

    private void fillFormWithPropertyData() {
        if(type!=null){
            typeEditText.setText(type);
        }
        if(price!=null){
            priceEditText.setText(price);
        }
        if(surface!=null){
            surfaceEditText.setText(surface);
        }
        if(address!=null){
            addressEditText.setText(address);
        }
        if(city!=null){
            cityEditText.setText(city);
        }
    }

    private void getDataFromForm() {
        getDataFromEditText(typeEditText);
        getDataFromEditText(priceEditText);
        getDataFromEditText(surfaceEditText);
        getDataFromEditText(addressEditText);
        getDataFromEditText(cityEditText);
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
                else if(s==priceEditText.getEditableText()){
                    price = textInputEditText.getText().toString();
                }
                else if(s==surfaceEditText.getEditableText()) {
                    surface = textInputEditText.getText().toString();
                }
                else if(s==addressEditText.getEditableText()) {
                    address = textInputEditText.getText().toString();
                }
                else if(s==cityEditText.getEditableText()) {
                    city = textInputEditText.getText().toString();
                }
                enableButtonNext();
            }
        });
    }

    private void getPropertyPhotos() {
    }

    //The button next is enabled only when all fields required are filled
    private void enableButtonNext() {
        if (Objects.requireNonNull(mBinding.etType.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etPrice.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etCity.getText()).toString().isEmpty()) {
            mBinding.btNextAddEdit.setEnabled(false);
        } else {
            mBinding.btNextAddEdit.setEnabled(true);
        }
    }

    //Dialog to alert about the add/edit annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_add_edit_title)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> {
                    navigateToMainActivity();
                })
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void navigateToNextFragmentAddEdit() {
        mBinding.btNextAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putString(TYPE, type);
                bundle.putString(PRICE, price);
                bundle.putString(SURFACE, surface);
                bundle.putString(ADDRESS, address);
                bundle.putString(CITY, city);
                if (mAddEditDetailedFragment == null) {
                    mAddEditDetailedFragment = AddEditDetailedFragment.newInstance();
                    mAddEditDetailedFragment.setArguments(bundle);
                }
                if (!mAddEditDetailedFragment.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditDetailedFragment).commit();
                }
                //mNavController.navigate(R.id.action_addEditGeneralFragment_to_addEditDetailedFragment, bundle);
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
