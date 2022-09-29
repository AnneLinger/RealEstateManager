package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.data.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.repositories.PhotoRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Photo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Unit tests for the PhotoRepository
 */

public class PhotoRepositoryUnitTest {

    //For Database
    private final PhotoDao mPhotoDao = Mockito.mock(PhotoDao.class);

    //For repository
    private PhotoRepositoryImpl mPhotoRepository;

    @Before
    public void setUp() {
        mPhotoRepository = new PhotoRepositoryImpl(mPhotoDao);
    }

    //Test get all photos
    @Test
    public void getAllPhotos() {
        //Recover the photos with Dao
        LiveData<List<Photo>> mExpectedPhotos = new MutableLiveData<>();
        when(mPhotoDao.getAllPhotos()).thenReturn(mExpectedPhotos);

        //Recover the photos with the repository
        LiveData<List<Photo>> mPhotos = mPhotoRepository.getAllPhotos();

        //Assert that the two lists are the same
        assertEquals(mExpectedPhotos, mPhotos);
        Mockito.verify(mPhotoDao, atLeastOnce()).getAllPhotos();
        verifyNoMoreInteractions(mPhotoDao);
    }

    //Test get a property photos
    @Test
    public void getPropertyPhotos() {
        //Recover photos with the Dao
        LiveData<List<Photo>> mExpectedPhotos = new MutableLiveData<>();
        when(mPhotoDao.getPhotos(1L)).thenReturn(mExpectedPhotos);

        //Recover photos with the repository
        LiveData<List<Photo>> mPhotos = mPhotoRepository.getPropertyPhotos(1L);

        //Assert that the two properties are the same
        assertEquals(mExpectedPhotos, mPhotos);
        Mockito.verify(mPhotoDao, atLeastOnce()).getPhotos(1L);
        verifyNoMoreInteractions(mPhotoDao);
    }
}
