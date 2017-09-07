package kg.ram.weatherkg.model;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import kg.ram.weatherkg.LoaderCallback;


public class WeatherParser {

    private final String BASE_URL = "http://pogoda.kg";
    private String mCity = "";
    private UpdateWeatherTask mUpdateWeatherTask;

    public WeatherParser() {
    }

    public void getActualWeather(LoaderCallback<DailyWeather[]> callback) {
        if(mUpdateWeatherTask == null
                || mUpdateWeatherTask.getStatus().equals(AsyncTask.Status.FINISHED))
        mUpdateWeatherTask = new UpdateWeatherTask(BASE_URL + "/" + mCity, callback);
        mUpdateWeatherTask.execute();
    }

    public void changeCity(String city) {
        mCity = city;
    }

    public void reset() {
        if(mUpdateWeatherTask!=null) {
            mUpdateWeatherTask.cancel(true);
            mUpdateWeatherTask = null;
        }
    }

    public String getCity() {
        return mCity;
    }

    private class UpdateWeatherTask extends AsyncTask<Void, Void, ParserResponce> {

        private final String mUrl;
        private LoaderCallback<DailyWeather[]> mCallback;


        public UpdateWeatherTask(String url, LoaderCallback<DailyWeather[]> callback) {
            mUrl = url;
            mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mCallback!=null)
                mCallback.onStartLoad();
        }

        @Override
        protected ParserResponce doInBackground(Void... voids) {
            Document doc = null;
            try {
                doc = Jsoup.connect(mUrl).get();
            } catch (IOException e) {
                return new ParserResponce(e);
            }

            Elements quads = doc.body().getAllElements().select("div#quad");
            DailyWeather[] weathers = new DailyWeather[quads.size()];
            for (int i = 0; i < quads.size(); i++) {
                weathers[i] = new DailyWeather(quads.get(i).getAllElements());
            }
            return new ParserResponce(weathers);
        }

        @Override
        protected void onPostExecute(ParserResponce parserResponce) {
            super.onPostExecute(parserResponce);
            if(isCancelled()) return;
            if (mCallback != null) {
                if (parserResponce.getDailyWeathers() != null)
                    mCallback.onSuccess(parserResponce.getDailyWeathers());
                else
                    mCallback.onFailure(parserResponce.getException());
            }
        }
    }

}
