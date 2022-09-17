package com.openclassrooms.realestatemanager.ui.addedit;

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
import com.openclassrooms.realestatemanager.databinding.FragmentDetailedDataBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;

/**
*Fragment to add or edit detailed information about property
*/

public class AddEditDetailedFragment extends Fragment {

    //For ui
    private FragmentDetailedDataBinding mBinding;

    public static AddEditDetailedFragment newInstance() {
        return new AddEditDetailedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentDetailedDataBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.getItem(0).setVisible(false);
    }

    //TODO create a alert dialog to confirm cancel add/edit
    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> navigateToMainActivity());
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
