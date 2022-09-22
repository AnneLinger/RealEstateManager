package com.openclassrooms.realestatemanager.domain.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
*Model class for properties photos
*/

@Entity(tableName = "photo_table",
        foreignKeys = @ForeignKey(entity = Property.class, parentColumns = "id", childColumns = "property_id", onDelete = ForeignKey.CASCADE))
public class Photo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "photo_id")
    private int photoId;

    @ColumnInfo(name = "property_id")
    private int propertyId;

    @ColumnInfo(name = "photoUri")
    private String photoUri;

    @ColumnInfo(name = "photo_description")
    private String photoDescription;

    public Photo(int photoId, int propertyId, String photoUri, String photoDescription) {
        this.photoId = photoId;
        this.propertyId = propertyId;
        this.photoUri = photoUri;
        this.photoDescription = photoDescription;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }
}
