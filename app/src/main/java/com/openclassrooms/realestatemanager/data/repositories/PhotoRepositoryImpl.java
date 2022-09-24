package com.openclassrooms.realestatemanager.data.repositories;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;

import javax.inject.Inject;

/**
* Repository for photos
*/

public class PhotoRepositoryImpl {

    //For data
    private final PhotoDao mPhotoDao;

    @Inject
    public PhotoRepositoryImpl(PhotoDao photoDao) {
        mPhotoDao = photoDao;
    }

    //Get the list of the photos
    public LiveData<List<Photo>> getAllPhotos() {
        return mPhotoDao.getAllPhotos();
    }

    //Get property photos
    public LiveData<List<Photo>> getPropertyPhotos(Long propertyId) {
        return mPhotoDao.getPhotos(propertyId);
    }

    //Create a photo
    public void addPhoto(Photo photo) {
        mPhotoDao.addPhoto(photo);
    }

    //Update a photo
    public void editPhoto(Photo photo) {
        mPhotoDao.updatePhoto(photo);
    }

    //Delete a photo
    public void deletePhoto(int photoId) {
        mPhotoDao.deletePhoto(photoId);
    }
}
