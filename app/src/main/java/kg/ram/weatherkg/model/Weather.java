package kg.ram.weatherkg.model;

import org.jsoup.select.Elements;

/**
 * Created by RAM on 10.08.2017.
 */

public class Weather {

    private DayTimeType mType;
    private String mTime;
    private String mTemperature;
    private String mImage;
    private String mDesc;

    public Weather(Elements elements, DayTimeType dayTimeTypeType) {
        mType = dayTimeTypeType;
        switch (dayTimeTypeType) {
            case NIGHT: {
                update(elements, 5, 7, 9, 10);
                break;
            }
            case MORNING: {
                update(elements, 12, 14, 16, 17);
                break;
            }
            case DAY: {
                update(elements, 19, 21, 23, 24);
                break;
            }
            case EVENING: {
                update(elements, 26, 28, 30, 31);
                break;
            }
        }
    }

    private void update(Elements elements, int pos1, int pos2, int pos3, int pos4) {
        mTime = elements.get(pos1).text();
        mTemperature = elements.get(pos2).text().replace("⁰C", "°C");
        mImage = elements.get(pos3).attr("src");
        mDesc = elements.get(pos4).text();
    }

    public String getTime() {
        return mTime;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public String getImage() {
        return mImage;
    }

    public String getDesc() {
        return mDesc;
    }

    public DayTimeType getType() {
        return mType;
    }

    public String getImageResId() {
        return getImage().replace("weather/", "")
                .replace(".jpg", "")
                .replace("-", "");
    }

}
