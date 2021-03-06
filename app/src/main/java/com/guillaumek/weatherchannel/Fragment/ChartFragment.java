package com.guillaumek.weatherchannel.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.guillaumek.weatherchannel.Network.ForecastWeather;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.ContentChart;
import com.guillaumek.weatherchannel.Tools.GraphEnum;
import com.guillaumek.weatherchannel.Tools.MessageTool;
import com.guillaumek.weatherchannel.Tools.Tools;
import com.guillaumek.weatherchannel.Tools.WeatherAChartEngine;

import org.achartengine.GraphicalView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    private static final String TAG = ChartFragment.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    SharedPreferences mSharedPreferences;
    public static final String SETTING_PREFS = "settingPrefs" ;
    public static final String nbDaysKey = "nbDaysKey";

    private Context mContext;
    private ForecastWeather mForecastWeather;
    private GraphEnum mType;
    private int mNumberDays;

    public ChartFragment() {
        // Required empty public constructor
    }

    public static ChartFragment newInstance(Context context, ForecastWeather forecastWeather, GraphEnum type) {
        ChartFragment fragment = new ChartFragment();
        fragment.setForecastWeather(forecastWeather);
        fragment.setType(type);
        fragment.setContext(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = mContext.getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        mNumberDays = mSharedPreferences.getInt(nbDaysKey, 0) + 1;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setType(GraphEnum type) {
        mType = type;
    }

    public void setForecastWeather(ForecastWeather forecastWeather) {
        mForecastWeather = forecastWeather;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);

        if (mForecastWeather != null) {
            FrameLayout frameLayoutChart = (FrameLayout) view.findViewById(R.id.contentGraph);

            WeatherAChartEngine weatherAChartEngine = null;
            List<Double> listElements = null;
            ContentChart contentChart = null;
            if (mType == GraphEnum.TEMPERATURE) {
                contentChart = new ContentChart(0, 30);
                listElements = getListTemperature(mForecastWeather.list, contentChart);
                weatherAChartEngine = new WeatherAChartEngine(mContext, mContext.getString(R.string.graphic_temperature_title),
                        mContext.getString(R.string.graphic_x_title), mContext.getString(R.string.graphic_temperature_y_title));
                weatherAChartEngine.setLineColor(getResources().getColor(R.color.FireBrick));
            } else if (mType == GraphEnum.HUMIDITY) {
                contentChart = new ContentChart(50, 110);
                listElements = getListHumidity(mForecastWeather.list, contentChart);
                weatherAChartEngine = new WeatherAChartEngine(mContext, mContext.getString(R.string.graphic_humidity_title),
                        mContext.getString(R.string.graphic_x_title), mContext.getString(R.string.graphic_percentage_y_title));
                weatherAChartEngine.setLineColor(getResources().getColor(R.color.colorPrimaryDark));
            } else if (mType == GraphEnum.PRESSURE) {
                contentChart = new ContentChart(940, 1050);
                listElements = getListPressure(mForecastWeather.list, contentChart);
                weatherAChartEngine = new WeatherAChartEngine(mContext, mContext.getString(R.string.graphic_pressure_title),
                        mContext.getString(R.string.graphic_x_title), mContext.getString(R.string.graphic_percentage_y_title));
                weatherAChartEngine.setLineColor(getResources().getColor(R.color.black));
            } else if (mType == GraphEnum.WIND) {
                contentChart = new ContentChart(0, 150);
                listElements = getListWindSpeed(mForecastWeather.list, contentChart);
                weatherAChartEngine = new WeatherAChartEngine(mContext, mContext.getString(R.string.graphic_wind_title),
                        mContext.getString(R.string.graphic_x_title), mContext.getString(R.string.graphic_speed_y_title));
                weatherAChartEngine.setLineColor(getResources().getColor(R.color.DimGray));
            } else if (mType == GraphEnum.CLOUDS) {
                contentChart = new ContentChart(0, 100);
                listElements = getListcloud(mForecastWeather.list, contentChart);
                weatherAChartEngine = new WeatherAChartEngine(mContext, mContext.getString(R.string.graphic_cloud_title),
                        mContext.getString(R.string.graphic_x_title), mContext.getString(R.string.graphic_percentage_y_title));
                weatherAChartEngine.setLineColor(getResources().getColor(R.color.Ivory));
            }
            if (weatherAChartEngine != null) {
                GraphicalView graphicalView = weatherAChartEngine.getChart(listElements, contentChart);
                if (graphicalView != null) {
                    frameLayoutChart.addView(graphicalView);
                }
            }
        }

        return view;
    }

    public List<Double> getListTemperature(List<ForecastWeather.FtWeather> ftWeatherList, ContentChart contentChart) {
        List<Double> listTemp = new ArrayList<Double>();
        for (int i = 0; i < ((mNumberDays * 8) <= ftWeatherList.size() ? (mNumberDays * 8) : ftWeatherList.size()); ++i) {
            Double temp = Tools.roundDouble(Tools.getCelsiusValue(ftWeatherList.get(i).main.temp), 2);
            listTemp.add(temp);
            if (temp < contentChart.getMin()) {
                contentChart.setMin(temp.intValue() - 10);
            }
            if (temp > contentChart.getMax()) {
                contentChart.setMax(temp.intValue() + 10);
            }
        }
        return listTemp;
    }

    public List<Double> getListHumidity(List<ForecastWeather.FtWeather> ftWeatherList, ContentChart contentChart) {
        List<Double> listTemp = new ArrayList<Double>();
        for (int i = 0; i < ((mNumberDays * 8) <= ftWeatherList.size() ? (mNumberDays * 8) : ftWeatherList.size()); ++i) {
            Double humid = Tools.roundDouble(ftWeatherList.get(i).main.humidity, 2);
            listTemp.add(humid);
            if (humid < contentChart.getMin()) {
                contentChart.setMin(humid.intValue() - 10);
            }
            if (humid > contentChart.getMax()) {
                contentChart.setMax(humid.intValue() + 10);
            }
        }
        return listTemp;
    }

    public List<Double> getListPressure(List<ForecastWeather.FtWeather> ftWeatherList, ContentChart contentChart) {
        List<Double> listTemp = new ArrayList<Double>();
        for (int i = 0; i < ((mNumberDays * 8) <= ftWeatherList.size() ? (mNumberDays * 8) : ftWeatherList.size()); ++i) {
            Double press = Tools.roundDouble(ftWeatherList.get(i).main.pressure, 2);
            listTemp.add(press);
            if (press < contentChart.getMin()) {
                contentChart.setMin(press.intValue() - 10);
            }
            if (press > contentChart.getMax()) {
                contentChart.setMax(press.intValue() + 10);
            }
        }
        return listTemp;
    }

    public List<Double> getListWindSpeed(List<ForecastWeather.FtWeather> ftWeatherList, ContentChart contentChart) {
        List<Double> listTemp = new ArrayList<Double>();
        for (int i = 0; i < ((mNumberDays * 8) <= ftWeatherList.size() ? (mNumberDays * 8) : ftWeatherList.size()); ++i) {
            Double windSpeed = Tools.roundDouble(ftWeatherList.get(i).wind.speed * 3.6, 2);
            listTemp.add(windSpeed);
            if (windSpeed < contentChart.getMin()) {
                contentChart.setMin(windSpeed.intValue() - 10);
            }
            if (windSpeed > contentChart.getMax()) {
                contentChart.setMax(windSpeed.intValue() + 10);
            }
        }
        return listTemp;
    }

    public List<Double> getListcloud(List<ForecastWeather.FtWeather> ftWeatherList, ContentChart contentChart) {
        List<Double> listTemp = new ArrayList<Double>();
        for (int i = 0; i < ((mNumberDays * 8) <= ftWeatherList.size() ? (mNumberDays * 8) : ftWeatherList.size()); ++i) {
            Double clouds = Tools.roundDouble(ftWeatherList.get(i).clouds.all, 2);
            listTemp.add(clouds);
            if (clouds < contentChart.getMin()) {
                contentChart.setMin(clouds.intValue() - 10);
            }
            if (clouds > contentChart.getMax()) {
                contentChart.setMax(clouds.intValue() + 10);
            }
        }
        return listTemp;
    }

}
