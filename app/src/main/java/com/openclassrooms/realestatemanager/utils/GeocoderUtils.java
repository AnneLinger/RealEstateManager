package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
*Created by Anne Linger on 26/09/2022.
*/

public class GeocoderUtils {

    public static double getLatitudeFromAddress(String stringAddress, Context context){
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        double latitude = 0;

        try {
            address = coder.getFromLocationName(stringAddress, 5);
            Log.e("InGeodocder", address.get(0).toString());
            Address location = address.get(0);

            latitude = location.getLatitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latitude;
    }


    public static double getLongitudeFromAddress(String stringAddress, Context context){
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        double longitude = 0;

        try {
            address = coder.getFromLocationName(stringAddress, 5);
            Address location = address.get(0);

            longitude = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return longitude;
    }
}
