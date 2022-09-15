package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
*Created by Anne Linger on 15/09/2022.
*/

@HiltViewModel
public class ListViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public ListViewModel(PropertyRepositoryImpl propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
    }

    public LiveData<List<Property>> getProperties() {
        return mPropertyRepository.getProperties();
    }

}
