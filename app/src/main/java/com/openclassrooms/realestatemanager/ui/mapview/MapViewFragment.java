package com.openclassrooms.realestatemanager.ui.mapview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.databinding.FragmentMapViewBinding;

/**
*Created by Anne Linger on 14/09/2022.
*/

public class MapViewFragment extends Fragment {

    //For UI
    private FragmentMapViewBinding mBinding;

    public static MapViewFragment newInstance() {
        return new MapViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentMapViewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
