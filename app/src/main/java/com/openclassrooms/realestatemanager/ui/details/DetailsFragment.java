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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.DetailsViewModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
*Fragment to display a property details
*/

public class DetailsFragment extends Fragment {

    //For ui
    private FragmentDetailsBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private long mPropertyId;
    private static final String ID = "id";
    private Property mProperty;
    private DetailsViewModel mDetailsViewModel;
    private List<Property> mProperties;
    private AddEditGeneralFragment mAddEditGeneralFragment;
    private List<Photo> mPhotos = new ArrayList<>();

    public static DetailsFragment newInstance(long propertyId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID, propertyId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            mPropertyId = getArguments().getLong(ID);
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
        initRecyclerView();
        configureViewModel();
        observeProperties();
        editProperty();
    }

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

    private void initRecyclerView() {
        mRecyclerView = mBinding.rvPhotos;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setAdapter(new PhotosAdapter(mPhotos));
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
        mBinding.tvTypeDetails.setText(mProperty.getType());
        mBinding.tvDescriptionDetail.setText(mProperty.getDescription());
        mBinding.tvPriceDetails.setText(String.format("$%s", mProperty.getPrice()));
        mBinding.tvSurfaceDetail.setText(MessageFormat.format("{0}{1}", mProperty.getSurface(), getString(R.string.square_m)));
        mBinding.tvRoomDetail.setText(String.valueOf(mProperty.getRoomNumber()));
        mBinding.tvAddressDetail.setText(mProperty.getAddress());
        mBinding.tvCityDetail.setText(mProperty.getCity());
        mBinding.tvEntryDateDetail.setText(mProperty.getEntryDate());
        mBinding.tvAgentDetail.setText(mProperty.getAgent());
        boolean isOnSale = mProperty.isOnSale();
        if(!isOnSale) {
            mBinding.tvSaleDetail.setText(R.string.no);
            mBinding.tvSoldDateDetail.setText(mProperty.getSoldDate());
        }
        else{
            mBinding.tvSaleDetail.setText(R.string.yes);
        }
    }

    private void editProperty() {
        mBinding.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putLong(ID, mProperty.getId());
                if (mAddEditGeneralFragment == null) {
                    mAddEditGeneralFragment = AddEditGeneralFragment.newInstance();
                    mAddEditGeneralFragment.setArguments(bundle);
                }
                if (!mAddEditGeneralFragment.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditGeneralFragment).commit();
                }
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
