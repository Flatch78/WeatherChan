package com.guillaumek.weatherchannel.Fragment.Dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.guillaumek.weatherchannel.Fragment.Adapter.CityIntoListAdapter;
import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;

/**
 * Created by flatch on 09/11/15.
 */
public class ListCityDialogFragment extends DialogFragment {

    SQLiteWeatherChan mSQLiteWeatherChan;
    CityIntoListAdapter mCityIntoListAdapter;

    public static ListCityDialogFragment newInstance() {
        return new ListCityDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSQLiteWeatherChan = ((AppWeatherChan)getActivity().getApplication()).getSQLiteWeatherChan();
        View view = inflater.inflate(R.layout.dialogfragment_list_city, container,
                false);
        getDialog().setTitle("DialogFragment Tutorial");

        final EditText editText = (EditText)view.findViewById(R.id.editTextAddCity);
        Button button = (Button)view.findViewById(R.id.buttonAddBity);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityInfoObject cityInfoObject = new CityInfoObject();
                cityInfoObject.setName(editText.getText().toString());
                cityInfoObject.setFavorite(1);
                mSQLiteWeatherChan.addCity(cityInfoObject);
                mCityIntoListAdapter.setListCityInfoObject(mSQLiteWeatherChan.getAllCities());
                mCityIntoListAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        ListView listView = (ListView)view.findViewById(R.id.listViewCity);
        mCityIntoListAdapter = new CityIntoListAdapter(getActivity(), mSQLiteWeatherChan.getAllCities());
        listView.setAdapter(mCityIntoListAdapter);

        return view;
    }
}
