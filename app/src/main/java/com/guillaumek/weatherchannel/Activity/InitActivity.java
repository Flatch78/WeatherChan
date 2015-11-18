package com.guillaumek.weatherchannel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Global.WeatherChannel;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class InitActivity extends AppCompatActivity {

    Future<File> downloading;
    SQLiteWeatherChan mSQLiteWeatherChan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);


        {
            mSQLiteWeatherChan = new SQLiteWeatherChan(this);
            if (mSQLiteWeatherChan.getCityCount() == 0) {
                Log.e("TEST", "fill in DB");
                CityInfoObject cityInfoObject = new CityInfoObject();
                cityInfoObject.setName("Dublin");
                cityInfoObject.setFavorite(1);
                mSQLiteWeatherChan.addCity(cityInfoObject);
            }
            ((AppWeatherChan)getApplication()).setSQLiteWeatherChan(mSQLiteWeatherChan);
        }


//        String[] locales = Locale.getISOCountries();
//        CityInfoObject cityInfoObject = new CityInfoObject();
//        for (String countryCode : locales) {
//            Locale obj = new Locale("en", countryCode);
//
//            System.out.println("Country Name = " + obj.getDisplayCountry(Locale.ENGLISH));
//        }

//        if (!WeatherChannel.EXT_DIRECTORY.exists()) {
//            WeatherChannel.EXT_DIRECTORY.mkdir();
//            getFileListCity();
//        } else {
//            fillBDByFile();
            startMainActivity();
//        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void getFileListCity() {

        if (!WeatherChannel.EXT_FILE_LIST_CITY.exists()) {

            downloading = Ion.with(this)
                    .load("http://openweathermap.org/help/city_list.txt")
                    .write(WeatherChannel.EXT_FILE_LIST_CITY)
                    .setCallback(new FutureCallback<File>() {
                        @Override
                        public void onCompleted(Exception e, File result) {
                            resetDownload();
                            if (e != null) {
                                Log.e("Download file", "error: " + e.getMessage());
                                return;
                            } else if (result == null || !result.exists()) {
                                Log.e("Download file", "error: result null");
                            } else {
                                Log.i("Download", "File list_city DONE");
                                fillBDByFile();
                                startMainActivity();
                            }
                        }
                    });
        }
    }

    void resetDownload() {
        downloading.cancel();
        downloading = null;
    }

    public boolean fillBDByFile() {


/*
        String[] locales = Locale.getISOCountries();
        CityInfoObject cityInfoObject = new CityInfoObject();
        for (String countryCode : locales) {
            Locale obj = new Locale("en", countryCode);

//            cityInfoObject.setId(Integer.valueOf(separated[0]));
            cityInfoObject.setName("");
            cityInfoObject.setLatitude(0);
            cityInfoObject.setLongitude(0);
            cityInfoObject.setCountry(obj.getDisplayCountry(Locale.ENGLISH));
            cityInfoObject.setFavorite(0);

            mSQLiteWeatherChan.addCity(cityInfoObject);
//            System.out.println("Country Name = " + obj.getDisplayCountry(Locale.ENGLISH));
        }
*/

        File fileListCity = WeatherChannel.EXT_FILE_LIST_CITY;
        if (fileListCity.exists()) {
            FileInputStream is;
            BufferedReader reader;

            try {
                is = new FileInputStream(fileListCity);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = reader.readLine();
                Log.e("test", "go fill in");

                CityInfoObject cityInfoObject = new CityInfoObject();
                while (line != null) {
                    if ((line = reader.readLine()) != null) {
                        String[] separated = line.split("\\t");
                        if (separated[4].contains("IE")) {
                            Log.e("test", "rst: " + separated[4]);
                            cityInfoObject.setId(Integer.valueOf(separated[0]));
                            cityInfoObject.setName(separated[1]);
                            cityInfoObject.setLatitude(Double.valueOf(separated[2]));
                            cityInfoObject.setLongitude(Double.valueOf(separated[3]));
                            cityInfoObject.setCountry(separated[4]);
                            cityInfoObject.setFavorite(0);

                            mSQLiteWeatherChan.addCity(cityInfoObject);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


}
