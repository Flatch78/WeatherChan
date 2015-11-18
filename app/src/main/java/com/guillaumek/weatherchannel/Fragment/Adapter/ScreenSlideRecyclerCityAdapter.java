package com.guillaumek.weatherchannel.Fragment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.guillaumek.weatherchannel.Network.ForecastWeather;
import com.guillaumek.weatherchannel.Network.NetworkAPIWeather;
import com.guillaumek.weatherchannel.Network.Object.WeatherObject;
import com.guillaumek.weatherchannel.R;
import com.guillaumek.weatherchannel.Tools.Tools;
import com.koushikdutta.ion.Ion;

/**
 * Created by flatch on 09/11/15.
 */
public class ScreenSlideRecyclerCityAdapter extends RecyclerView.Adapter<ScreenSlideRecyclerCityAdapter.ViewHolder> {

    private Context mContext;
    private ForecastWeather mForecastWeather;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewCity;
        public TextView mTextViewDescription;
        public TextView mTextViewTemperature;
        public TextView mTextViewUpdate;
        public ImageView mImageViewIcon;

        public ViewHolder(View view) {
            super(view);
            mTextViewCity = (TextView) view.findViewById(R.id.textViewCity);
            mTextViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
            mTextViewTemperature = (TextView) view.findViewById(R.id.textViewTemperature);
            mTextViewUpdate = (TextView) view.findViewById(R.id.textViewUpdate);
            mImageViewIcon = (ImageView) view.findViewById(R.id.imageViewWeather);
            mTextViewTemperature.setTextSize(30);
        }
    }

    public ScreenSlideRecyclerCityAdapter(Context context, ForecastWeather forecastWeather) {
        mContext = context;
        updateList(forecastWeather);
    }

    public void updateList(ForecastWeather forecastWeather) {
        mForecastWeather = forecastWeather;
    }

    @Override
    public ScreenSlideRecyclerCityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_city, parent, false);
        ViewHolder vh = new ViewHolder(view);

//        view.findViewById(R.id.textViewCity).setVisibility(View.GONE);
        view.findViewById(R.id.textViewCountry).setVisibility(View.GONE);
        view.findViewById(R.id.textViewHumidity).setVisibility(View.GONE);
        view.findViewById(R.id.textViewPressure).setVisibility(View.GONE);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mForecastWeather != null) {
            ForecastWeather.FtWeather ftWeather = mForecastWeather.list.get(position);
            String description = "";
            String icon = "";
            for (WeatherObject weather : ftWeather.weather) {
                description += weather.description + "\n";
                icon = weather.icon;
            }

            holder.mTextViewCity.setText(mForecastWeather.city.name);
            holder.mTextViewDescription.setText(description);
            holder.mTextViewTemperature.setText(String.format("%.2f°C", Tools.getCelsiusValue(ftWeather.main.temp)));

            String URLIcon = NetworkAPIWeather.getURLWeatherIcon(icon);
            Log.e("icon", "url: " + URLIcon);
            Ion.with(mContext)
                    .load(URLIcon)
                    .withBitmap()
                    .intoImageView(holder.mImageViewIcon);
            holder.mTextViewUpdate.setText(ftWeather.dt_txt);
        }
    }

    @Override
    public int getItemCount() {
        return mForecastWeather != null ? mForecastWeather.list.size() : 0;
    }
}