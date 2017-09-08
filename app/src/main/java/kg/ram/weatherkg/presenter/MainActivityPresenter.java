package kg.ram.weatherkg.presenter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kg.ram.weatherkg.LoaderCallback;
import kg.ram.weatherkg.R;
import kg.ram.weatherkg.contracts.MainActivityContract;
import kg.ram.weatherkg.helpers.Helper;
import kg.ram.weatherkg.helpers.ImageHelper;
import kg.ram.weatherkg.helpers.Prefs;
import kg.ram.weatherkg.helpers.PrefsName;
import kg.ram.weatherkg.model.DailyWeather;
import kg.ram.weatherkg.model.DayTimeType;
import kg.ram.weatherkg.model.Weather;
import kg.ram.weatherkg.model.WeatherManager;
import kg.ram.weatherkg.ui.WeatherAdapter;
import kg.ram.weatherkg.view.WeatherWidget;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private final String[] CITIES = {"Бишкек", "Чолпон-Ата", "Каракол", "Ош", "Нарын", "Джалал-Абад", "Баткен", "Талас"};
    private final String[] URLS = {"", "cholpon-ata", "karakol", "osh", "naryn", "djalal-abad", "batken", "talas"};

    private MainActivityContract.View mView;
    private int mCityPosition;
    private ArrayAdapter<String> mCitiesAdapter;
    private WeatherManager mWeatherManager;

    public MainActivityPresenter() {
        mCityPosition = Prefs.getInt(PrefsName.CITY_POSITION);
        mWeatherManager = WeatherManager.getInstance();
        mWeatherManager.changeCity(URLS[mCityPosition]);
    }

    @Override
    public void attach(MainActivityContract.View view) {
        mView = view;
        updateCitiesAdapter(view.getContext());
    }

    @Override
    public void destroy() {
        mView = null;
        mWeatherManager.reset();
    }

    @Override
    public ArrayAdapter<String> getCitiesSpinnerAdapter() {
        return mCitiesAdapter;
    }

    private void updateCitiesAdapter(Context context) {
        mCitiesAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, CITIES);
        mCitiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onChangeCity(int position) {
        mCityPosition = position;
        Prefs.setValue(PrefsName.CITY_POSITION, position);
        mWeatherManager.changeCity(URLS[position]);
        updateWeather();
    }

    @Override
    public int getCityPosition() {
        return mCityPosition;
    }

    @Override
    public WeatherAdapter getDailyAdapter() {
        return mWeatherManager.getAdapter(mView.getContext());
    }

    @Override
    public void updateWeather() {
        if (mView == null) return;
        mWeatherManager.updateWeather(new LoaderCallback<ArrayList<DailyWeather>>() {
            @Override
            public void onStartLoad() {
                if (mView == null) return;
                mView.showWeatherLoadingIndicator();
            }

            @Override
            public void onSuccess(ArrayList<DailyWeather> dailyWeathers) {
                if (mView == null) return;
                mView.hideWeatherLoadingIndicator();
                mView.setUpdateTime(mWeatherManager.getLastUpdateTime());
                Weather currentWeather = getCurrentWeather(dailyWeathers);
                if (currentWeather != null) {
                    String temperature = currentWeather.getTemperature();
                    Drawable weatherDrawable = ImageHelper.getWeatherDrawable(mView.getContext(),
                            currentWeather.getImageResId());
                    updateWidgets(temperature, weatherDrawable);
                    mView.setCurrentTemperature(temperature);
                    mView.setCurrentTemperatureIcon(weatherDrawable);
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (mView == null) return;
                mView.hideWeatherLoadingIndicator();
                mView.showSnackBarWithMessage(e.getMessage());
            }
        });
    }

    private void updateWidgets(String temperature, Drawable weatherDrawable) {
        if(mView == null) return;
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(mView.getContext());
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(mView.getContext(), WeatherWidget.class));
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            for (int appWidgetId : appWidgetIds) {
                RemoteViews rView = new RemoteViews(mView.getContext().getPackageName(), R.layout.weather_widget);
                rView.setTextViewText(R.id.tv_widget_temperature, temperature);
                rView.setImageViewBitmap(R.id.iv_widget_weathe, ((BitmapDrawable)weatherDrawable).getBitmap());
                widgetManager.updateAppWidget(appWidgetId, rView);
            }
        }
    }

    private Weather getCurrentWeather(ArrayList<DailyWeather> dailyWeathers) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        DayTimeType type = Helper.getTypeByHour(hour);

        Weather weather = null;
        DailyWeather dailyWeather = dailyWeathers.get(0);
        for (Weather w : dailyWeather.getWeathers()) {
            if (w.getType().equals(type)) {
                weather = w;
            }
        }
        return weather;
    }
}
