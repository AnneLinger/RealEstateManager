package com.openclassrooms.realestatemanager.ui.mapview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapViewBinding;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditDetailedFragment;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.MapViewModel;

import java.util.List;
import java.util.Map;

/**
*Created by Anne Linger on 14/09/2022.
*/

public class MapViewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    //For UI
    private FragmentMapViewBinding mBinding;
    private GoogleMap mGoogleMap;
    private float zoom = 12;

    //For data
    private MapViewModel mMapViewModel;
    private Location mLocation;
    private String mLocationString;
    private DetailsFragment mDetailsFragment;

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
        requestLocationPermission();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //TODO updateMap ! ! !
    }

    private void configureViewModels() {
        mMapViewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);
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
        mMapViewModel.getLivedataLocation().observe(requireActivity(), this::updateMapLocation);
    }

    //Update map with user location
    private void updateMapLocation(Location location) {
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
        //updateMapWithData();
    }

   /** private void updateMapWithData() {
        mGoogleMap.clear();
        for (Result mResult : results) {
            LatLng placeLatLng = new LatLng(mResult.getGeometry().getLocation().getLat(), mResult.getGeometry().getLocation().getLng());
            addMarker(R.drawable.icon2073973_1920restaurant, placeLatLng, mResult);
            for (Booking booking : mBookingList) {
                if (booking.getPlaceId().equalsIgnoreCase(mResult.getPlaceId())) {
                    addMarker(R.drawable.icon_restaurant_green, placeLatLng, mResult);
                }
            }
        }
        mGoogleMap.setOnInfoWindowClickListener(marker -> navigateToDetailsFragment(mProperty.getId));
    }

    private void addMarker(int drawable, LatLng placeLatLng, Result result) {
        mGoogleMap.addMarker(new MarkerOptions()
                .position(placeLatLng)
                .title(result.getName())
                .icon(BitmapDescriptorFactory.fromBitmap(setUpMarkerIcon(drawable))));
    }

    private Bitmap setUpMarkerIcon(int drawable) {
        Bitmap markerBitmap = BitmapFactory.decodeResource(requireContext().getResources(), drawable);
        return Bitmap.createScaledBitmap(markerBitmap, 80, 120, false);
    }*/

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

    private void navigateToDetailsFragment(long id) {
        if (mDetailsFragment == null) {
            mDetailsFragment = DetailsFragment.newInstance(id);
        }
        if (!mDetailsFragment.isVisible()) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mDetailsFragment).commit();
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
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
