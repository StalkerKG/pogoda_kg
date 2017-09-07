package kg.ram.weatherkg.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import kg.ram.weatherkg.R;
import kg.ram.weatherkg.helpers.ImageHelper;
import kg.ram.weatherkg.model.Weather;


public class WeatherDailyAdapter extends ArrayAdapter<Weather> {

    private final Context mContext;
    private final Weather[] mObjects;

    public WeatherDailyAdapter(@NonNull Context context, @NonNull Weather[] objects) {
        super(context, R.layout.weather_item, objects);
        mContext = context;
        mObjects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false);
        ImageView ivWeatherImage = convertView.findViewById(R.id.ivWeatherImage);
        TextView tvTemperature = convertView.findViewById(R.id.tvTemperature);
        TextView tvTime = convertView.findViewById(R.id.tvTime);
        TextView tvDesc = convertView.findViewById(R.id.tvDesc);
        Weather weather = mObjects[position];
        tvTemperature.setText(weather.getTemperature());
        tvTime.setText(weather.getTime());
        tvDesc.setText(weather.getDesc());
        Drawable weatherDrawable = ImageHelper.getWeatherDrawable(mContext, weather.getImageResId());
        ivWeatherImage.setImageDrawable(weatherDrawable);
        return convertView;
    }
}
