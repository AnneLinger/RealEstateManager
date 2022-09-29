package com.openclassrooms.realestatemanager.ui.details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.openclassrooms.realestatemanager.domain.models.nearbysearchpojo.Result;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.GeocoderUtils;
import com.openclassrooms.realestatemanager.viewmodels.DetailsViewModel;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment to display a property details
 */

@RequiresApi(api = Build.VERSION_CODES.M)
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public class DetailsFragment extends Fragment {

    //For ui
    private FragmentDetailsBinding mBinding;

    //For data
    private long mPropertyId;
    private static final String ID = "id";
    private Property mProperty;
    private DetailsViewModel mDetailsViewModel;
    private List<Property> mProperties;
    private AddEditGeneralFragment mAddEditGeneralFragment;
    private final List<Photo> mPhotos = new ArrayList<>();
    private double mLatitude;
    private double mLongitude;
    private final List<String> mSchools = new ArrayList();
    private final List<String> mSupermarkets = new ArrayList();
    private final List<String> mParks = new ArrayList();

    //Empty new instance for display on tablet in landscape mode if none property is selected
    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentDetailsBinding.inflate(inflater, container, false);
        //noinspection deprecation
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
        configureBottomNav();
        configureViewModel();
        if (getArguments() != null) {
            mPropertyId = getArguments().getLong(ID);
            observeProperties();
        }
        editProperty();
    }

    private void configureToolbar() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.property_details_title));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void configureBottomNav() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void configureViewModel() {
        mDetailsViewModel = new ViewModelProvider(requireActivity()).get(DetailsViewModel.class);
    }

    private void observeProperties() {
        mDetailsViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        mProperties = propertiesList;
        getProperty();
    }

    private void getProperty() {
        for (Property property : mProperties) {
            if (property.getId() == mPropertyId) {
                mProperty = property;
            }
        }
        getPropertyData();
        observePropertyPhotos();
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
        if (!isOnSale) {
            mBinding.tvSaleDetail.setText(R.string.no);
            mBinding.tvSoldDateDetail.setText(mProperty.getSoldDate());
        } else {
            mBinding.tvSaleDetail.setText(R.string.yes);
        }
        requestToGetBitmapForMap();
        requestPointsOfInterest();
    }

    private void observePropertyPhotos() {
        mDetailsViewModel.getPropertyPhotos(mPropertyId).observe(requireActivity(), this::getPropertyPhotos);
    }

    private void getPropertyPhotos(List<Photo> photos) {
        if (!photos.isEmpty()) {
            mPhotos.addAll(photos);
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = mBinding.rvPhotos;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(new PhotosAdapter(mPhotos));
    }

    private void requestToGetBitmapForMap() {
        mLatitude = GeocoderUtils.getLatitudeFromAddress(mProperty.getAddress(), requireContext());
        mLongitude = GeocoderUtils.getLongitudeFromAddress(mProperty.getAddress(), requireContext());
        mDetailsViewModel.getBitmapFromApi(mLatitude, mLongitude);
        observeBitmapReturn();
    }

    private void observeBitmapReturn() {
        mDetailsViewModel.getBitmapLiveData().observe(this, this::updateMapWithBitmap);
    }

    private void updateMapWithBitmap(Bitmap bitmap) {
        mBinding.imMapDetails.setImageBitmap(bitmap);
    }

    private void requestPointsOfInterest() {
        String propertyLocationString = mLatitude + "," + mLongitude;
        mDetailsViewModel.fetchNearbySearchSchools(propertyLocationString);
        mDetailsViewModel.fetchNearbySearchSupermarkets(propertyLocationString);
        mDetailsViewModel.fetchNearbySearchParks(propertyLocationString);
        observeSchools();
        observeSupermarkets();
        observeParks();
    }

    private void observeSchools() {
        mDetailsViewModel.getNearbySearchSchoolsResponseLiveData().observe(this, this::updatePointsOfInterestWithSchools);
    }

    private void updatePointsOfInterestWithSchools(List<Result> results) {
        for (Result result : results) {
            mSchools.add(result.getName());
        }
        updateTextViewWithPointsOfInterest();
    }

    private void observeSupermarkets() {
        mDetailsViewModel.getNearbySearchSupermarketsResponseLiveData().observe(this, this::updatePointsOfInterestWithSupermarkets);
    }

    private void updatePointsOfInterestWithSupermarkets(List<Result> results) {
        for (Result result : results) {
            mSupermarkets.add(result.getName());
        }
        updateTextViewWithPointsOfInterest();
    }

    private void observeParks() {
        mDetailsViewModel.getNearbySearchParksResponseLiveData().observe(this, this::updatePointsOfInterestWithParks);
    }

    private void updatePointsOfInterestWithParks(List<Result> results) {
        for (Result result : results) {
            mParks.add(result.getName());
        }
        updateTextViewWithPointsOfInterest();
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private void updateTextViewWithPointsOfInterest() {
        List<String> pointsOfInterestList = new ArrayList<>();
        pointsOfInterestList.addAll(mSchools);
        pointsOfInterestList.addAll(mSupermarkets);
        pointsOfInterestList.addAll(mParks);
        String schools = getString(R.string.schools) + Arrays.toString(mSchools.toArray()).replace("[", "").replace("]", "");
        String supermarkets = getString(R.string.shops) + Arrays.toString(mSupermarkets.toArray()).replace("[", "").replace("]", "");
        String parks = getString(R.string.parks) + Arrays.toString(mParks.toArray()).replace("[", "").replace("]", "");
        mBinding.tvPointsInterestDetailSchools.setText(schools);
        mBinding.tvPointsInterestDetailSupermarkets.setText(supermarkets);
        mBinding.tvPointsInterestDetailParks.setText(parks);
    }

    private void editProperty() {
        mBinding.fabEdit.setOnClickListener(v -> {
            final Bundle bundle = new Bundle();
            bundle.putLong(ID, mProperty.getId());
            if (mAddEditGeneralFragment == null) {
                mAddEditGeneralFragment = AddEditGeneralFragment.newInstance();
                mAddEditGeneralFragment.setArguments(bundle);
            }
            if (!mAddEditGeneralFragment.isVisible()) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditGeneralFragment).commit();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
