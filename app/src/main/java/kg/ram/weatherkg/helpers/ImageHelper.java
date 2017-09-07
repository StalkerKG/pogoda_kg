package kg.ram.weatherkg.helpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import kg.ram.weatherkg.R;

public class ImageHelper {

    public static Drawable getWeatherDrawable(Context context, String imageResId) {
        try {
            int drawable = context.getResources().getIdentifier(imageResId, "drawable", context.getPackageName());
            return ContextCompat.getDrawable(context, drawable);
        } catch (Exception e) {
            return ContextCompat.getDrawable(context, R.drawable.unknow);
        }
    }
}
