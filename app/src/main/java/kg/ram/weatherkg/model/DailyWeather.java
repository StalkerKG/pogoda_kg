package kg.ram.weatherkg.model;

import org.jsoup.select.Elements;

/**
 * Created by RAM on 10.08.2017.
 */

public class DailyWeather {
    private String mDate;
    private Weather[] mWeathers;

    public DailyWeather(Elements elements){
        mDate = elements.get(1).text();
        mWeathers = new Weather[4];
        mWeathers[0] = new Weather(elements, DayTimeType.NIGHT);
        mWeathers[1] = new Weather(elements, DayTimeType.MORNING);
        mWeathers[2] = new Weather(elements, DayTimeType.DAY);
        mWeathers[3] = new Weather(elements, DayTimeType.EVENING);
    }

    public String getDate() {
        return mDate;
    }

    public Weather[] getWeathers() {
        return mWeathers;
    }
}
