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
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentGeneralDataBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.AddViewModel;

/**
*Fragment to add or edit general information about property
*/

public class AddEditGeneralFragment extends Fragment {

    //For ui
    private FragmentGeneralDataBinding mBinding;
    private AddEditDetailedFragment mAddEditDetailedFragment = AddEditDetailedFragment.newInstance();

    //For data
    private AddViewModel mAddViewModel;

    public static AddEditGeneralFragment newInstance() {
        return new AddEditGeneralFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentGeneralDataBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
        configureViewModel();
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

    //TODO attacher le VM au fragment
    private void configureViewModel() {
        mAddViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);
    }

    private void saveProperty() {
        mBinding.fabSaveProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddViewModel.createProperty("House", 120000, 80, 5, null, "12 avenue de la République, 35000 Rennes", true, "13 octobre 2022", null, "Bob Stuart");
                navigateToMainActivity();
            }
        });
    }

    //TODO Like in the adapter, renvoi  de la nav vers la main activity pour aller vers fagment addEditDetailed
    private void navigateToAddEditFragment() {
        /**if (!mAddEditDetailedFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_places_frame_layout, mAddEditDetailedFragment).commit();
        }
        mBinding.bottomNav.setVisibility(View.GONE);
        mBinding.mainActivityLayout.closeDrawers();
        mBinding.toolbar.setTitle(this.getString(R.string.add_edit_general_title));
        mBinding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);*/
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
