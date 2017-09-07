package kg.ram.weatherkg.model;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import kg.ram.weatherkg.LoaderCallback;
import kg.ram.weatherkg.ui.WeatherAdapter;

public class WeatherManager {
    private static WeatherManager mInstance;
    private final WeatherParser mWeatherParser;
    private Date mLastUpdateTime;
    private ArrayList<DailyWeather> mDailyWeathers = new ArrayList<>();
    private WeatherAdapter mAdapter;
    private boolean mIsCityChanged;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");


    public WeatherManager() {
        mWeatherParser = new WeatherParser();
    }

    public static WeatherManager getInstance() {
        if(mInstance == null)
            mInstance = new WeatherManager();
        return mInstance;
    }

    public void updateWeather() {
        updateWeather(null);
    }

    public void updateWeather(final LoaderCallback<ArrayList<DailyWeather>> callback) {
        try {

            if (mDailyWeathers.isEmpty() || mIsCityChanged) {
                mWeatherParser.getActualWeather(new LoaderCallback<DailyWeather[]>() {
                    @Override
                    public void onStartLoad() {
                        if(callback!=null)
                            callback.onStartLoad();
                    }

                    @Override
                    public void onSuccess(DailyWeather[] dailyWeathers) {
                        mIsCityChanged = false;
                        mLastUpdateTime = new Date();
                        mDailyWeathers.clear();
                        mDailyWeathers.addAll(Arrays.asList(dailyWeathers));
                        mAdapter.notifyDataSetChanged();
                        if (callback != null)
                            callback.onSuccess(mDailyWeathers);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (callback != null) {
                            callback.onFailure(e);
                        }
                    }
                });
            } else if (callback != null) {
                callback.onSuccess(mDailyWeathers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLastUpdateTime() {
        return mDateFormat.format(mLastUpdateTime);
    }

    public WeatherAdapter getAdapter(Context context) {
        mAdapter = new WeatherAdapter(context, mDailyWeathers);
        return mAdapter;
    }

    public void changeCity(String s) {
        if(!mWeatherParser.getCity().equalsIgnoreCase(s)) {
            mWeatherParser.changeCity(s);
            mIsCityChanged = true;
        }
    }

    public void reset() {
        mWeatherParser.reset();
    }
}
