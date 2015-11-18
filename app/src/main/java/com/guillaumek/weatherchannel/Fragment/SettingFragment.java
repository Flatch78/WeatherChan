package com.guillaumek.weatherchannel.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.MessageTool;


public class SettingFragment extends Fragment {

    private static final String TAG = SettingFragment.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    // SharedPreferences
    SharedPreferences mSharedPreferences;
    public static final String SETTING_PREFS = "settingPrefs" ;
    public static final String checkKey = "checkKey";
    public static final String GraphicKey = "graphicKey";
    public static final String HumidityKey = "humiKey";
    public static final String WindKey = "windKey";
    public static final String PressureKey = "pressKey";
    public static final String CloudsKey = "cloudsKey";
    public static final String nbDaysKey = "nbDaysKey";
    public static final String UVKey = "uvKey";

    // View

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getActivity().getSharedPreferences(SETTING_PREFS, Context.MODE_PRIVATE);
//        if (mSharedPreferences.getString(checkKey, null) == null) {
//            SharedPreferences.Editor editor = mSharedPreferences.edit();
//            editor.putBoolean(GraphicKey, true);
//            editor.putBoolean(HumidityKey, true);
//            editor.putBoolean(WindKey, true);
//            editor.putBoolean(PressureKey, true);
//            editor.putBoolean(CloudsKey, true);
//            editor.putBoolean(UVKey, true);
//            editor.putInt(nbDaysKey, 1);
//            editor.putString(checkKey, checkKey);
//            editor.commit();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Switch switchGraphic = (Switch) view.findViewById(R.id.switchGraphic);
        switchGraphic.setChecked(mSharedPreferences.getBoolean(GraphicKey, true));
        switchGraphic.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        view.findViewById(R.id.layoutNbDays).setVisibility(isChecked ? View.VISIBLE : View.GONE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(GraphicKey, isChecked);
                        editor.apply();
                    }
                });
        Switch switchHumidity = (Switch) view.findViewById(R.id.switchHumidity);
        switchHumidity.setChecked(mSharedPreferences.getBoolean(HumidityKey, true));
        switchHumidity.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(HumidityKey, isChecked);
                        editor.apply();
                    }
                });
        Switch switchWind = (Switch) view.findViewById(R.id.switchWind);
        switchWind.setChecked(mSharedPreferences.getBoolean(WindKey, true));
        switchWind.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(WindKey, isChecked);
                        editor.apply();
                    }
                });
        Switch switchPressure = (Switch) view.findViewById(R.id.switchPressure);
        switchPressure.setChecked(mSharedPreferences.getBoolean(PressureKey, true));
        switchPressure.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(PressureKey, isChecked);
                        editor.apply();
                    }
                });
        Switch switchClouds = (Switch) view.findViewById(R.id.switchClouds);
        switchClouds.setChecked(mSharedPreferences.getBoolean(CloudsKey, true));
        switchClouds.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(CloudsKey, isChecked);
                        editor.apply();
                    }
                });
        Switch switchUV = (Switch) view.findViewById(R.id.switchUV);
        switchUV.setChecked(mSharedPreferences.getBoolean(UVKey, true));
        switchUV.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putBoolean(UVKey, isChecked);
                        editor.apply();
                    }
                });

        setViewSeekbar(view);
        return view;
    }

    private void setViewSeekbar(View view) {
        final TextView textViewNbDays = (TextView) view.findViewById(R.id.textViewNbDays);
        SeekBar seekBarNbDays = (SeekBar) view.findViewById(R.id.seekBarNbDays);
        seekBarNbDays.setMax(4);
        int intProgress = mSharedPreferences.getInt(nbDaysKey, 0);
        textViewNbDays.setText(String.format("(%d)", intProgress + 1));
        seekBarNbDays.setProgress(intProgress);
        seekBarNbDays.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 1;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue + 1;
                textViewNbDays.setText(String.format("(%d)", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putInt(nbDaysKey, progress - 1);
                editor.apply();
            }
        });
        view.findViewById(R.id.layoutNbDays).setVisibility(mSharedPreferences.getBoolean(GraphicKey, true) ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
