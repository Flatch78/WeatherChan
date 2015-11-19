package com.guillaumek.weatherchannel.Fragment;


import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guillaumek.weatherchannel.Network.NetworkAPIWeather;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.MessageTool;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    private static final String TAG = InformationFragment.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    public InformationFragment() {
        // Required empty public constructor
    }

    public static InformationFragment newInstance() {
        InformationFragment fragment = new InformationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ListView listViewTable = (ListView) view.findViewById(R.id.listViewWarning);
        listViewTable.setAdapter(fillInTable());

        TextView mTextViewLinkWS = (TextView) view.findViewById(R.id.textViewLinkWebSite);
        mTextViewLinkWS.setMovementMethod(LinkMovementMethod.getInstance());

        final ImageView imageViewLogo = (ImageView) view.findViewById(R.id.logoOpenWeatherMap);


        String URLBackground = NetworkAPIWeather.getLogoOpenWeatherMap();
        Ion.with(getActivity())
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
                            imageViewLogo.setBackgroundDrawable(ob);
                        }
                    }

                });
        return view;
    }

    private SimpleCursorAdapter fillInTable() {
        String[] columns = new String[] { "_id",
                "type",
                getActivity().getString(R.string.title_uv_index),
                getActivity().getString(R.string.title_risk_index),
                getActivity().getString(R.string.title_recommendation_index)
        };

        MatrixCursor matrixCursor= new MatrixCursor(columns);
        getActivity().startManagingCursor(matrixCursor);
        matrixCursor.addRow(new Object[]{0,
                R.mipmap.ic_warning_nothing,
                getActivity().getString(R.string.title_uv_index),
                getActivity().getString(R.string.title_risk_index),
                getActivity().getString(R.string.title_recommendation_index)});
        matrixCursor.addRow(new Object[]{1,
                R.mipmap.ic_warning_low,
                getActivity().getString(R.string.row_green_uv),
                getActivity().getString(R.string.row_green_risk),
                getActivity().getString(R.string.row_green_recommend)});
        matrixCursor.addRow(new Object[] { 2,
                R.mipmap.ic_warning_moderate,
                getActivity().getString(R.string.row_yellow_uv),
                getActivity().getString(R.string.row_yellow_risk),
                getActivity().getString(R.string.row_yellow_recommend) });
        matrixCursor.addRow(new Object[] { 3,
                R.mipmap.ic_warning_high,
                getActivity().getString(R.string.row_orange_uv),
                getActivity().getString(R.string.row_orange_risk),
                getActivity().getString(R.string.row_orange_recommend) });
        matrixCursor.addRow(new Object[] { 3,
                R.mipmap.ic_warning_very_high,
                getActivity().getString(R.string.row_red_uv),
                getActivity().getString(R.string.row_red_risk),
                getActivity().getString(R.string.row_red_recommend) });
        matrixCursor.addRow(new Object[]{3,
                R.mipmap.ic_warning_extreme,
                getActivity().getString(R.string.row_purple_uv),
                getActivity().getString(R.string.row_purple_risk),
                getActivity().getString(R.string.row_purple_recommend)});

        String[] from = new String[] {
                "type",
                getActivity().getString(R.string.title_uv_index),
                getActivity().getString(R.string.title_risk_index),
                getActivity().getString(R.string.title_recommendation_index) };

        int[] to = new int[] { R.id.imageViewWarning, R.id.textViewColUV, R.id.textViewColRisk, R.id.textViewColRecommend };

        return new SimpleCursorAdapter(getActivity(), R.layout.adapter_row_listview, matrixCursor, from, to, 0);
    }

}
