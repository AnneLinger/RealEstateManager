package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.repositories.PhotoRepositoryImpl;
import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.search.SearchFragment;

import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
*Created by Anne Linger on 15/09/2022.
*/

@HiltViewModel
public class SearchViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;

    //For threads
    private final Executor mExecutor;

    @Inject
    public SearchViewModel(PropertyRepositoryImpl propertyRepository, Executor executor) {
        mPropertyRepository = propertyRepository;
        mExecutor = executor;
    }

    public LiveData<List<Property>> getSearchProperties(@Nullable String type,
                                                        String minPrice,
                                                        String maxPrice,
                                                        String minSurface,
                                                        String maxSurface,
                                                        int minRoomNumber,
                                                        int maxRoomNumber,
                                                        String address,
                                                        boolean onSale) {
        return mPropertyRepository.getSearchProperties(type, minPrice, maxPrice, minSurface, maxSurface, minRoomNumber, maxRoomNumber, address, onSale);
    }
}
