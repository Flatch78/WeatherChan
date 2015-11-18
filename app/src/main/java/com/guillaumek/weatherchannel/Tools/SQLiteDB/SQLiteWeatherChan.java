package com.guillaumek.weatherchannel.Tools.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.guillaumek.weatherchannel.Network.CurrentWeather;
import com.guillaumek.weatherchannel.Network.NetworkAPIWeather;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.Tools.MessageTool;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by flatch on 09/11/15.
 */
public class SQLiteWeatherChan extends SQLiteOpenHelper {


    Context mContext;

    private static final String TAG = SQLiteWeatherChan.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database
    private static final String DATABASE_NAME = "WeatherChanDB.db";

    // Table
    private static final String TABLE_CITY = "list_city";

    // Columns
    private static final String COL_ID = "ID";
    private static final String COL_CREATED_AT = "CREATED_AT";
    private static final String COL_NAME = "NAME";
    private static final String COL_COUNTRY = "COUNTRY";

    private static final String COL_LAT = "LAT";
    private static final String COL_LON = "LON";

    private static final String COL_FAVORITE = "FAV";

    // list city table Create Statements
    private static final String CREATE_TABLE_LIST = "CREATE TABLE " + TABLE_CITY
            + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT,"
            + COL_COUNTRY + " TEXT,"
            + COL_LAT + " DOUBLE,"
            + COL_LON + " DOUBLE,"
            + COL_FAVORITE + " INTEGER,"
            + COL_CREATED_AT + " DATETIME"
            + ")";

    public SQLiteWeatherChan(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        onCreate(db);
    }

    public long addCity(final CityInfoObject city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(COL_ID, city.getId());
        values.put(COL_NAME, city.getName());
//        values.put(COL_LAT, city.getLatitude());
//        values.put(COL_LON, city.getLongitude());
//        values.put(COL_COUNTRY, city.getCountry());
        values.put(COL_FAVORITE, city.getFavorite());
        values.put(COL_CREATED_AT, getDateTime());
        city.setId((int) db.insert(TABLE_CITY, null, values));

        Ion.with(mContext)
                .load(NetworkAPIWeather.getURLWeatherNameCity(city.getName()))
                .as(CurrentWeather.class)
                .setCallback(new FutureCallback<CurrentWeather>() {
                    @Override
                    public void onCompleted(Exception e, CurrentWeather result) {
                        if (e != null) {
                            Log.e("Request", "error: " + e.getMessage());
                        } else if (result == null) {
                            Log.e("Request", "error: request fail");
                        } else {
                            city.setLatitude(result.coord.lat);
                            city.setLongitude(result.coord.lon);
                            city.setCountry(new Locale("en", result.sys.country).getDisplayCountry());
                            updateCity(city);
                        }
                    }

                });

        return 0;
    }

    public CityInfoObject getCity(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM "
                + TABLE_CITY + " WHERE "
                + COL_ID + " = " + id;

        Log.e(TAG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        CityInfoObject cityInfoObject = new CityInfoObject();
        cityInfoObject.setId((int) id);
        cityInfoObject.setName((cursor.getString(cursor.getColumnIndex(COL_NAME))));
        cityInfoObject.setLatitude((cursor.getDouble(cursor.getColumnIndex(COL_LAT))));
        cityInfoObject.setLongitude((cursor.getDouble(cursor.getColumnIndex(COL_LON))));
        cityInfoObject.setCountry((cursor.getString(cursor.getColumnIndex(COL_COUNTRY))));
        cityInfoObject.setFavorite((cursor.getInt(cursor.getColumnIndex(COL_FAVORITE))));
        cityInfoObject.setCreated_at(cursor.getString(cursor.getColumnIndex(COL_CREATED_AT)));

        return cityInfoObject;
    }

    public List<CityInfoObject> getAllCities() {
        List<CityInfoObject> listCityInfoObject = new ArrayList<CityInfoObject>();
        String selectQuery = "SELECT  * FROM " + TABLE_CITY;

        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CityInfoObject cityInfoObject = new CityInfoObject();
                cityInfoObject.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                cityInfoObject.setName((cursor.getString(cursor.getColumnIndex(COL_NAME))));
                cityInfoObject.setLatitude((cursor.getDouble(cursor.getColumnIndex(COL_LAT))));
                cityInfoObject.setLongitude((cursor.getDouble(cursor.getColumnIndex(COL_LON))));
                cityInfoObject.setCountry((cursor.getString(cursor.getColumnIndex(COL_COUNTRY))));
                cityInfoObject.setFavorite((cursor.getInt(cursor.getColumnIndex(COL_FAVORITE))));
                cityInfoObject.setCreated_at(cursor.getString(cursor.getColumnIndex(COL_CREATED_AT)));

                listCityInfoObject.add(cityInfoObject);
            } while (cursor.moveToNext());
        }

        return listCityInfoObject;
    }

    public List<CityInfoObject> getAllCityByCountry(String country) {
        List<CityInfoObject> listCityInfoObject = new ArrayList<CityInfoObject>();

        String selectQuery = "SELECT  * FROM " + TABLE_CITY
                + " WHERE " + COL_COUNTRY + " = '" + country;

        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CityInfoObject cityInfoObject = new CityInfoObject();
                cityInfoObject.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                cityInfoObject.setName((cursor.getString(cursor.getColumnIndex(COL_NAME))));
                cityInfoObject.setLatitude((cursor.getDouble(cursor.getColumnIndex(COL_LAT))));
                cityInfoObject.setLongitude((cursor.getDouble(cursor.getColumnIndex(COL_LON))));
                cityInfoObject.setCountry((cursor.getString(cursor.getColumnIndex(COL_COUNTRY))));
                cityInfoObject.setFavorite((cursor.getInt(cursor.getColumnIndex(COL_FAVORITE))));
                cityInfoObject.setCreated_at(cursor.getString(cursor.getColumnIndex(COL_CREATED_AT)));

                listCityInfoObject.add(cityInfoObject);
            } while (cursor.moveToNext());
        }

        return listCityInfoObject;
    }

    public List<CityInfoObject> getAllCityByFavorite(boolean isFavorite) {
        List<CityInfoObject> listCityInfoObject = new ArrayList<CityInfoObject>();

        String selectQuery = "SELECT  * FROM " + TABLE_CITY
                + " WHERE " + COL_FAVORITE + " = '" + (isFavorite ? 1 : 0);

        Log.e(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CityInfoObject cityInfoObject = new CityInfoObject();
                cityInfoObject.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                cityInfoObject.setName((cursor.getString(cursor.getColumnIndex(COL_NAME))));
                cityInfoObject.setLatitude((cursor.getDouble(cursor.getColumnIndex(COL_LAT))));
                cityInfoObject.setLongitude((cursor.getDouble(cursor.getColumnIndex(COL_LON))));
                cityInfoObject.setCountry((cursor.getString(cursor.getColumnIndex(COL_COUNTRY))));
                cityInfoObject.setFavorite((cursor.getInt(cursor.getColumnIndex(COL_FAVORITE))));
                cityInfoObject.setCreated_at(cursor.getString(cursor.getColumnIndex(COL_CREATED_AT)));

                listCityInfoObject.add(cityInfoObject);
            } while (cursor.moveToNext());
        }

        return listCityInfoObject;
    }

    public int getCityCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CITY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateCity(CityInfoObject cityInfoObject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, cityInfoObject.getName());
        values.put(COL_LAT, cityInfoObject.getLatitude());
        values.put(COL_LON, cityInfoObject.getLongitude());
        values.put(COL_COUNTRY, cityInfoObject.getCountry());
        values.put(COL_FAVORITE, cityInfoObject.getFavorite());

        return db.update(TABLE_CITY, values, COL_ID + " = ?", new String[] { String.valueOf(cityInfoObject.getId()) });
    }

    public void deleteCity(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CITY, COL_ID + " = ?", new String[] { String.valueOf(id) });
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}


