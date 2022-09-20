package com.openclassrooms.realestatemanager.ui.addedit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentGeneralDataBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.viewmodels.AddViewModel;

import java.util.Objects;

/**
*Fragment to add or edit general information about property
*/

public class AddEditGeneralFragment extends Fragment {

    //For ui
    private FragmentGeneralDataBinding mBinding;
    private AddEditDetailedFragment mAddEditDetailedFragment = AddEditDetailedFragment.newInstance();

    //For navigation
    private NavController mNavController;

    //For data
    private AddViewModel mAddViewModel;

    public static AddEditGeneralFragment newInstance() {
        return new AddEditGeneralFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentGeneralDataBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mNavController = Navigation.findNavController(view);
        configureToolbar();
        configureBottomNav();
        configureViewModel();
        saveProperty();
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
        mAddViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);
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

    //TO delete only for test during dev
    private void saveProperty() {
        mBinding.btNextAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", "onClick");
                mAddViewModel.createProperty("House", 120000, 80, 5, null, "12 Main Street", "New York", true, "13 octobre 2022", null, "Bob Stuart");
                //mNavController.navigate(R.id.action_addEditGeneralFragment_to_addEditDetailedFragment);
                //navigateToMainActivity();
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
