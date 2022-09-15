package com.openclassrooms.realestatemanager.domain.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

/**
*Model class for properties
*/

@Entity(tableName = "property_table")
public class Property {

    //TODO Add a photo class and add a foreign key for it ?
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    private int id;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "surface")
    private int surface;

    @ColumnInfo(name = "room_number")
    private int roomNumber;

    @Nullable
    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "on_sale")
    private boolean onSale;

    @ColumnInfo(name = "entry_date")
    private String entryDate;

    @Nullable
    @ColumnInfo(name = "sold_date")
    private String soldDate;

    @ColumnInfo(name = "agent")
    private String agent;

    public Property(String type, int price, int surface, int roomNumber, @Nullable String description, String address, boolean onSale, String entryDate, @Nullable String soldDate, String agent) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.roomNumber = roomNumber;
        this.description = description;
        this.address = address;
        this.onSale = onSale;
        this.entryDate = entryDate;
        this.soldDate = soldDate;
        this.agent = agent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
