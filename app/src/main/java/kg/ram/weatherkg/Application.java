package kg.ram.weatherkg;

import kg.ram.weatherkg.helpers.Prefs;

/**
 * Created by RAM on 11.08.2017.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Prefs(this);
    }
}
