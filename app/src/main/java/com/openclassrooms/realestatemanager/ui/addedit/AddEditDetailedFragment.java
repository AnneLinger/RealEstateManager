package com.openclassrooms.realestatemanager.ui.addedit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailedDataBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
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
*Fragment to add or edit detailed information about property
*/

public class AddEditDetailedFragment extends Fragment {

    //For ui
    private FragmentDetailedDataBinding mBinding;

    //For navigation
    private NavController mNavController;

    //For data
    private AddEditDetailedViewModel mAddEditDetailedViewModel;
    private AddEditPhotoFragment mAddEditPhotoFragment;
    private static final DateUtils mDateTimeUtils = new DateUtils();
    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private Date date;
    private String type;
    private final String TYPE = "type";
    private String price;
    private final String PRICE = "price";
    private String surface;
    private final String SURFACE = "surface";
    private String address;
    private final String ADDRESS = "address";
    private String city;
    private final String CITY = "city";
    private int photoNumber;
    private final String PHOTO_NUMBER = "photo_number";
    private int roomNumber = 0;
    private String description = null;
    private String entryDate;
    private String agent;
    private String soldDate = null;
    private boolean onSale = true;
    private String photoKey;
    private TextInputEditText roomNumberEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText entryDateEditText;
    private TextInputEditText agentEditText;
    private TextInputEditText soldDateEditText;
    private final String ID = "id";
    private int mPropertyId;
    private Property mProperty;
    private List<Property> mProperties = new ArrayList<>();
    private List<Photo> mPhotos = new ArrayList<>();
    private int photoKeyForBitmap = 1;
    private List<String> mPhotoUriList = new ArrayList<>();


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

    //TODO attach le VM au fragment
    private void configureViewModel() {
        mAddEditDetailedViewModel = new ViewModelProvider(requireActivity()).get(AddEditDetailedViewModel.class);
    }

    private void checkIfPropertyAlreadyExists() {
        assert getArguments() != null;
        if(!(getArguments().getInt(ID) == 0)) {
            mPropertyId = getArguments().getInt(ID);
            getProperty();
        }
    }

    private void observeProperties(){
        Log.e("", "observeProperties");
        mAddEditDetailedViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        Log.e("", propertiesList.toString());
        mProperties = propertiesList;
        photoKey = String.valueOf(mProperties.size()+1);
    }

    private void getProperty() {
        for(Property property : mProperties) {
            if(property.getId()==mPropertyId){
                mProperty = property;
            }
        }
        getPropertyData();
    }

    private void getPropertyData() {
        roomNumber = mProperty.getRoomNumber();
        description = mProperty.getDescription();
        entryDate = mProperty.getEntryDate();
        agent = mProperty.getAgent();
        onSale = mProperty.isOnSale();
        if(!onSale) {
            soldDate = mProperty.getSoldDate();
        }
        fillFormWithPropertyData();
    }

    private void fillFormWithPropertyData() {
        if(!(roomNumber ==0)){
            roomNumberEditText.setText(String.valueOf(roomNumber));
        }
        if(description!=null){
            descriptionEditText.setText(description);
        }
        if(entryDate!=null){
            entryDateEditText.setText(entryDate);
        }
        if(agent!=null){
            agentEditText.setText(agent);
        }
        if(!onSale){
            mBinding.switchSold.setChecked(true);
            soldDateEditText.setText(soldDate);
        }
    }

    private void getDataFromPreviousForm() {
        assert getArguments() != null;
        type = getArguments().getString(TYPE);
        price = getArguments().getString(PRICE);
        surface = getArguments().getString(SURFACE);
        address = getArguments().getString(ADDRESS);
        city = getArguments().getString(CITY);
        photoNumber = getArguments().getInt(PHOTO_NUMBER);
        Log.e("photo number detailed", String.valueOf(photoNumber));
        if(photoNumber>0) {
            for(int i=1; i<=photoNumber; i++){
                mPhotos.add(getArguments().getParcelable(String.valueOf(i)));
            }
            Log.e("photo uri", mPhotos.toString());
        }
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
                if(s==roomNumberEditText.getEditableText()){
                    roomNumber = Integer.parseInt(textInputEditText.getText().toString());
                }
                else if(s==descriptionEditText.getEditableText()){
                    description = textInputEditText.getText().toString();
                }
                else if(s==entryDateEditText.getEditableText()) {
                    entryDate = textInputEditText.getText().toString();
                }
                else if(s==agentEditText.getEditableText()) {
                    agent = textInputEditText.getText().toString();
                }
                else if(s==soldDateEditText.getEditableText()) {
                    soldDate = textInputEditText.getText().toString();
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

    private void selectEntryDateFromDatePicker() {
        mBinding.tfEntryDate.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configureDatePickerDialog(mBinding.etEntryDate);
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
                    onSale = false;
                }
                else {
                    mBinding.etSoldDate.setEnabled(false);
                    mBinding.tfSoldDate.setEnabled(false);
                    onSale = true;
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

    //TODO create an utils for that !
    //Dialog to alert about the add/edit annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_add_edit_title)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> {
                    for(Photo photo : mPhotos){
                        mAddEditDetailedViewModel.deletePhoto(photo.getPhotoId());
                    }
                    navigateToMainActivity();
                })
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void saveProperty() {
        mBinding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mProperties.isEmpty()){
                    Log.e("Properties before add", String.valueOf(mProperties.size()));
                }
                if(mPropertyId==0) {
                    Property newProperty = new Property(type, price, surface, roomNumber, description, address, city, onSale, entryDate, soldDate, agent, photoKey);
                    mAddEditDetailedViewModel.createProperty(newProperty);
                    mPropertyId = mProperties.size()+1;
                    Log.e("Properties after add", String.valueOf(mProperties.size()));
                }
                else {
                    editProperty();
                }
                navigateToPhotosFragment();
            }
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
        if(!onSale){
            mProperty.setSoldDate(soldDate);
        }
        mAddEditDetailedViewModel.editProperty(mProperty);
    }

    private void navigateToPhotosFragment() {
        final Bundle bundle = new Bundle();
        bundle.putInt(ID, mPropertyId);
        if (mAddEditPhotoFragment == null) {
            mAddEditPhotoFragment = AddEditPhotoFragment.newInstance();
            mAddEditPhotoFragment.setArguments(bundle);
        }
        if (!mAddEditPhotoFragment.isVisible()) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditPhotoFragment).commit();
        }
    }
    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
