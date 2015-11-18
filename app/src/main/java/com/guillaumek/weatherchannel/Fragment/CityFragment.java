package com.guillaumek.weatherchannel.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guillaumek.weatherchannel.Network.CurrentWeather;
import com.guillaumek.weatherchannel.Network.NetworkAPIWeather;
import com.guillaumek.weatherchannel.Network.Object.UVObject;
import com.guillaumek.weatherchannel.Network.Object.WeatherObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.MessageTool;
import com.guillaumek.weatherchannel.Tools.Tools;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {

    private static final String TAG = CityFragment.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    // SharedPreferences
    SharedPreferences mSharedPreferences;
    public static final String SETTING_PREFS = "settingPrefs" ;
    public static final String HumidityKey = "humiKey";
    public static final String WindKey = "windKey";
    public static final String PressureKey = "pressKey";
    public static final String CloudsKey = "cloudsKey";
    public static final String UVKey = "uvKey";


    private Context mContext;
    private CurrentWeather mCurrentWeather;
    private TextView mTextViewUV;
    ImageView mImageViewUV;

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(Context context, CurrentWeather currentWeather) {
        CityFragment fragment = new CityFragment();
        fragment.setCurrentWeather(currentWeather);
        fragment.setContext(context);
        return fragment;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        mCurrentWeather = currentWeather;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = mContext.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        if (mCurrentWeather != null && mCurrentWeather.name != null) {
            ((TextView) view.findViewById(R.id.textViewCity)).setText(mCurrentWeather.name);
            ((TextView) view.findViewById(R.id.textViewCountry)).setText(mCurrentWeather.sys.country);
            String description = "";
            String icon = "";
            for (WeatherObject weather : mCurrentWeather.weather) {
                description += weather.description + "\n";
                icon = weather.icon;
            }
            ((TextView) view.findViewById(R.id.textViewDescription)).setText(description);
            {
                ((TextView) view.findViewById(R.id.textViewHumidity)).setText(String.format("%d%%", mCurrentWeather.main.humidity));
                view.findViewById(R.id.layoutHumidity).setVisibility(mSharedPreferences.getBoolean(HumidityKey, true) ? View.VISIBLE : View.GONE);
            }
            {
                ((TextView) view.findViewById(R.id.textViewWind)).setText(String.format("%.2f m/!s", mCurrentWeather.wind.speed));
                view.findViewById(R.id.layoutWind).setVisibility(mSharedPreferences.getBoolean(WindKey, true) ? View.VISIBLE : View.GONE);
            }
            {
                ((TextView) view.findViewById(R.id.textViewPressure)).setText(String.format("%.2fPa", mCurrentWeather.main.pressure));
                view.findViewById(R.id.layoutPressure).setVisibility(mSharedPreferences.getBoolean(PressureKey, true) ? View.VISIBLE : View.GONE);
            }
            {
                ((TextView) view.findViewById(R.id.textViewClouds)).setText(String.format("%d%%", mCurrentWeather.clouds.all));
                view.findViewById(R.id.layoutClouds).setVisibility(mSharedPreferences.getBoolean(CloudsKey, true) ? View.VISIBLE : View.GONE);
            }
            {
                mImageViewUV = (ImageView) view.findViewById(R.id.imageViewUV);
                mTextViewUV = (TextView) view.findViewById(R.id.textViewUV);
                view.findViewById(R.id.layoutUV).setVisibility(mSharedPreferences.getBoolean(UVKey, true) ? View.VISIBLE : View.GONE);
                getUVWeather();
            }
            ((TextView) view.findViewById(R.id.textViewTemperature)).setText(String.format("%.2f°C", Tools.getCelsiusValue(mCurrentWeather.main.temp)));


            DateFormat dateFormat = SimpleDateFormat.getTimeInstance(); //new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String lastUpdate = "";
            try {
                Date date = new Date();
                lastUpdate = dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ((TextView) view.findViewById(R.id.textViewUpdate)).setText(lastUpdate);


            String URLIcon = NetworkAPIWeather.getURLWeatherIcon(icon);
            Ion.with(getActivity())
                    .load(URLIcon)
                    .withBitmap()
                    .intoImageView((ImageView) view.findViewById(R.id.imageViewWeather));
        }
        return view;
    }

    private void iconUV(double value) {
        if (value < 2.9) {
            mImageViewUV.setImageResource(R.mipmap.ic_warning_low);
        } else if (value < 5.9) {
            mImageViewUV.setImageResource(R.mipmap.ic_warning_moderate);
        } else if (value < 7.9) {
            mImageViewUV.setImageResource(R.mipmap.ic_warning_high);
        } else if (value < 10.9) {
            mImageViewUV.setImageResource(R.mipmap.ic_warning_very_high);
        } else {
            mImageViewUV.setImageResource(R.mipmap.ic_warning_extreme);
        }
    }

    private void getUVWeather() {
        String pathURI = NetworkAPIWeather.getURLCurrentUVCoord(mCurrentWeather.coord.lat, mCurrentWeather.coord.lon);
        msg.MsgDebug("URL: " + pathURI);
        Ion.with(mContext)
                .load(pathURI)
                .as(UVObject.class)
                .setCallback(new FutureCallback<UVObject>() {
                    @Override
                    public void onCompleted(Exception e, UVObject result) {
                        if (e != null) {
                            msg.MsgError("Error: " + e.getMessage());
                        } else if (result == null) {
                            msg.MsgError("Error: request fail");
                        } else {
                            mTextViewUV.setText(String.format("%.2f", result.value));
                            iconUV(result.value);
                        }
                    }

                });

    }

}
