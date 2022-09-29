package com.openclassrooms.realestatemanager.utils;

import java.util.Calendar;

/**
 * Useful methods for date and time
 */
public class DateUtils {

    //Recover the date from the DatePickerDialog
    public java.util.Date getDateFromDatePicker(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
}
