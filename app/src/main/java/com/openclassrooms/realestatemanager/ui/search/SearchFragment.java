package com.openclassrooms.realestatemanager.ui.search;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
*Activity to make researches on properties
*/

public class SearchFragment extends Fragment {

    //For ui
    private FragmentSearchBinding mBinding;

    //For data
    private static final DateUtils mDateTimeUtils = new DateUtils();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private Date date;
    private String type;
    private String minPrice;
    private String maxPrice;
    private int minRoomNumber;
    private int maxRoomNumber;
    private String minSurface;
    private String maxSurface;
    private String entranceDate;
    private String Location;
    private boolean onSale = true;
    private String soldDate;
    private TextInputEditText typeEditText;
    private TextInputEditText minPriceEditText;
    private TextInputEditText maxPriceEditText;
    private TextInputEditText minSurfaceEditText;
    private TextInputEditText maxSurfaceEditText;
    private TextInputEditText minRoomNumberEditText;
    private TextInputEditText maxRoomNumberEditText;
    private TextInputEditText entranceFromEditText;
    private TextInputEditText locationEditText;
    private TextInputEditText soldSinceEditText;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSearchBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureToolbar();
        getDataFromForm();
        launchResearch();
    }

    private void configureToolbar() {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> showDialogToConfirmCancel());
    }

    private void getDataFromForm() {
        getCurrentDate();
        selectEntryDateFromDatePicker();
        getPropertySale();
        selectSoldDateFromDatePicker();
        getDataFromEditText(typeEditText);
        getDataFromEditText(minPriceEditText);
        getDataFromEditText(maxPriceEditText);
        getDataFromEditText(minSurfaceEditText);
        getDataFromEditText(maxSurfaceEditText);
        getDataFromEditText(minRoomNumberEditText);
        getDataFromEditText(maxRoomNumberEditText);
        getDataFromEditText(entranceFromEditText);
        getDataFromEditText(locationEditText);
        getDataFromEditText(soldSinceEditText);
    }

    private void getDataFromEditText(TextInputEditText textInputEditText){
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s==typeEditText.getEditableText()){
                    type = textInputEditText.getText().toString();
                }
                else if(s==minPriceEditText.getEditableText()){
                    minPrice = textInputEditText.getText().toString();
                }
                else if(s==maxPriceEditText.getEditableText()){
                    maxPrice = textInputEditText.getText().toString();
                }
                else if(s==minSurfaceEditText.getEditableText()){
                    minSurface = textInputEditText.getText().toString();
                }
                else if(s==maxSurfaceEditText.getEditableText()) {
                    maxSurface = textInputEditText.getText().toString();
                }
                if(s==minRoomNumberEditText.getEditableText()){
                    minRoomNumber = Integer.parseInt(textInputEditText.getText().toString());
                }
                if(s==maxRoomNumberEditText.getEditableText()){
                    maxRoomNumber = Integer.parseInt(textInputEditText.getText().toString());
                }
                else if(s==entranceFromEditText.getEditableText()) {
                    entranceDate = textInputEditText.getText().toString();
                }
                else if(s==soldSinceEditText.getEditableText()) {
                    soldDate = textInputEditText.getText().toString();
                }
            }
        });
    }

    private void selectEntryDateFromDatePicker() {
        mBinding.tfSearchEntryDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog(mBinding.etSearchEntryDate);
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
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

    private void getPropertySale() {
        mBinding.switchSearchSold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mBinding.etSearchSoldDate.setEnabled(true);
                    mBinding.tfSearchSoldDate.setEnabled(true);
                    onSale = false;
                }
                else {
                    mBinding.etSearchSoldDate.setEnabled(false);
                    mBinding.tfSearchSoldDate.setEnabled(false);
                    onSale = true;
                }
            }
        });
    }

    private void selectSoldDateFromDatePicker() {
        mBinding.tfSearchSoldDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog(mBinding.etSearchSoldDate);
            }
        });
    }

    private void launchResearch() {
        mBinding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<CharSequence>  propertyIdStringList = new ArrayList<>();
                navigateToMainActivity(propertyIdStringList);
            }
        });
    }

    //Dialog to alert about the research annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_research)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> {
                    navigateToMainActivity();
                })
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void navigateToMainActivity(ArrayList<CharSequence> propertyIdStrings) {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.putCharSequenceArrayListExtra("propertiesId", propertyIdStrings);
        startActivity(intent);
    }
}
