package com.openclassrooms.realestatemanager.ui.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.domain.models.Property;
import com.openclassrooms.realestatemanager.ui.details.DetailsFragment;
import com.openclassrooms.realestatemanager.ui.main.MainActivity;

import java.util.List;

/**
*Created by Anne Linger on 14/09/2022.
*/

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private List<Property> mProperties;
    private OnItemClickListener mListener;

    public ListViewAdapter(List<Property> properties, OnItemClickListener listener) {
        mProperties = properties;
        mListener = listener;
    }

    @NonNull
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.displayProperty(mProperties.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(mProperties.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProperties.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photo;
        private final TextView type;
        private final TextView city;
        private final TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.im_photo);
            type = itemView.findViewById(R.id.tv_type);
            city = itemView.findViewById(R.id.tv_city);
            price = itemView.findViewById(R.id.tv_price);
        }

        private void displayProperty(Property property) {
            //TODO set also photo ! !
            type.setText(property.getType());
            city.setText(property.getCity());
            price.setText(String.format("$%s", property.getPrice()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int id);
    }
}
