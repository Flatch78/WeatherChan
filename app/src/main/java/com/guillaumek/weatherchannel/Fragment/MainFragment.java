package com.guillaumek.weatherchannel.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.guillaumek.weatherchannel.Activity.MainActivity;
import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Network.CurrentWeather;
import com.guillaumek.weatherchannel.Network.ForecastWeather;
import com.guillaumek.weatherchannel.Network.NetworkAPIWeather;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.GraphEnum;
import com.guillaumek.weatherchannel.Tools.MessageTool;
import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;
import com.guillaumek.weatherchannel.Tools.WeatherGPSTracker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    private View mView;
    private MainActivity mActivity;
    private int mNBRequest;

    private LinearLayout mLinearLayoutBack;
    private ProgressBar mProgressBar;

    // SharedPreferences
    private SharedPreferences mSharedPreferences;
    public static final String SETTING_PREFS = "settingPrefs";
    public static final String GraphicKey = "graphicKey";
    public static final String HumidityKey = "humiKey";
    public static final String WindKey = "windKey";
    public static final String PressureKey = "pressKey";
    public static final String CloudsKey = "cloudsKey";
    public static final String UVKey = "uvKey";


    private ScreenSlidePagerCityAdapter mScreenSlidePagerCityAdapter;
    private ScreenSlidePagerGraphAdapter mScreenSlidePagerGraphAdapter;

    private ViewPager mViewPagerCities;
    private ViewPager mViewPagerChart;

    private WeatherGPSTracker mWeatherGPSTracker;

    private List<CurrentWeather> mListCurrentWeather;
    private List<GraphEnum> mListGraphEnum;
    private ForecastWeather mForecastWeather;

    private SQLiteWeatherChan mSQLiteWeatherChan;


    public MainFragment() {
        mNBRequest = 0;
        mListGraphEnum = new ArrayList<>();
    }

    public static MainFragment newInstance(MainActivity context) {
        MainFragment fragment = new MainFragment();
        fragment.setContext(context);
        return fragment;
    }

    public void setContext(MainActivity context) {
        mActivity = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mWeatherGPSTracker = new WeatherGPSTracker(mActivity);
        mListCurrentWeather = new ArrayList<>();
        mForecastWeather = null;
        mSharedPreferences = getActivity().getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        mSQLiteWeatherChan = ((AppWeatherChan) mActivity.getApplication()).getSQLiteWeatherChan();
        setLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fillInListGraph();

        mView = inflater.inflate(R.layout.fragment_main, container, false);

        mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBarLoading);

        mLinearLayoutBack = (LinearLayout) mView.findViewById(R.id.layoutBackground);

        mViewPagerCities = (ViewPager) mView.findViewById(R.id.pagerCities);
        mViewPagerCities.setVisibility(View.GONE);
        mScreenSlidePagerCityAdapter = new ScreenSlidePagerCityAdapter((mActivity).getSupportFragmentManager());
        mViewPagerCities.setAdapter(mScreenSlidePagerCityAdapter);
        mViewPagerCities.addOnPageChangeListener(new PagerCityAdapterOnPageChangeListener());

        mViewPagerChart = (ViewPager) mView.findViewById(R.id.pagerGraph);
        mViewPagerChart.setVisibility(View.GONE);
        if (mSharedPreferences.getBoolean(GraphicKey, true)) {
            mViewPagerChart.setVisibility(View.VISIBLE);
            mScreenSlidePagerGraphAdapter = new ScreenSlidePagerGraphAdapter((mActivity).getSupportFragmentManager());
            mViewPagerChart.setAdapter(mScreenSlidePagerGraphAdapter);
        } else {
            mViewPagerChart.setVisibility(View.GONE);
        }
        doRequest();

        return mView;
    }

    public void fillInListGraph() {
        mListGraphEnum.clear();
        mListGraphEnum.add(GraphEnum.TEMPERATURE);
        if (mSharedPreferences.getBoolean(HumidityKey, true)) {
            mListGraphEnum.add(GraphEnum.HUMIDITY);
        }
        if (mSharedPreferences.getBoolean(WindKey, true)) {
            mListGraphEnum.add(GraphEnum.WIND);
        }
        if (mSharedPreferences.getBoolean(CloudsKey, true)) {
            mListGraphEnum.add(GraphEnum.CLOUDS);
        }
        if (mSharedPreferences.getBoolean(PressureKey, true)) {
            mListGraphEnum.add(GraphEnum.PRESSURE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                doRequest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean setLocation() {

        if (mWeatherGPSTracker.canGetLocation()) {
            return true;
        }
        mWeatherGPSTracker.showSettingsAlert();

        return false;
    }

    private void doRequest() {
        mViewPagerCities.setVisibility(View.GONE);
        mViewPagerChart.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (mNBRequest <= 0) {
            ++mNBRequest;
            mListCurrentWeather.clear();
            mScreenSlidePagerCityAdapter.notifyDataSetChanged();
            String pathURI = NetworkAPIWeather.getURLWeatherCoord(mWeatherGPSTracker.getLatitude(), mWeatherGPSTracker.getLongitude());
            requestWeather(pathURI);
            pathURI = NetworkAPIWeather.getURLForecastCoord(mWeatherGPSTracker.getLatitude(), mWeatherGPSTracker.getLongitude());
            requestForecast(pathURI);
            Handler handler = new Handler();
            if (mSQLiteWeatherChan != null) {
                List<CityInfoObject> listCityInfObj = mSQLiteWeatherChan.getAllCities();
                for (final CityInfoObject cityInfObj : listCityInfObj) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            String pathURIwe = NetworkAPIWeather.getURLWeatherCoord(cityInfObj.getLatitude(), cityInfObj.getLongitude());
                            requestWeather(pathURIwe);
                            ++mNBRequest;
                        }
                    }, 500);
                }
            }
        }
    }

    private void requestWeather(final String pathURI) {

        Ion.with(mActivity)
                .load(pathURI)
                .as(CurrentWeather.class)
                .setCallback(new FutureCallback<CurrentWeather>() {
                    @Override
                    public void onCompleted(Exception e, CurrentWeather result) {
                        if (e != null) {
                            msg.MsgError("Error: " + e.getMessage());
                            requestWeather(pathURI);
                        } else if (result == null) {
                            msg.MsgError("Error: request fail");
                        } else {
                            msg.MsgDebug("Request: " + pathURI + " Done");
                            if (mListCurrentWeather != null) {
                                mListCurrentWeather.add(mListCurrentWeather.size(), result);
                            }
                            if (mScreenSlidePagerCityAdapter != null) {
                                mScreenSlidePagerCityAdapter.notifyDataSetChanged();
                            }
                            if (mListCurrentWeather.size() == 1) {
                                setBackGroundImage(result.weather.get(0).icon);
                            }
                        }
                        --mNBRequest;
                        if (mNBRequest == 0) {
                            mViewPagerCities.setVisibility(View.VISIBLE);
                            mViewPagerChart.setVisibility(View.VISIBLE);
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                });
    }

    private void setBackGroundImage(String icon) {
        String URLBackground = NetworkAPIWeather.getURLbyIconName(icon);
        if (URLBackground != null && mActivity != null) {
            Ion.with(mActivity)
                    .load(URLBackground)
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {
                            if (e != null) {
                                msg.MsgError("Error: " + e.getMessage());
                            } else if (result == null) {
                                msg.MsgError("Error: request fail");
                            } else {
                                BitmapDrawable ob = new BitmapDrawable(getResources(), result);
                                mLinearLayoutBack.setBackgroundDrawable(ob);
                            }
                        }

                    });
        }
    }

    private void requestForecast(final String pathURI) {

        Ion.with(mActivity)
                .load(pathURI)
                .as(ForecastWeather.class)
                .setCallback(new FutureCallback<ForecastWeather>() {
                    @Override
                    public void onCompleted(Exception e, ForecastWeather result) {
                        if (e != null && e.getMessage() != null) {
                            msg.MsgError("Error: " + e.getMessage());
                        } else if (result == null) {
                            msg.MsgError("Error: request fail");
                        } else {
                            msg.MsgDebug("Request: " + pathURI + " Done");
                            mForecastWeather = result;
                            if (mScreenSlidePagerGraphAdapter != null) {
                                mScreenSlidePagerGraphAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ScreenSlidePagerCityAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerCityAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mListCurrentWeather != null && mListCurrentWeather.size() > position) {
                return CityFragment.newInstance(mActivity, mListCurrentWeather.get(position));
            }
            return CityFragment.newInstance(mActivity, null);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mListCurrentWeather.size() == 0 ? 1 : mListCurrentWeather.size(); // Because fail when they are 0 item
        }
    }

    private class ScreenSlidePagerGraphAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerGraphAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (mForecastWeather != null) {
                return ChartFragment.newInstance(mActivity, mForecastWeather, mListGraphEnum.get(position));
            }
            return ChartFragment.newInstance(mActivity, null, GraphEnum.NOTHING);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mListGraphEnum.size();
        }
    }

    public class PagerCityAdapterOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {
            if (mListCurrentWeather != null && mListCurrentWeather.size() > position) {
                setBackGroundImage(mListCurrentWeather.get(position).weather.get(0).icon);
                String pathURI = NetworkAPIWeather.getURLForecastCoord(mListCurrentWeather.get(position).coord.lat, mListCurrentWeather.get(position).coord.lon);
                requestForecast(pathURI);
            }
            currentPage = position;
        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }

}
