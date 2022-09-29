package com.openclassrooms.realestatemanager.ui.addedit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailedDataBinding;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.DateUtils;
import com.openclassrooms.realestatemanager.viewmodels.AddEditDetailedViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Fragment to add or edit detailed information about property
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddEditDetailedFragment extends Fragment {

    //For ui
    private FragmentDetailedDataBinding mBinding;

    //For data
    private AddEditDetailedViewModel mAddEditDetailedViewModel;
    private AddEditPhotoFragment mAddEditPhotoFragment;
    private static final DateUtils mDateTimeUtils = new DateUtils();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private Date date;
    private String type;
    private String price;
    private String surface;
    private String address;
    private String city;
    private int roomNumber = 0;
    private String description = null;
    private String entryDate;
    private String agent;
    private String soldDate = null;
    private boolean onSale = true;
    private final String photoKey = "1";
    private TextInputEditText roomNumberEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText entryDateEditText;
    private TextInputEditText agentEditText;
    private TextInputEditText soldDateEditText;
    private final String ID = "id";
    private long mPropertyId;
    private Property mProperty;
    private List<Property> mProperties = new ArrayList<>();

    public static AddEditDetailedFragment newInstance() {
        return new AddEditDetailedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentDetailedDataBinding.inflate(inflater, container, false);
        roomNumberEditText = mBinding.etRoomNumber;
        descriptionEditText = mBinding.etDescription;
        entryDateEditText = mBinding.etEntryDate;
        agentEditText = mBinding.etAgent;
        soldDateEditText = mBinding.etSoldDate;
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        configureViewModel();
        observeProperties();
        checkIfPropertyAlreadyExists();
        getDataFromPreviousForm();
        getDataFromForm();
        saveProperty();
    }

    private void configureToolbar() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(this.getString(R.string.add_edit_detailed_title));
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> showDialogToConfirmCancel());
    }

    private void configureBottomNav() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void initUi() {
        configureToolbar();
        configureBottomNav();
        mBinding.btSave.setEnabled(false);
        mBinding.etSoldDate.setEnabled(false);
        mBinding.tfSoldDate.setEnabled(false);
    }

    private void configureViewModel() {
        mAddEditDetailedViewModel = new ViewModelProvider(requireActivity()).get(AddEditDetailedViewModel.class);
    }

    private void checkIfPropertyAlreadyExists() {
        assert getArguments() != null;
        if (!(getArguments().getLong(ID) == 0)) {
            mPropertyId = getArguments().getLong(ID);
            observeProperties();
        }
    }

    private void observeProperties() {
        mAddEditDetailedViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        mProperties = propertiesList;
        if (mProperties.size() > 0) {
            getProperty();
        }
    }

    private void getProperty() {
        for (Property property : mProperties) {
            if (property.getId() == mPropertyId) {
                mProperty = property;
                getPropertyData();
            }
        }
    }

    private void getPropertyData() {
        roomNumber = mProperty.getRoomNumber();
        description = mProperty.getDescription();
        entryDate = mProperty.getEntryDate();
        agent = mProperty.getAgent();
        onSale = mProperty.isOnSale();
        if (!onSale) {
            soldDate = mProperty.getSoldDate();
        }
        fillFormWithPropertyData();
    }

    private void fillFormWithPropertyData() {
        if (!(roomNumber == 0)) {
            roomNumberEditText.setText(String.valueOf(roomNumber));
        }
        if (description != null) {
            descriptionEditText.setText(description);
        }
        if (entryDate != null) {
            entryDateEditText.setText(entryDate);
        }
        if (agent != null) {
            agentEditText.setText(agent);
        }
        if (!onSale) {
            mBinding.switchSold.setChecked(true);
            soldDateEditText.setText(soldDate);
        }
    }

    private void getDataFromPreviousForm() {
        assert getArguments() != null;
        String TYPE = "type";
        type = getArguments().getString(TYPE);
        String PRICE = "price";
        price = getArguments().getString(PRICE);
        String SURFACE = "surface";
        surface = getArguments().getString(SURFACE);
        String ADDRESS = "address";
        address = getArguments().getString(ADDRESS);
        String CITY = "city";
        city = getArguments().getString(CITY);
    }

    private void getDataFromForm() {
        getCurrentDate();
        selectEntryDateFromDatePicker();
        getPropertySale();
        selectSoldDateFromDatePicker();
        getDataFromEditText(roomNumberEditText);
        getDataFromEditText(descriptionEditText);
        getDataFromEditText(entryDateEditText);
        getDataFromEditText(agentEditText);
        getDataFromEditText(soldDateEditText);
    }

    private void getDataFromEditText(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == roomNumberEditText.getEditableText()) {
                    roomNumber = Integer.parseInt(Objects.requireNonNull(textInputEditText.getText()).toString());
                } else if (s == descriptionEditText.getEditableText()) {
                    description = Objects.requireNonNull(textInputEditText.getText()).toString();
                } else if (s == entryDateEditText.getEditableText()) {
                    entryDate = Objects.requireNonNull(textInputEditText.getText()).toString();
                } else if (s == agentEditText.getEditableText()) {
                    agent = Objects.requireNonNull(textInputEditText.getText()).toString();
                } else if (s == soldDateEditText.getEditableText()) {
                    soldDate = Objects.requireNonNull(textInputEditText.getText()).toString();
                }
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

    private void configureDatePickerDialog(EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
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
        };
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(requireActivity(), dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);
        datePickerDialog.show();
    }

    private void selectEntryDateFromDatePicker() {
        mBinding.tfEntryDate.setStartIconOnClickListener(view -> configureDatePickerDialog(mBinding.etEntryDate));
    }

    private void getPropertySale() {
        mBinding.switchSold.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.etSoldDate.setEnabled(true);
                mBinding.tfSoldDate.setEnabled(true);
                onSale = false;
            } else {
                mBinding.etSoldDate.setEnabled(false);
                mBinding.tfSoldDate.setEnabled(false);
                onSale = true;
            }
            enableButtonSave();
        });
    }

    private void selectSoldDateFromDatePicker() {
        mBinding.tfSoldDate.setStartIconOnClickListener(view -> configureDatePickerDialog(mBinding.etSoldDate));
    }

    //The button save is enabled only when all fields required are filled
    private void enableButtonSave() {
        if (Objects.requireNonNull(mBinding.etRoomNumber.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etEntryDate.getText()).toString().isEmpty() ||
                Objects.requireNonNull(mBinding.etAgent.getText()).toString().isEmpty()) {
            mBinding.btSave.setEnabled(false);
        }
        if (!onSale)
            mBinding.btSave.setEnabled(!Objects.requireNonNull(mBinding.etSoldDate.getText()).toString().isEmpty());
        else {
            mBinding.btSave.setEnabled(true);
        }
    }

    //Dialog to alert about the add/edit annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_add_edit_title)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> navigateToMainActivity())
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void saveProperty() {
        mBinding.btSave.setOnClickListener(v -> {
            if (mPropertyId == 0) {
                Property newProperty = new Property(type, price, surface, roomNumber, description, address, city, onSale, entryDate, soldDate, agent, photoKey);
                mAddEditDetailedViewModel.createProperty(newProperty);
                mPropertyId = mProperties.size() + 1;
            } else {
                editProperty();
            }
            navigateToPhotosFragment();
        });
    }

    private void editProperty() {
        mProperty.setType(type);
        mProperty.setPrice(price);
        mProperty.setSurface(surface);
        mProperty.setAddress(address);
        mProperty.setCity(city);
        mProperty.setRoomNumber(roomNumber);
        mProperty.setDescription(description);
        mProperty.setEntryDate(entryDate);
        mProperty.setAgent(agent);
        mProperty.setOnSale(onSale);
        if (!onSale) {
            mProperty.setSoldDate(soldDate);
        }
        mAddEditDetailedViewModel.editProperty(mProperty);
    }

    private void navigateToPhotosFragment() {
        final Bundle bundle = new Bundle();
        bundle.putLong(ID, mPropertyId);
        if (mAddEditPhotoFragment == null) {
            mAddEditPhotoFragment = AddEditPhotoFragment.newInstance();
            mAddEditPhotoFragment.setArguments(bundle);
        }
        if (!mAddEditPhotoFragment.isVisible()) {
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditPhotoFragment).commit();
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
