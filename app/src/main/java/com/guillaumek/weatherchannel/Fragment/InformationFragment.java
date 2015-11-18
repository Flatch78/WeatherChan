package com.guillaumek.weatherchannel.Fragment;


import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.guillaumek.weatherchannel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {


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
        matrixCursor.addRow(new Object[] { 3,
                R.mipmap.ic_warning_extreme,
                getActivity().getString(R.string.row_purple_uv),
                getActivity().getString(R.string.row_purple_risk),
                getActivity().getString(R.string.row_purple_recommend) });

        String[] from = new String[] {
                "type",
                getActivity().getString(R.string.title_uv_index),
                getActivity().getString(R.string.title_risk_index),
                getActivity().getString(R.string.title_recommendation_index) };

        int[] to = new int[] { R.id.imageViewWarning, R.id.textViewColUV, R.id.textViewColRisk, R.id.textViewColRecommend };

        return new SimpleCursorAdapter(getActivity(), R.layout.adapter_row_listview, matrixCursor, from, to, 0);
    }

}
