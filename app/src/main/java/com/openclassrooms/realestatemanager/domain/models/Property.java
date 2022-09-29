package com.openclassrooms.realestatemanager.domain.models;

import android.content.ContentValues;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

/**
 * Model class for properties
 */

@Entity(tableName = "property_table")
public class Property {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "price")
    private String price;

    @Nullable
    @ColumnInfo(name = "surface")
    private String surface;

    @ColumnInfo(name = "room_number")
    private int roomNumber;

    @Nullable
    @ColumnInfo(name = "description")
    private String description;

    @Nullable
    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "on_sale")
    private boolean onSale;

    @ColumnInfo(name = "entry_date")
    private String entryDate;

    @Nullable
    @ColumnInfo(name = "sold_date")
    private String soldDate;

    @ColumnInfo(name = "agent")
    private String agent;

    @ColumnInfo(name = "photo_key")
    private String photoKey;

    public Property() {
    }

    public Property(String type, String price, @Nullable String surface, int roomNumber, @Nullable String description, @Nullable String address, String city, boolean onSale, String entryDate, @Nullable String soldDate, String agent, String photoKey) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.roomNumber = roomNumber;
        this.description = description;
        this.address = address;
        this.city = city;
        this.onSale = onSale;
        this.entryDate = entryDate;
        this.soldDate = soldDate;
        this.agent = agent;
        this.photoKey = photoKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Nullable
    public String getSurface() {
        return surface;
    }

    public void setSurface(@Nullable String surface) {
        this.surface = surface;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    @Nullable
    public String getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(@Nullable String soldDate) {
        this.soldDate = soldDate;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getPhotoKey() {
        return photoKey;
    }

    public void setPhotoKey(String photoKey) {
        this.photoKey = photoKey;
    }

    @SuppressWarnings("unused")
    public static Property fromContentValues(ContentValues values) {

        final Property property = new Property();

        if (values.containsKey("type")) property.setType(values.getAsString("type"));

        if (values.containsKey("price")) property.setPrice(values.getAsString("price"));

        if (values.containsKey("surface")) property.setSurface(values.getAsString("surface"));

        if (values.containsKey("room_number"))
            property.setRoomNumber(values.getAsInteger("room_number"));

        if (values.containsKey("description"))
            property.setDescription(values.getAsString("description"));

        if (values.containsKey("address")) property.setAddress(values.getAsString("address"));

        if (values.containsKey("city")) property.setCity(values.getAsString("city"));

        if (values.containsKey("on_sale")) property.setOnSale(values.getAsBoolean("on_sale"));

        if (values.containsKey("entry_date"))
            property.setEntryDate(values.getAsString("entry_date"));

        if (values.containsKey("sold_date")) property.setSoldDate(values.getAsString("sold_date"));

        if (values.containsKey("agent")) property.setAgent(values.getAsString("agent"));

        if (values.containsKey("photo_key")) property.setPhotoKey(values.getAsString("photo_key"));

        return property;

    }

}
