package com.openclassrooms.realestatemanager.ui.listview;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListViewBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment;
import com.openclassrooms.realestatemanager.viewmodels.ListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display the properties list
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class ListViewFragment extends Fragment implements ListViewAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    //For UI
    private FragmentListViewBinding mBinding;

    //For data
    private ListViewModel mListViewModel;
    private AddEditGeneralFragment mAddEditGeneralFragment;
    private Context mContext;
    private List<Property> mProperties;
    private List<Photo> mPhotos;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
        mSwipeRefreshLayout = mBinding.swipeLayout;
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.color_primary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        this.onAttach(requireContext());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModel();
        observeProperties();
        addAProperty();
    }

    @Override
    public void onItemClick(long id) {
        Fragment fragment = DetailsFragment.newInstance(id);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int orientation = getResources().getConfiguration().orientation;
        if (isTablet && orientation == Configuration.ORIENTATION_LANDSCAPE) {
            transaction.replace(R.id.nav_host_fragment_details, fragment, "details_fragment");
        } else {
            transaction.replace(R.id.nav_host_fragment, fragment, "details_fragment");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void configureViewModel() {
        mListViewModel = new ViewModelProvider(requireActivity()).get(ListViewModel.class);
    }

    private void observeProperties() {
        mListViewModel.getProperties().observe(requireActivity(), this::getProperties);
    }

    private void getProperties(List<Property> propertiesList) {
        mProperties = propertiesList;
        observePhotos();
    }

    private void observePhotos() {
        mListViewModel.getAllPhotos().observe(requireActivity(), this::getPhotos);
    }

    private void getPhotos(List<Photo> photos) {
        mPhotos = photos;
        checkIfAResearchHadTakenPlace();
    }

    private void checkIfAResearchHadTakenPlace() {
        List<Property> searchProperties = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String BUNDLE_KEY = "search_properties";
            List<String> searchPropertiesIdString = bundle.getStringArrayList(BUNDLE_KEY);
            for (String string : searchPropertiesIdString) {
                long id = Long.parseLong(string);
                for (Property property : mProperties) {
                    if (property.getId() == id) {
                        searchProperties.add(property);
                    }
                }
            }
            initRecyclerView(searchProperties, mPhotos);
        } else {
            initRecyclerView(mProperties, mPhotos);
        }
    }

    private void initRecyclerView(List<Property> properties, List<Photo> photos) {
        RecyclerView recyclerView = mBinding.rvListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new ListViewAdapter(properties, photos, this));
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void addAProperty() {
        mBinding.fabAdd.setOnClickListener(v -> {
            if (mAddEditGeneralFragment == null) {
                mAddEditGeneralFragment = AddEditGeneralFragment.newInstance();
            }
            if (!mAddEditGeneralFragment.isVisible()) {
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditGeneralFragment).commit();
            }
        });
    }

    @Override
    public void onRefresh() {
        initRecyclerView(mProperties, mPhotos);
    }
}
