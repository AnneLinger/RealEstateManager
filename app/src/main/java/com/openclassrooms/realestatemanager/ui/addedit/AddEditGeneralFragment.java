package com.openclassrooms.realestatemanager.ui.addedit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentGeneralDataBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.ChipUtils;
import com.openclassrooms.realestatemanager.utils.PopupUtils;
import com.openclassrooms.realestatemanager.viewmodels.AddEditDetailedViewModel;
import com.openclassrooms.realestatemanager.viewmodels.AddEditGeneralViewModel;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
*Fragment to add or edit general information about property
*/

public class AddEditGeneralFragment extends Fragment {

    //For ui
    private FragmentGeneralDataBinding mBinding;

    //For navigation
    private NavController mNavController;

    //For data
    private AddEditDetailedFragment mAddEditDetailedFragment;
    private AddEditGeneralViewModel mAddEditGeneralViewModel;
    private String type;
    private final String TYPE = "type";
    private String price;
    private final String PRICE = "price";
    private String surface = null;
    private final String SURFACE = "surface";
    private String address = null;
    private final String ADDRESS = "address";
    private String city;
    private final String CITY = "city";
    private TextInputEditText typeEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText surfaceEditText;
    private TextInputEditText addressEditText;
    private TextInputEditText cityEditText;
    private final String ID = "id";
    private int mPropertyId = 0;
    private Property mProperty;
    private List<Property> mProperties;
    private List<String> mPhotoUriList = new ArrayList<>();
    private int mPhotoNumber = 0;
    private final String PHOTO_NUMBER = "photo_number";
    private int photoKeyForBundle = 1;
    private List<String> mPhotosLabels = new ArrayList<>();
    private final CharSequence[] mCharSequences = new CharSequence[] {"Exterior", "Kitchen", "Living Room", "Bedroom", "Bathroom", "Garden", "Else"};
    private String tempLabel;

    public static AddEditGeneralFragment newInstance() {
        return new AddEditGeneralFragment();
    }

    //For photo permission result
    private static final String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private final ActivityResultLauncher<String[]> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
        boolean granted = true;
        for (Map.Entry<String, Boolean> x : isGranted.entrySet())
            if (!x.getValue()) granted = false;
        if (granted) addPhoto();
        else {
            showDialogToDenyAddProperty();
        }
    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentGeneralDataBinding.inflate(inflater, container, false);
        typeEditText = mBinding.etType;
        priceEditText = mBinding.etPrice;
        surfaceEditText = mBinding.etSurface;
        addressEditText = mBinding.etAddress;
        cityEditText = mBinding.etCity;
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        configureViewModel();
        checkIfPropertyAlreadyExists();
        getDataFromForm();
        checkPermissionsToAddPhoto();
        getPropertyPhotos();
        navigateToNextFragmentAddEdit();
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
        mAddEditGeneralViewModel = new ViewModelProvider(requireActivity()).get(AddEditGeneralViewModel.class);
    }

    private void checkIfPropertyAlreadyExists() {
        if(getArguments()!=null) {
            mPropertyId = getArguments().getInt(ID);
            observeProperties();
        }
    }

    private void observeProperties(){
        Log.e("", "observeProperties");
        mAddEditGeneralViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        Log.e("", propertiesList.toString());
        mProperties = propertiesList;
        getProperty();
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
        type = mProperty.getType();
        price = mProperty.getPrice();
        surface = mProperty.getSurface();
        address = mProperty.getAddress();
        city = mProperty.getCity();
        fillFormWithPropertyData();
    }

    private void fillFormWithPropertyData() {
        if(type!=null){
            typeEditText.setText(type);
        }
        if(price!=null){
            priceEditText.setText(price);
        }
        if(surface!=null){
            surfaceEditText.setText(surface);
        }
        if(address!=null){
            addressEditText.setText(address);
        }
        if(city!=null){
            cityEditText.setText(city);
        }
    }

    private void getDataFromForm() {
        getDataFromEditText(typeEditText);
        getDataFromEditText(priceEditText);
        getDataFromEditText(surfaceEditText);
        getDataFromEditText(addressEditText);
        getDataFromEditText(cityEditText);
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
                else if(s==priceEditText.getEditableText()){
                    price = textInputEditText.getText().toString();
                }
                else if(s==surfaceEditText.getEditableText()) {
                    surface = textInputEditText.getText().toString();
                }
                else if(s==addressEditText.getEditableText()) {
                    address = textInputEditText.getText().toString();
                }
                else if(s==cityEditText.getEditableText()) {
                    city = textInputEditText.getText().toString();
                }
                enableButtonNext();
            }
        });
    }

    private void checkPermissionsToAddPhoto() {
        mBinding.btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(),
                                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //Permission ok => get a photo
                    addPhoto();
                } else {
                    //Permission not ok => ask permission to the user for location
                    mActivityResultLauncher.launch(perms);
                }
            }
        });
    }

    private void addPhoto() {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        mPhotoNumber+=1;
                        Uri tempUri = getImageUri(requireContext(), selectedImage);
                        mPhotoUriList.add(tempUri.toString());
                        Log.e("photo uri", mPhotoUriList.toString());
                        addPhotoLabel(tempUri.toString());

                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                mPhotoUriList.add(selectedImage.toString());
                                mPhotoNumber+=1;
                                cursor.close();
                                Log.e("photo uri", mPhotoUriList.toString());
                                addPhotoLabel(selectedImage.toString());
                            }
                        }
                    }
                    break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, getString(R.string.title), null);
        return Uri.parse(path);
    }

    private void addPhotoLabel(String uriString){
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Choose a label for your photo")
                .setCancelable(false)
                .setSingleChoiceItems(mCharSequences, 6, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tempsLabel = mCharSequences[which].toString();
                        mPhotosLabels.add(tempsLabel);
                        managePhotoChipGroup(uriString, tempsLabel);
                        Log.e("photo label list", mPhotosLabels.toString());
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void managePhotoChipGroup(String uriString, String label) {
        LayoutInflater inflater = LayoutInflater.from(mBinding.chipGroup.getContext());
        Chip chip = (Chip) inflater.inflate(R.layout.chip_entry, mBinding.chipGroup, false);
        //chip.setText(MessageFormat.format("{0}{1}", getString(R.string.photo), String.valueOf(mPhotoUriList.size()) + " " + label));
        chip.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.photo), uriString.substring(uriString.length() - 5), label));
        chip.setCloseIconVisible(true);
        mBinding.chipGroup.addView(chip);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChipUtils.deleteAChipFromAView((Chip) view);
                ChipUtils.deleteAChipFromAList((Chip) view, mPhotoUriList);
                ChipUtils.deleteAChipFromAList((Chip) view, mPhotosLabels);
            }
        });
    }

    private void setPhotoLabelTextView(String label) {
        mBinding.firstTvPhotoLabel.setText(label);
        mPhotosLabels.add(label);
    }

    private void getPropertyPhotos() {
    }

    //TODO add one photo necessary to enable
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
                    navigateToMainActivity();
                })
                .setNegativeButton(R.string.continue_button, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    //Dialog to alert about essential permission
    public void showDialogToDenyAddProperty() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext(), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.permissions_required)
                .setMessage(R.string.photo_permission_text)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_add_edit_title, (dialog, which) -> {
                    navigateToMainActivity();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void navigateToNextFragmentAddEdit() {
        mBinding.btNextAddEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle bundle = new Bundle();
                bundle.putInt(ID, mPropertyId);
                bundle.putString(TYPE, type);
                bundle.putString(PRICE, price);
                bundle.putString(SURFACE, surface);
                bundle.putString(ADDRESS, address);
                bundle.putString(CITY, city);
                Log.e("photo number", String.valueOf(mPhotoNumber));
                bundle.putInt(PHOTO_NUMBER, mPhotoNumber);
                Log.e("photo uri before bundle", mPhotoUriList.toString());
                for(String string : mPhotoUriList){
                    bundle.putString(String.valueOf(photoKeyForBundle), string);
                    Log.e("photo uri key", String.valueOf(photoKeyForBundle));
                    photoKeyForBundle+=1;
                }
                if (mAddEditDetailedFragment == null) {
                    mAddEditDetailedFragment = AddEditDetailedFragment.newInstance();
                    mAddEditDetailedFragment.setArguments(bundle);
                }
                if (!mAddEditDetailedFragment.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditDetailedFragment).commit();
                }
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
