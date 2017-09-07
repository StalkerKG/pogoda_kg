package kg.ram.weatherkg.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class Prefs {

    private static SharedPreferences mSharedPreferences;

    public Prefs(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initDefaultPreference();
    }

    private void initDefaultPreference() {
        contains(PrefsName.CITY_POSITION, 0);
    }

    public static SharedPreferences GetSharedPreferences() {
        return mSharedPreferences;
    }

    public static int getInt(String key) {
        try {
            return mSharedPreferences.getInt(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getLong(String key) {
        try {
            return mSharedPreferences.getLong(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean getBool(String key) {
        try {
            return mSharedPreferences.getBoolean(key, false);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getString(String key) {
        try {
            return mSharedPreferences.getString(key, "");
        } catch (Exception e) {
            return "";
        }
    }

    public static float getDouble(String key) {
        try {
            return mSharedPreferences.getFloat(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setValue(String key, Object value) {
        try {
            SharedPreferences.Editor editor = GetSharedPreferences().edit();
            if (value instanceof Integer)
                editor.putInt(key, Integer.parseInt(value.toString()));
            if (value instanceof Long)
                editor.putLong(key, Long.parseLong(value.toString()));
            if (value instanceof String)
                editor.putString(key, value.toString());
            if (value instanceof Boolean)
                editor.putBoolean(key, Boolean.parseBoolean(value.toString()));
            editor.apply();
        } catch (NumberFormatException e) {
        }
    }

    public static boolean contains(String key, @Nullable Object initValue) {
        boolean contain = mSharedPreferences.contains(key);
        if (!contain && initValue != null)
            setValue(key, initValue);
        return contain;
    }
}
