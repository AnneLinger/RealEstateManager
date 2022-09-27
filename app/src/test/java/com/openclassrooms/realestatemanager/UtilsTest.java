package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;

import okhttp3.internal.Util;

/**
*Created by Anne Linger on 27/09/2022.
*/

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void checkConvertDollarToEuro() {
        int priceConvert = Utils.convertDollarToEuro(100);
        int priceExpected = (int) (100 * 0.962);
        assertEquals(priceConvert, priceExpected);
    }

    @Test
    public void checkConvertEuroToDollar() {
        int priceConvert = Utils.convertEuroToDollar(100);
        int priceExpected = (int) (100 * 1.041);
        assertEquals(priceConvert, priceExpected);
    }

    @Test
    public void checkGetTodayDate() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = dateFormat.parse(Utils.getTodayDate());
        assert date != null;
        assertEquals(dateFormat.format(date), Utils.getTodayDate());
    }

    @Test
    public void checkGetTodayDateFormat() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse(Utils.getTodayDateFormat());
        assert date != null;
        assertEquals(dateFormat.format(date), Utils.getTodayDateFormat());
    }
}
