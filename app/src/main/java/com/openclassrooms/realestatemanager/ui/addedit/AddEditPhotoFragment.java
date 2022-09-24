package com.openclassrooms.realestatemanager.ui.addedit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentPhotoDataBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;
import com.openclassrooms.realestatemanager.utils.ChipUtils;
import com.openclassrooms.realestatemanager.viewmodels.AddEditPhotoViewModel;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
*Fragment to add or edit property photos
*/

public class AddEditPhotoFragment extends Fragment {

    //For ui
    private FragmentPhotoDataBinding mBinding;

    //For data
    private AddEditPhotoViewModel mAddEditPhotoViewModel;
    private long mPropertyId;
    private Property mProperty;
    private List<Property> mProperties;
    private List<String> mPhotoUriList = new ArrayList<>();
    private final CharSequence[] mCharSequences = new CharSequence[] {"Exterior", "Kitchen", "Living Room", "Bedroom", "Bathroom", "Garden", "Else"};
    private List<Photo> mPhotos = new ArrayList<>();
    private final String ID = "id";

    public static AddEditPhotoFragment newInstance() {
        return new AddEditPhotoFragment();
    }

    //For photo permission result
    private static final String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
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
        mBinding = FragmentPhotoDataBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        configureViewModel();
        //observeProperties();
        observePropertyPhotos();
        checkPermissionsToAddPhoto();
        finishAddEdit();
    }

    private void initUi() {
        configureToolbar();
        configureBottomNav();
        mBinding.btSavePhotos.setEnabled(false);
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
        mAddEditPhotoViewModel = new ViewModelProvider(requireActivity()).get(AddEditPhotoViewModel.class);
    }

    private void observeProperties(){
        mAddEditPhotoViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        mProperties = propertiesList;
        if(mProperties.size()>0) {
            getProperty();
        }
    }

    private void getProperty() {
        assert getArguments() != null;
        mPropertyId = getArguments().getLong(ID);
        for(Property property : mProperties) {
            if(property.getId()==mPropertyId){
                mProperty = property;
            }
        }
        //observePropertyPhotos();
    }

    private void observePropertyPhotos() {
        assert getArguments() != null;
        mPropertyId = getArguments().getLong(ID);
        //mAddEditPhotoViewModel.getPropertyPhotos(mPropertyId).observe((getViewLifecycleOwner()), observer);
        mAddEditPhotoViewModel.getPropertyPhotos(mPropertyId).observe(getViewLifecycleOwner(), this::getPropertyPhotos);
    }

    Observer<List<Photo>> observer = new Observer<List<Photo>>() {
        @Override
        public void onChanged(@Nullable List<Photo> photos) {
            /**if (!photos.isEmpty() && mPhotos.isEmpty()) {
                mPhotos.addAll(photos);
                fillFormWithPropertyPhotos();
            }
            if(!mPhotos.isEmpty()){
                mPhotos.removeAll(photos);
                mAddEditPhotoViewModel.getPropertyPhotos(mPropertyId).removeObservers(getViewLifecycleOwner());
            }*/
            mPhotos.clear();
            assert photos != null;
            mPhotos.addAll(photos);
            mAddEditPhotoViewModel.getPropertyPhotos(mPropertyId).removeObservers(getViewLifecycleOwner());
            fillFormWithPropertyPhotos();
        }
    };

    private void getPropertyPhotos(List<Photo> photos) {
        if (!photos.isEmpty()) {
            mPhotos.clear();
            mBinding.chipGroup.removeAllViews();
            mPhotos.addAll(photos);
        }
        fillFormWithPropertyPhotos();
    }

    private void fillFormWithPropertyPhotos() {
        for (Photo photo : mPhotos) {
            managePhotoChipGroup(photo);
        }
        mAddEditPhotoViewModel.getPropertyPhotos(mPropertyId).removeObservers(getViewLifecycleOwner());
    }

    private void checkPermissionsToAddPhoto() {
        mBinding.btPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(requireContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
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
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery"}; // create a menuOption Array
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
                        Uri tempUri = getImageUri(requireContext().getApplicationContext(), selectedImage);
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
                                //int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                //String picturePath = cursor.getString(columnIndex);
                                cursor.close();
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

        //Get current date to add to path title and description, can't work without it
        Date date = new Date(System.currentTimeMillis());

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title" + date, "New picture" + date);
        return Uri.parse(path);
    }

    private void addPhotoLabel(String uriString){
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle("Choose a label for your photo")
                .setCancelable(false)
                .setSingleChoiceItems(mCharSequences, 6, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Photo newPhoto = mAddEditPhotoViewModel.createPhoto(mPropertyId, uriString, mCharSequences[which].toString());
                        //mPhotos.add(newPhoto);
                        managePhotoChipGroup(newPhoto);
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void managePhotoChipGroup(Photo photo) {
        LayoutInflater inflater = LayoutInflater.from(mBinding.chipGroup.getContext());
        Chip chip = (Chip) inflater.inflate(R.layout.chip_entry, mBinding.chipGroup, false);
        chip.setText(MessageFormat.format("{0} {1} {2}", getString(R.string.photo), photo.getPhotoUri().substring(photo.getPhotoUri().length() - 5), photo.getPhotoLabel()));
        chip.setCloseIconVisible(true);
        mBinding.chipGroup.addView(chip);
        enableButtonSave();
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChipUtils.deleteAChipFromAView((Chip) view);
                mAddEditPhotoViewModel.deletePhoto(photo.getPhotoId());
                //mPhotos.remove(photo);
            }
        });
    }

    //The button next is enabled only when all fields required are filled
    private void enableButtonSave() {
        if (mPhotos.isEmpty()){
            mBinding.btSavePhotos.setEnabled(false);
        } else {
            mBinding.btSavePhotos.setEnabled(true);

        }
    }

    private void finishAddEdit() {
        mBinding.btSavePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });
    }

    //Dialog to alert about the add/edit annulment
    public void showDialogToConfirmCancel() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder((this.requireContext()), R.style.AlertDialogTheme);
        alertDialogBuilder.setTitle(R.string.cancel_add_edit_title)
                .setMessage(R.string.confirm_cancel_message)
                .setCancelable(false)
                .setPositiveButton(R.string.cancel_button, (dialog, which) -> {
                    for(Photo photo : mPhotos){
                        mAddEditPhotoViewModel.deletePhoto(photo.getPhotoId());
                    }
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

    private void navigateToMainActivity() {
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
    }
}
