package com.openclassrooms.realestatemanager.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
*Model class for properties photos
*/

@Entity(tableName = "photo_table",
        foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "id", childColumns = "property_id", onDelete = ForeignKey.CASCADE))
public class Photo implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photo_id")
    private int photoId;

    @ColumnInfo(name = "property_id")
    private Long propertyId;

    @ColumnInfo(name = "photoUri")
    private String photoUri;

    @ColumnInfo(name = "photo_label")
    private String photoLabel;

    public Photo(Long propertyId, String photoUri, String photoLabel) {
        this.propertyId = propertyId;
        this.photoUri = photoUri;
        this.photoLabel = photoLabel;
    }

    //Parcel constructor
    protected Photo(Parcel in) {
        propertyId = in.readLong();
        photoUri = in.readString();
        photoLabel = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoLabel() {
        return photoLabel;
    }

    public void setPhotoLabel(String photoLabel) {
        this.photoLabel = photoLabel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(photoId);
        dest.writeLong(propertyId);
        dest.writeString(photoUri);
        dest.writeString(photoLabel);
    }
}
