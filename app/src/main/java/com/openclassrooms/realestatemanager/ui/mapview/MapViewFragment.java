package com.openclassrooms.realestatemanager.ui.mapview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapViewBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.GeocoderUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewmodels.MapViewModel;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
*Created by Anne Linger on 14/09/2022.
*/

public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    //For UI
    private FragmentMapViewBinding mBinding;
    private GoogleMap mGoogleMap;
    private float zoom = 11;

    //For data
    private MapViewModel mMapViewModel;
    private Location mLocation;
    private String mLocationString;
    private final LatLng FAKE_AGENT_LOCATION = new LatLng(40.655675, -74.210345);
    private DetailsFragment mDetailsFragment;
    private List<Property> mProperties;

    //For location permission result
    private static final String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private final ActivityResultLauncher<String[]> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
        boolean granted = true;
        for (Map.Entry<String, Boolean> x : isGranted.entrySet())
            if (!x.getValue()) granted = false;
        if (granted) getUserLocation();
        else {
            showDialogToDenyAccessMap();
        }
    });

    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(inflater, container, false);
        configureViewModels();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        checkIfUserHasInternet();
    }

    private void configureViewModels() {
        mMapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
    }

    //Check internet
    private void checkIfUserHasInternet() {
        if(Utils.isUserHasInternet(requireContext())) {
            requestLocationPermission();
        }
        else {
            mBinding.fragmentMapView.setVisibility(View.INVISIBLE);
            showDialogToInformAboutInternetAvailability();
        }
    }

    //Dialog to alert about no internet available
    public void showDialogToInformAboutInternetAvailability() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.no_internet_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                })
                .setMessage(R.string.no_internet_text)
                .setCancelable(false)
                .create()
                .show();
    }

    //Check the location permission
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Permission ok => display map
            getUserLocation();
        } else {
            //Permission not ok => ask permission to the user for location
            mActivityResultLauncher.launch(perms);
        }
    }

    //Get the user location when permission is ok
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        mMapViewModel.getUserLocation(this.getContext());
        mGoogleMap.setMyLocationEnabled(true);
        mMapViewModel.getLivedataLocation().observe(requireActivity(), this::updateMapWithUserLocation);
    }

    //Update map with user location
    private void updateMapWithUserLocation(Location location) {
        mLocation = location;
        mLocationString = mLocation.getLatitude() + "," + mLocation.getLongitude();
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(requireActivity().getString(R.string.my_position)));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoom));
        }
        observeProperties();
    }

    private void observeProperties(){
        mMapViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        mProperties = propertiesList;
        updateMapWithProperties();
    }

    private void updateMapWithProperties() {
        for(Property property : mProperties) {
            double latitude = GeocoderUtils.getLatitudeFromAddress(property.getAddress(), requireContext());
            double longitude = GeocoderUtils.getLongitudeFromAddress(property.getAddress(), requireContext());
            LatLng latLng = new LatLng(latitude, longitude);
            addMarker(latLng, property);
        }
        //To move camera on fake agent location in New York
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FAKE_AGENT_LOCATION, zoom));
        mGoogleMap.setOnInfoWindowClickListener(marker -> navigateToDetailsFragment(mProperties, marker));
    }

    private void addMarker(LatLng placeLatLng, Property property) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(placeLatLng)
                .title(property.getType() + property.getId()));
    }

    //Dialog to alert about essential permission
    public void showDialogToDenyAccessMap() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext(), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.permissions_required)
                .setMessage(R.string.photo_permission_text)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_add_edit_title, (dialog, which) -> {
                    navigateToMainActivity();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void navigateToDetailsFragment(List<Property> properties, Marker marker) {
        for (Property property : properties) {
            if (Objects.requireNonNull(marker.getTitle()).contains(String.valueOf(property.getId()))) {
                if (mDetailsFragment == null) {
                    mDetailsFragment = DetailsFragment.newInstance(property.getId());
                }
                if (!mDetailsFragment.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mDetailsFragment).commit();
                }
            }
        }

    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        getUserLocation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMapViewModel.stopLocationRequest();
        }
    }
}
