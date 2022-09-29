package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.data.repositories.PropertyRepositoryImpl;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for search fragment
 */

@HiltViewModel
public class SearchViewModel extends ViewModel {

    //For data
    private final PropertyRepositoryImpl mPropertyRepository;

    @Inject
    public SearchViewModel(PropertyRepositoryImpl propertyRepository) {
        mPropertyRepository = propertyRepository;
    }

    public List<Property> getSearchProperties(SupportSQLiteQuery query) {
        return mPropertyRepository.getSearchProperties(query);
    }
}
