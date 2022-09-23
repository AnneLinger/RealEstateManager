package com.openclassrooms.realestatemanager.data.repositories;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.domain.models.Photo;

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

    //Create a photo
    public void addPhoto(Photo photo) {
        mPhotoDao.addPhoto(photo);
    }

    //Delete a photo
    public void deletePhoto(int photoId) {
        mPhotoDao.deletePhoto(photoId);
    }
}
