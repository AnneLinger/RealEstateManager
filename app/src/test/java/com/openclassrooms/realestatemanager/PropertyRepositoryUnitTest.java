package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.data.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Property;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Unit tests for the PropertyRepository
 */

public class PropertyRepositoryUnitTest {

    //For Database
    private final PropertyDao mPropertyDao = Mockito.mock(PropertyDao.class);

    //For repository
    private PropertyRepositoryImpl mPropertyRepository;

    @Before
    public void setUp() {
        mPropertyRepository = new PropertyRepositoryImpl(mPropertyDao);
    }

    //Test get all properties
    @Test
    public void getProperties() {
        //Recover the properties with Dao
        LiveData<List<Property>> mExpectedProperties = new MutableLiveData<>();
        when(mPropertyDao.getProperties()).thenReturn(mExpectedProperties);

        //Recover the properties with the repository
        LiveData<List<Property>> mProperties = mPropertyRepository.getProperties();

        //Assert that the two lists are the same
        assertEquals(mExpectedProperties, mProperties);
        Mockito.verify(mPropertyDao, atLeastOnce()).getProperties();
        verifyNoMoreInteractions(mPropertyDao);
    }

    //Test get one Property
    @Test
    public void getProperty() {
        //Recover a property with the Dao
        LiveData<Property> mExpectedProperty = new MutableLiveData<>();
        when(mPropertyDao.getProperty(1)).thenReturn(mExpectedProperty);

        //Recover a property with the repository
        LiveData<Property> mProperty = mPropertyRepository.getProperty(1);

        //Assert that the two properties are the same
        assertEquals(mExpectedProperty, mProperty);
        Mockito.verify(mPropertyDao, atLeastOnce()).getProperty(1);
        verifyNoMoreInteractions(mPropertyDao);
    }
}
