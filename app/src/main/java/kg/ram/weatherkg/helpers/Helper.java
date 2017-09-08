package kg.ram.weatherkg.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

import kg.ram.weatherkg.model.DayTimeType;

/**
 * Created by RAM on 11.08.2017.
 */

public class Helper {
    public static boolean isConnectToNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null
                && manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static DayTimeType getTypeByHour(int hour) {
        if (hour > 0 && hour <= 6)
            return DayTimeType.NIGHT;
        if (hour > 6 && hour <= 12)
            return DayTimeType.MORNING;
        if (hour > 12 && hour <= 18)
            return DayTimeType.DAY;
        if (hour > 18 && hour <= 0)
            return DayTimeType.EVENING;
        return DayTimeType.NIGHT;
    }
}
