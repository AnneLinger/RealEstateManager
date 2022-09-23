package com.openclassrooms.realestatemanager.ui.details;

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

import java.util.List;

/**
 * Adapter and ViewHolder to display an horizontal recycler view for photos
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<Photo> mPhotos;

    public PhotosAdapter(List<Photo> photos) {
        mPhotos = photos;
    }

    @NonNull
    @Override
    public PhotosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder holder, int position) {
        holder.displayPhoto(mPhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photoImage;
        private final TextView photoDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.im_photo_rv);
            photoDescription = itemView.findViewById(R.id.tv_photo_description);
        }

        public void displayPhoto(Photo photo) {
            photoImage.setImageURI(Uri.parse(photo.getPhotoUri()));
            photoDescription.setText(photo.getPhotoLabel());
        }
    }
}

