package com.openclassrooms.realestatemanager.ui.addedit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentGeneralDataBinding;
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
        initUi();
        configureViewModel();
        getPropertyType();
        getPropertyPrice();
        getPropertySurface();
        getPropertyAddress();
        getPropertyCity();
        getPropertyPhotos();
        goToNextFragmentAddEdit();
    }

    private void initUi() {
        configureToolbar();
        configureBottomNav();
        mBinding.btNextAddEdit.setEnabled(false);
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

    private void getPropertyType() {
        mBinding.etType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonNext();
            }
        });
    }

    private void getPropertyPrice() {
        mBinding.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonNext();
            }
        });
    }

    private void getPropertySurface() {
        mBinding.etSurface.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonNext();
            }
        });
    }

    private void getPropertyAddress() {
        mBinding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonNext();
            }
        });
    }

    private void getPropertyCity() {
        mBinding.etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonNext();
            }
        });
    }

    private void getPropertyPhotos() {
    }

    //The button next is enabled only when all fields required are filled
    private void enableButtonNext() {
        if (Objects.requireNonNull(mBinding.etType.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etPrice.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etCity.getText()).toString().isEmpty()) {
            mBinding.btNextAddEdit.setEnabled(false);
        } else {
            mBinding.btNextAddEdit.setEnabled(true);
        }
    }

    //Dialog to alert about the add/edit annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_add_edit_title)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> {
                    mNavController.navigate(R.id.action_addEditGeneralFragment_to_listViewFragment);
                })
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void goToNextFragmentAddEdit() {
        mBinding.btNextAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavController.navigate(R.id.action_addEditGeneralFragment_to_addEditDetailedFragment);
            }
        });
    }
}
