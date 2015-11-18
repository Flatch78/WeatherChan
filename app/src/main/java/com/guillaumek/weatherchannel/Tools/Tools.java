package com.guillaumek.weatherchannel.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by flatch on 09/11/15.
 */
public class Tools {

    public static double getCelsiusValue(double kelvin) {
        return kelvin - 273.15;
    }

    public static double getFahrenhietValue(double kelvin) {
        return  (getCelsiusValue(kelvin) * 9.0/5.0) + 32.0;
    }

    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
