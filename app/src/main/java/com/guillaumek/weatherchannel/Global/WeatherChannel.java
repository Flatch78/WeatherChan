package com.guillaumek.weatherchannel.Global;

import android.os.Environment;

import java.io.File;

/**
 * Created by flatch on 09/11/15.
 */
public final class WeatherChannel {

    public static final File EXT_DIRECTORY = new File(Environment.getExternalStorageDirectory(), "WeatherChannel");
    public static final File EXT_FILE_LIST_CITY = new File(EXT_DIRECTORY, "list_city.txt");
}
