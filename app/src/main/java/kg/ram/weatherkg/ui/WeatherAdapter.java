package kg.ram.weatherkg.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kg.ram.weatherkg.R;
import kg.ram.weatherkg.model.DailyWeather;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.Holder> {

    private final Context mContext;
    private final ArrayList<DailyWeather> mObjects;

    public WeatherAdapter(Context context, ArrayList<DailyWeather> objects) {
        mContext = context;
        mObjects = objects;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_weather_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.updateView(mObjects.get(position));
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private final Unbinder mUnbinder;
        @BindView(R.id.tvDateTime) TextView tvDateTime;
        @BindView(R.id.llHoursWeatherHolder) LinearLayout llHoursWeatherHolder;

        Holder(View itemView) {
            super(itemView);
            mUnbinder = ButterKnife.bind(this, itemView);
        }

        void updateView(DailyWeather dailyWeather) {
            tvDateTime.setText(dailyWeather.getDate());
            llHoursWeatherHolder.removeAllViews();
            WeatherDailyAdapter adapter = new WeatherDailyAdapter(mContext, dailyWeather.getWeathers());
            for (int i = 0; i < adapter.getCount(); i++)
                llHoursWeatherHolder.addView(adapter.getView(i, null, llHoursWeatherHolder));
        }

        @Override
        protected void finalize() throws Throwable {
            mUnbinder.unbind();
            super.finalize();
        }
    }
}
