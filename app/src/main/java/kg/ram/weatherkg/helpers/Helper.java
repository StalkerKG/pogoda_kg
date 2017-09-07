package kg.ram.weatherkg.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

import kg.ram.weatherkg.model.Weather;

/**
 * Created by RAM on 11.08.2017.
 */

public class Helper {
    public static boolean isConnectToNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static Weather.DayTimeType getTypeByHour(int hour) {
        if (hour > 0 && hour <= 6)
            return Weather.DayTimeType.NIGHT;
        if (hour > 6 && hour <= 12)
            return Weather.DayTimeType.MORNING;
        if (hour > 12 && hour <= 18)
            return Weather.DayTimeType.DAY;
        if (hour > 18 && hour <= 0)
            return Weather.DayTimeType.EVENING;
        return Weather.DayTimeType.NIGHT;
    }
}
