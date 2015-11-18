package com.guillaumek.weatherchannel.Fragment.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.guillaumek.weatherchannel.Activity.MainActivity;
import com.guillaumek.weatherchannel.Fragment.SettingFragment;
import com.guillaumek.weatherchannel.Global.AppWeatherChan;
import com.guillaumek.weatherchannel.Network.Object.CityInfoObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.SQLiteDB.SQLiteWeatherChan;

/**
 * Created by flatch on 09/11/15.
 */
public class AddCityDialogFragment extends DialogFragment {

    private SQLiteWeatherChan mSQLiteWeatherChan;
    private MainActivity mMainActivity;
    SettingFragment.ISettingCallback mSettingCallback;

    public static AddCityDialogFragment newInstance(MainActivity mainActivity, SettingFragment.ISettingCallback settingCallback) {

        AddCityDialogFragment fragment = new AddCityDialogFragment();
        fragment.setMainActivity(mainActivity);
        fragment.setSettingCallBack(settingCallback);
        return fragment;
    }

    public void setMainActivity(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }
    public void setSettingCallBack(SettingFragment.ISettingCallback settingCallback) {
        mSettingCallback = settingCallback;
    }

    public AddCityDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSQLiteWeatherChan = ((AppWeatherChan)mMainActivity.getApplication()).getSQLiteWeatherChan();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogfragment_list_city, null);

        final EditText editTextNameCity = (EditText)view.findViewById(R.id.editTextAddCity);
        final EditText editTextLat = (EditText)view.findViewById(R.id.editTextLat);
        final EditText editTextLon = (EditText)view.findViewById(R.id.editTextLon);

        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                .setTitle("Add new city")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String nameCity = editTextNameCity.getText().toString();
                                if (nameCity.length() > 0) {
                                    CityInfoObject cityInfoObject = new CityInfoObject();
                                    cityInfoObject.setName(nameCity);

                                    String textLat = editTextLat.getText().toString();
                                    String textLon = editTextLon.getText().toString();
                                    if (textLat.length() > 0 && textLon.length() > 0) {
                                        cityInfoObject.setLatitude(Float.valueOf(textLat));
                                        cityInfoObject.setLongitude(Float.valueOf(textLon));
                                    } else {
                                        cityInfoObject.setLatitude(0);
                                        cityInfoObject.setLongitude(0);
                                        Toast.makeText(getActivity(), "The location is better with latitude and longitude.", Toast.LENGTH_LONG).show();
                                    }
                                    if (mSQLiteWeatherChan != null) {
                                        mSQLiteWeatherChan.addCity(cityInfoObject);
                                    } else {
                                        Toast.makeText(getActivity(), "DB Problem.", Toast.LENGTH_SHORT).show();
                                    }
                                    mSettingCallback.cbUpdateAdapterSpinnerCity();
                                    dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "I need city same.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );



        builder.setView(view);
        return builder.create();
    }

}
