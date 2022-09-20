package com.openclassrooms.realestatemanager.ui.addedit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.openclassrooms.realestatemanager.databinding.FragmentDetailedDataBinding;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.DateUtils;
import com.openclassrooms.realestatemanager.viewmodels.AddViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
*Fragment to add or edit detailed information about property
*/

public class AddEditDetailedFragment extends Fragment {

    //For ui
    private FragmentDetailedDataBinding mBinding;

    //For navigation
    private NavController mNavController;

    //For data
    private AddViewModel mAddViewModel;
    private boolean isPropertySold = false;
    private static final DateUtils mDateTimeUtils = new DateUtils();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private Date date;
    private String type;
    private final String TYPE = "type";
    private int price;
    private final String PRICE = "price";
    private String surface;
    private final String SURFACE = "surface";
    private String address;
    private final String ADDRESS = "address";
    private String city;
    private final String CITY = "city";


    public static AddEditDetailedFragment newInstance() {
        return new AddEditDetailedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentDetailedDataBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mNavController = Navigation.findNavController(view);
        initUi();
        configureViewModel();
        getDataFromPreviousForm();
        getDataFromForm();
        goToNextFragmentAddEdit();
    }

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.add_edit_detailed_title));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> showDialogToConfirmCancel());
    }

    private void configureBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void initUi() {
        configureToolbar();
        configureBottomNav();
        mBinding.btSave.setEnabled(false);
        mBinding.etSoldDate.setEnabled(false);
        mBinding.tfSoldDate.setEnabled(false);
    }

    //TODO attacher le VM au fragment
    private void configureViewModel() {
        mAddViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);
    }

    private void getDataFromPreviousForm() {
        assert getArguments() != null;
        type = getArguments().getString(TYPE);
        price = getArguments().getInt(PRICE);
        surface = getArguments().getString(SURFACE);
        address = getArguments().getString(ADDRESS);
        city = getArguments().getString(CITY);
    }

    private void getDataFromForm() {
        getPropertyRoomNumber();
        getPropertyDescription();
        getCurrentDate();
        selectEntryDateFromDatePicker();
        getPropertyEntryDate();
        getPropertyAgent();
        getPropertySale();
        selectSoldDateFromDatePicker();
        getPropertySoldDate();
    }

    private void getPropertyRoomNumber() {
        mBinding.etRoomNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonSave();
            }
        });
    }

    private void getPropertyDescription() {
        mBinding.etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonSave();
            }
        });
    }

    private void getCurrentDate() {
        final Calendar calendar = Calendar.getInstance();
        this.lastSelectedYear = calendar.get(Calendar.YEAR);
        this.lastSelectedMonth = calendar.get(Calendar.MONTH);
        this.lastSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //Configure the DatePickerDialog
    private void configureDatePickerDialog(EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date = mDateTimeUtils.getDateFromDatePicker(year, monthOfYear, dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String result = formatter.format(date);
                try {
                    date = formatter.parse(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                editText.setText(result);
                lastSelectedYear = year;
                lastSelectedMonth = monthOfYear;
                lastSelectedDay = dayOfMonth;
            }
        };
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(requireActivity(), dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);
        datePickerDialog.show();
    }

    private void selectEntryDateFromDatePicker() {
        mBinding.tfEntryDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog(mBinding.etEntryDate);
            }
        });
    }

    private void getPropertyEntryDate() {
        mBinding.etEntryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonSave();
            }
        });
    }

    private void getPropertyAgent() {
        mBinding.etAgent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonSave();
            }
        });
    }

    private void getPropertySale() {
        mBinding.switchSold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mBinding.etSoldDate.setEnabled(true);
                    mBinding.tfSoldDate.setEnabled(true);
                    isPropertySold = true;
                }
                else {
                    mBinding.etSoldDate.setEnabled(false);
                }
                enableButtonSave();
            }
        });
    }

    private void selectSoldDateFromDatePicker() {
        mBinding.tfSoldDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog(mBinding.etSoldDate);
            }
        });
    }

    private void getPropertySoldDate() {
        mBinding.etSoldDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                enableButtonSave();
            }
        });
    }

    //The button save is enabled only when all fields required are filled
    private void enableButtonSave() {
        if (Objects.requireNonNull(mBinding.etRoomNumber.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etEntryDate.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etAgent.getText()).toString().isEmpty()) {
            mBinding.btSave.setEnabled(false);
        }
        if (isPropertySold)
            mBinding.btSave.setEnabled(!Objects.requireNonNull(mBinding.etSoldDate.getText()).toString().isEmpty());
        else {
            mBinding.btSave.setEnabled(true);
        }
    }

    //TODO create an utils for that !
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

    private void goToNextFragmentAddEdit() {
        mBinding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", "onClick");
                mAddViewModel.createProperty(type, price, surface, 5, null, address, city, true, "13 octobre 2022", null, "Bob Stuart");
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        mNavController.navigate(R.id.action_addEditDetailedFragment_to_listViewFragment);
    }
}
