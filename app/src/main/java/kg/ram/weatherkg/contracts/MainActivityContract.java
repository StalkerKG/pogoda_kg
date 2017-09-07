package kg.ram.weatherkg.contracts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import kg.ram.weatherkg.ui.WeatherAdapter;

public class MainActivityContract {
    public interface View{
        Context getContext();
        void showWeatherLoadingIndicator();
        void hideWeatherLoadingIndicator();
        void showSnackBarWithMessage(String message);
        void setCurrentTemperature(String temperature);
        void setCurrentTemperatureIcon(Drawable weatherDrawable);
        void setUpdateTime(String lastUpdateTime);
    }

    public interface Presenter{
        void attach(MainActivityContract.View view);
        void destroy();
        ArrayAdapter<String> getCitiesSpinnerAdapter();
        void onChangeCity(int position);
        int getCityPosition();
        WeatherAdapter getDailyAdapter();
        void updateWeather();
    }
}
