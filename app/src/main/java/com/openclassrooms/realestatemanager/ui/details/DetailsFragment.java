package com.openclassrooms.realestatemanager.ui.details;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.ui.search.SearchFragment;
import com.openclassrooms.realestatemanager.viewmodels.DetailsViewModel;

import java.util.List;

/**
*Fragment to display a property details
*/

public class DetailsFragment extends Fragment {

    //For ui
    private FragmentDetailsBinding mBinding;

    //For navigation
    private NavController mNavController;

    //For data
    private int mPropertyId;
    private static final String ID = "id";
    private Property mProperty;
    private DetailsViewModel mDetailsViewModel;
    private List<Property> mProperties;
    private String mType;
    private AddEditGeneralFragment mAddEditGeneralFragment;

    public static DetailsFragment newInstance(int propertyId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID, propertyId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            mPropertyId = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
        configureBottomNav();
        configureViewModel();
        observeProperties();
        editProperty();
    }

    //TODO create a navigate vers fragment addEditGeneral mais redonner pour ça la main à la MainActivity ++

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.property_details_title));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void configureBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void configureViewModel() {
        mDetailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
    }

    //TODO attach vm to fragment
    private void observeProperties(){
        Log.e("", "observeProperties");
        mDetailsViewModel.getProperties().observe(requireActivity(), this::getProperties);
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
        mType = mProperty.getType();
        mBinding.test.setText(mType);
    }

    private void editProperty() {
        mBinding.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putInt(ID, mProperty.getId());
                if (mAddEditGeneralFragment == null) {
                    mAddEditGeneralFragment = AddEditGeneralFragment.newInstance();
                    mAddEditGeneralFragment.setArguments(bundle);
                }
                if (!mAddEditGeneralFragment.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditGeneralFragment).commit();
                }
                //mNavController.navigate(R.id.action_detailsFragment_to_addEditGeneralFragment);
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
