package com.guillaumek.weatherchannel.Tools;

/**
 * Created by flatch on 12/11/15.
 */
public class ContentChart {

    int mMin;
    int mMax;

    public ContentChart() {
    }

    public ContentChart(int min, int max) {
        mMin = min;
        mMax = max;
    }

    public int getMax() {
        return mMax;
    }

    public int getMin() {
        return mMin;
    }

    public void setMax(int max) {
        mMax = max;
    }

    public void setMin(int min) {
        mMin = min;
    }
}
