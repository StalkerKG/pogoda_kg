package kg.ram.weatherkg.model;

/**
 * Created by RAM on 10.08.2017.
 */

public class ParserResponce {
    private DailyWeather[] mDailyWeathers;
    private Exception mException;

    public ParserResponce(DailyWeather[] dailyWeathers) {
        mDailyWeathers = dailyWeathers;
    }

    public ParserResponce(Exception exception) {
        mException = exception;
    }

    public DailyWeather[] getDailyWeathers() {
        return mDailyWeathers;
    }

    public Exception getException() {
        return mException;
    }
}
