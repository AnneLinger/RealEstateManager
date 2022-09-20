package com.openclassrooms.realestatemanager.ui.simulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSimulatorBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;

/**
*Activity for home loan simulator
*/

public class SimulatorFragment extends Fragment {

    //For ui
    private FragmentSimulatorBinding mBinding;

    public static SimulatorFragment newInstance() {
        return new SimulatorFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSimulatorBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
    }

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
