package com.openclassrooms.realestatemanager.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

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

    public List<Property> getSearchProperties(SupportSQLiteQuery query) {
        return mPropertyRepository.getSearchProperties(query);
    }
}
