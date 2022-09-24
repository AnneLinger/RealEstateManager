package com.openclassrooms.realestatemanager.ui.listview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListViewBinding;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.addedit.AddEditGeneralFragment;
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment;
import com.openclassrooms.realestatemanager.viewmodels.ListViewModel;

import java.util.List;

/**
*Fragment to display the properties list
*/

public class ListViewFragment extends Fragment implements ListViewAdapter.OnItemClickListener {

    //For UI
    private FragmentListViewBinding mBinding;
    private RecyclerView mRecyclerView;

    //For data
    private ListViewModel mListViewModel;
    private AddEditGeneralFragment mAddEditGeneralFragment;
    private Context mContext;
    private List<Property> mProperties;
    private List<Photo> mPhotos;

    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentListViewBinding.inflate(inflater, container, false);
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment, "details_fragment");
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

    private void observeProperties(){
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
        initRecyclerView(mProperties, mPhotos);
    }

    private void initRecyclerView(List<Property> properties, List<Photo> photos) {
        mRecyclerView = mBinding.rvListView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new ListViewAdapter(properties, photos, this));
    }

    private void addAProperty() {
        mBinding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddEditGeneralFragment == null) {
                    mAddEditGeneralFragment = AddEditGeneralFragment.newInstance();
                }
                if (!mAddEditGeneralFragment.isVisible()) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, mAddEditGeneralFragment).commit();
                }
                //mNavController.navigate(R.id.action_listViewFragment_to_addEditGeneralFragment);
            }
        });
    }
}
