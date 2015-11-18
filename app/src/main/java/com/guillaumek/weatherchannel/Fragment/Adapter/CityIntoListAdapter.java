package com.guillaumek.weatherchannel.Fragment.Adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityIntoListAdapter extends ArrayAdapter<CityInfoObject> {

    private List<CityInfoObject> mListCityInfoObject;
    private Context mContext;

    public CityIntoListAdapter(Context context, List<CityInfoObject> listCityInfoObject) {
        super(context, R.layout.adapter_city_into_list, listCityInfoObject);
        mContext = context;
        mListCityInfoObject = listCityInfoObject;
        for (CityInfoObject cityInfoObject : listCityInfoObject) {
            Log.e("cityInfoObject", "city: " + cityInfoObject.getName());
        }
    }

    public void setListCityInfoObject(List<CityInfoObject> listCityInfoObject) {
        mListCityInfoObject = listCityInfoObject;
    }

    static class ViewHolder {
        protected TextView textCity;
        protected TextView textCountry;
        protected Button delFav;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {

        final int id = (int)mListCityInfoObject.get(position).getId();
            LayoutInflater inflator = ((Activity) mContext).getLayoutInflater();
            View view = inflator.inflate(R.layout.adapter_city_into_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textCity = (TextView) view.findViewById(R.id.textViewCity);
            viewHolder.textCountry = (TextView) view.findViewById(R.id.textViewCountry);
            viewHolder.delFav = (Button) view.findViewById(R.id.buttonDelete);
            viewHolder.delFav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((AppWeatherChan)((Activity)mContext).getApplication()).getSQLiteWeatherChan().deleteCity(id);
                            notifyDataSetChanged();
                        }
                    });
            view.setTag(viewHolder);
            convertView = view;
//        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.textCity.setText(mListCityInfoObject.get(position).getName());
            holder.textCountry.setText(mListCityInfoObject.get(position).getCountry());
//        }
        return convertView;
    }
}
