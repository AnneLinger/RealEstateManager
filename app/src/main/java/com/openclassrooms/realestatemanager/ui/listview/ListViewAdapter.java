package com.openclassrooms.realestatemanager.ui.listview;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.domain.models.Photo;
import com.openclassrooms.realestatemanager.domain.models.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter and ViewHolder to display the list of the properties
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private final List<Property> mProperties;
    private final List<Photo> mPhotos;
    private final OnItemClickListener mListener;

    public ListViewAdapter(List<Property> properties, List<Photo> photos, OnItemClickListener listener) {
        mProperties = properties;
        mPhotos = photos;
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
        holder.displayProperty(mProperties.get(position), mPhotos);
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(mProperties.get(position).getId()));
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

        private void displayProperty(Property property, List<Photo> photos) {
            List<Photo> propertyPhotos = new ArrayList<>();
            for (Photo photo : photos) {
                if (photo.getPropertyId() == property.getId()) {
                    propertyPhotos.add(photo);
                }
            }
            if (propertyPhotos.size() > 0) {
                photo.setImageURI(Uri.parse(propertyPhotos.get(0).getPhotoUri()));
            }
            type.setText(property.getType());
            city.setText(property.getCity());
            price.setText(String.format("$%s", property.getPrice()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
    }
}
