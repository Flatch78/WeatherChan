package com.guillaumek.weatherchannel.Tools;

import android.content.Context;

import com.guillaumek.weatherchannel.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;




/**
 * Created by flatch on 11/11/15.
 */
public class WeatherAChartEngine {

    private static final String TAG = WeatherAChartEngine.class.getName();
    private MessageTool msg = new MessageTool(TAG);

    Context mContext;

    XYMultipleSeriesRenderer mRenderer;
    GraphicalView mChartView;

    int mLineColor;
    String mTitle;
    String mXTitle;
    String mYTitle;

    public WeatherAChartEngine(Context context, String title, String XTitle, String YTitle) {
        mContext = context;
        mTitle = title;
        mXTitle = XTitle;
        mYTitle = YTitle;

        int color = R.color.black;
        mLineColor = context.getResources().getColor(color);

    }

    public void setLineColor(int color) {
        mLineColor = color;
    }

    public GraphicalView getChart(List<Double> datas, ContentChart contentChart) {

        if (datas != null && datas.size() > 0) {
            XYSeries xSeries = new XYSeries("");
            for (int cpt = 0; cpt < datas.size(); ++cpt) {
                xSeries.add(cpt * 3, datas.get(cpt));
            }


            // Create a Dataset to hold the XSeries.
            XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
            dataSet.addSeries(xSeries);

            // Create XYSeriesRenderer to customize XSeries
            XYSeriesRenderer Xrenderer = new XYSeriesRenderer();
            Xrenderer.setColor(mLineColor);
            Xrenderer.setPointStyle(PointStyle.X);
            Xrenderer.setDisplayChartValues(true);
            Xrenderer.setLineWidth(3);
            Xrenderer.setFillPoints(true);

            // Create XYMultipleSeriesRenderer to customize the whole chart
            mRenderer = new XYMultipleSeriesRenderer();

            mRenderer.setChartTitle(mTitle);
            mRenderer.setXTitle(mXTitle);
            mRenderer.setYTitle(mYTitle);
            { // Color
                mRenderer.setLabelsColor(mContext.getResources().getColor(R.color.WhiteSmoke));
                mRenderer.setXLabelsColor(mContext.getResources().getColor(R.color.WhiteSmoke));
                mRenderer.setMarginsColor(mContext.getResources().getColor(R.color.colorPrimaryOpac));
                mRenderer.setGridColor(mContext.getResources().getColor(R.color.colorGrayOpac));
                mRenderer.setXAxisColor(mContext.getResources().getColor(R.color.WhiteSmoke));
                mRenderer.setYAxisColor(mContext.getResources().getColor(R.color.WhiteSmoke));
            }

            mRenderer.setZoomButtonsVisible(false);
            mRenderer.setPanEnabled(true);
            mRenderer.setShowGrid(true);
            mRenderer.setClickEnabled(true);
            mRenderer.setZoomEnabled(false);
            mRenderer.setShowLegend(false);

            mRenderer.setYAxisMin(contentChart.getMin());
            mRenderer.setYAxisMax(contentChart.getMax());

            // Adding the XSeriesRenderer to the MultipleRenderer.
            mRenderer.addSeriesRenderer(Xrenderer);
            mChartView = (GraphicalView) ChartFactory.getLineChartView(mContext, dataSet, mRenderer);
            mChartView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhiteOpac));

            return mChartView;
        } else {
            msg.MsgError("Error, Data " + (datas == null ? "null" : "Ok" + (datas.size() > 0 ? " full" : " empty")));
        }
        return null;
    }



}
