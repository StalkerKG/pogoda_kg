package kg.ram.weatherkg.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kg.ram.weatherkg.R;
import kg.ram.weatherkg.contracts.MainActivityContract;
import kg.ram.weatherkg.helpers.Helper;
import kg.ram.weatherkg.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    @BindView(R.id.tvUpdateTime) TextView tvUpdateTime;
    @BindView(R.id.rvDailyWeatherHolder) RecyclerView rvDailyWeatherHolder;
    @BindView(R.id.spnrCities) Spinner spnrCities;
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.pbLoadIndicator) ProgressBar pbLoadIndicator;

    private Unbinder mBind;
    private BroadcastReceiver mNetworkMonitoringReceiver;
    private boolean mIsSpinnerInitSelect;
    private MainActivityContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setElevation(0);
        }

        mPresenter = new MainActivityPresenter();
        mPresenter.attach(this);

        spnrCities.setAdapter(mPresenter.getCitiesSpinnerAdapter());
        spnrCities.setSelection(mPresenter.getCityPosition());
        spnrCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!mIsSpinnerInitSelect) {
                    mIsSpinnerInitSelect = true;
                    return;
                }
                mPresenter.onChangeCity(i);
                rvDailyWeatherHolder.scrollToPosition(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rvDailyWeatherHolder.setLayoutManager(new LinearLayoutManager(this));
        rvDailyWeatherHolder.setAdapter(mPresenter.getDailyAdapter());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Helper.isConnectToNetwork(this)) {
            updateWeather();
        } else {
            registerNetworkListenerReceiver();
            showBadConnectionMessage();
        }
    }

    private void registerNetworkListenerReceiver() {
        mNetworkMonitoringReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Helper.isConnectToNetwork(MainActivity.this))
                    updateWeather();
            }
        };
        registerReceiver(mNetworkMonitoringReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void updateWeather() {
        mPresenter.updateWeather();
    }

    private void showBadConnectionMessage() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.oops)
                .setCancelable(false)
                .setMessage(R.string.network_problem_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        if (mBind != null) {
            mBind.unbind();
        }
        if (mNetworkMonitoringReceiver != null)
            unregisterReceiver(mNetworkMonitoringReceiver);
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showWeatherLoadingIndicator() {
        pbLoadIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideWeatherLoadingIndicator() {
        pbLoadIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showSnackBarWithMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, 5000).show();
    }

    @Override
    public void setCurrentTemperature(String temperature) {
        textView.setText(temperature);
    }

    @Override
    public void setCurrentTemperatureIcon(Drawable weatherDrawable) {
        imageView.setImageDrawable(weatherDrawable);
    }

    @Override
    public void setUpdateTime(String lastUpdateTime) {
        tvUpdateTime.setText(lastUpdateTime);
    }
}
