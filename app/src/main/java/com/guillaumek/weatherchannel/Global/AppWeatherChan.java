package com.guillaumek.weatherchannel.Global;

import android.app.Application;
import android.content.Context;

import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;

/**
 * Created by flatch on 10/11/15.
 */
public class AppWeatherChan extends Application {

    private SQLiteWeatherChan mSQLiteWeatherChan;
    private static Context mContext;

    public void setSQLiteWeatherChan(SQLiteWeatherChan mSQLiteWeatherChan) {
        this.mSQLiteWeatherChan = mSQLiteWeatherChan;
    }

    public SQLiteWeatherChan getSQLiteWeatherChan() {
        return mSQLiteWeatherChan;
    }


    public void onCreate() {
        super.onCreate();
        AppWeatherChan.mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return AppWeatherChan.mContext;
    }
}
