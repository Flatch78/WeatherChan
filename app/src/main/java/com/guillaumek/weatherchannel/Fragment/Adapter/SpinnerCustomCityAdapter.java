package com.guillaumek.weatherchannel.Fragment.Adapter;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.guillaumek.weatherchannel.Activity.MainActivity;
import com.guillaumek.weatherchannel.Fragment.SettingFragment;
import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpinnerCustomCityAdapter extends ArrayAdapter<String> {


    private SQLiteWeatherChan mSQLiteWeatherChan;
    private MainActivity mActivity;
    private List<CityInfoObject> mListCityInObj;
    public Resources mResources;
    CityInfoObject mCityInfoObject = null;
    LayoutInflater mLayoutInflater;
    SettingFragment.ISettingCallback mSettingCallback;

    public SpinnerCustomCityAdapter(MainActivity activity, SettingFragment.ISettingCallback settingCallback,
                                    int textViewResourceId, ArrayList listCityInObj, Resources resLocal) {
        super(activity, textViewResourceId, listCityInObj);

        mActivity = activity;
        mListCityInObj = listCityInObj;
        mResources = resLocal;
        mSettingCallback = settingCallback;

        mSQLiteWeatherChan = ((AppWeatherChan) mActivity.getApplication()).getSQLiteWeatherChan();
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, 1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, 0);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, int type) {


        View view = null;
        if (mListCityInObj.size() > position) {
            view = mLayoutInflater.inflate(R.layout.adapter_spinner_custom_city, parent, false);

            mCityInfoObject = null;
            mCityInfoObject = mListCityInObj.get(position);

            TextView name = (TextView) view.findViewById(R.id.textViewCityName);
            Button buttonRemove = (Button) view.findViewById(R.id.buttonRemoveCity);

            name.setText(mCityInfoObject.getName());
            buttonRemove.setClickable(type == 1);
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "Remove city " + mCityInfoObject.getName(), Toast.LENGTH_SHORT).show();
                    mSQLiteWeatherChan.deleteCity(mCityInfoObject.getId());
                    mSettingCallback.cbUpdateAdapterSpinnerCity();
                }
            });
        }

        return view;
    }


}
