package northeastern.cs5520fa23.greenthumbs.model.weather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.Weather.WeatherViewModel;

public class WeatherUpdateReceiver extends BroadcastReceiver {
    private final WeatherViewModel viewModel;
    public static final String EXTRA_WEATHER_DATA = "com.example.weatherapp.EXTRA_WEATHER_DATA";

    public WeatherUpdateReceiver(WeatherViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> forecasts = intent.getStringArrayListExtra(EXTRA_WEATHER_DATA);
        if (forecasts != null && forecasts.size() >= 3) {
            viewModel.setForecasts(forecasts);
        }
    }
}


