package com.guillaumek.weatherchannel.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.guillaumek.weatherchannel.Activity.MainActivity;
import com.guillaumek.weatherchannel.Network.CurrentWeather;
import com.guillaumek.weatherchannel.Network.ForecastWeather;
import com.guillaumek.weatherchannel.Network.NetworkAPIWeather;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.MessageTool;
import com.guillaumek.weatherchannel.Tools.WeatherGPSTracker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    private View mView;
    private Context mContext;

    LinearLayout mLinearLayoutBack;

    // SharedPreferences
    SharedPreferences mSharedPreferences;
    public static final String SETTING_PREFS = "settingPrefs" ;
    public static final String GraphicKey = "graphicKey";


    private ScreenSlidePagerCityAdapter mScreenSlidePagerCityAdapter;
    private ScreenSlidePagerGraphAdapter mScreenSlidePagerGraphAdapter;

    private WeatherGPSTracker mWeatherGPSTracker;

    private List<CurrentWeather> mListCurrentWeather;
    private ForecastWeather mForecastWeather;

    public MainFragment() {
    }

    public static MainFragment newInstance(Context context) {
        MainFragment fragment = new MainFragment();
        fragment.setContext(context);
        return fragment;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mWeatherGPSTracker = new WeatherGPSTracker(mContext);
        mListCurrentWeather = new ArrayList<>();
        mForecastWeather = null;
        mSharedPreferences = getActivity().getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
        setLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_main, container, false);


        mLinearLayoutBack = (LinearLayout) mView.findViewById(R.id.layoutBackground);


        ViewPager viewPagerCities = (ViewPager) mView.findViewById(R.id.pagerCities);
        mScreenSlidePagerCityAdapter = new ScreenSlidePagerCityAdapter(((MainActivity)mContext).getSupportFragmentManager());
        viewPagerCities.setAdapter(mScreenSlidePagerCityAdapter);

        ViewPager viewPagerChart = (ViewPager) mView.findViewById(R.id.contentGraph);
        if (mSharedPreferences.getBoolean(GraphicKey, true)) {
            viewPagerChart.setVisibility(View.VISIBLE);
            mScreenSlidePagerGraphAdapter = new ScreenSlidePagerGraphAdapter(((MainActivity) mContext).getSupportFragmentManager());
            viewPagerChart.setAdapter(mScreenSlidePagerGraphAdapter);
        } else {
            viewPagerChart.setVisibility(View.GONE);
        }
        doRequest();

        return mView;
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
        String pathURI = NetworkAPIWeather.getURLWeatherCoord(mWeatherGPSTracker.getLatitude(), mWeatherGPSTracker.getLongitude());
        requestWeather(pathURI);
        pathURI = NetworkAPIWeather.getURLForecastCoord(mWeatherGPSTracker.getLatitude(), mWeatherGPSTracker.getLongitude());
        requestForecast(pathURI);
    }

    private void requestWeather(final String pathURI) {

        msg.MsgDebug("URL: " + pathURI);

        Ion.with(mContext)
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
                                mListCurrentWeather.add(result);
                            }
                            if (mScreenSlidePagerCityAdapter != null) {
                                mScreenSlidePagerCityAdapter.notifyDataSetChanged();
                            }
                            setBackGroundImage(result.weather.get(0).icon);
                        }
                    }

                });
    }

    private void setBackGroundImage(String icon) {
        String URLBackground = NetworkAPIWeather.getURLbyIconName(icon);
        if (URLBackground != null && mContext != null) {
            Ion.with(mContext)
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

        msg.MsgDebug("URL: " + pathURI);

        Ion.with(mContext)
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
                return CityFragment.newInstance(mContext, mListCurrentWeather.get(position));
            }
            return CityFragment.newInstance(mContext, null);
        }

        @Override
        public int getItemPosition(Object object){
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
                return ChartFragment.newInstance(mContext, mForecastWeather, position);
            }
            return ChartFragment.newInstance(mContext, null, position);
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

}
