package com.openclassrooms.realestatemanager.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

/**
*Created by Anne Linger on 14/09/2022.
*/

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    public ListViewAdapter() {
    }

    @NonNull
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.ViewHolder holder, int position) {
        holder.displayPlace();
    }

    @Override
    public int getItemCount() {
        //return mPlaceList.size();
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_place_name);
        }

        private void displayPlace() {
        }

        private void navigateToPropertyDetails() {
            //Intent intent = new Intent(itemView.getContext(), PlaceDetailsActivity.class);
            //intent.putExtra("place id", place.getPlaceId());
            //itemView.getContext().startActivity(intent);
        }
    }
}
