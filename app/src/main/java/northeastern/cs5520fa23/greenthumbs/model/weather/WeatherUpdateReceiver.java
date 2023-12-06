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

public class WeatherUpdateReceiver extends BroadcastReceiver {

    private final Fragment fragment;
    public static final String ACTION_WEATHER_UPDATE = "com.example.weatherapp.ACTION_WEATHER_UPDATE";
    public static final String EXTRA_WEATHER_DATA = "com.example.weatherapp.EXTRA_WEATHER_DATA";

    public WeatherUpdateReceiver(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> forecasts = intent.getStringArrayListExtra(EXTRA_WEATHER_DATA);
        if (forecasts != null && forecasts.size() >= 3) {
            // Get references to the UI components
            ImageView imgToday = fragment.requireView().findViewById(R.id.img_weather_today);
            TextView txtToday = fragment.requireView().findViewById(R.id.txt_weather_today);
            ImageView imgTomorrow = fragment.requireView().findViewById(R.id.img_weather_tomorrow);
            TextView txtTomorrow = fragment.requireView().findViewById(R.id.txt_weather_tomorrow);
            ImageView imgDayAfter = fragment.requireView().findViewById(R.id.img_weather_day_after);
            TextView txtDayAfter = fragment.requireView().findViewById(R.id.txt_weather_day_after);

            // Update the UI components with the new data
            updateWeatherView(imgToday, txtToday, forecasts.get(0));
            updateWeatherView(imgTomorrow, txtTomorrow, forecasts.get(1));
            updateWeatherView(imgDayAfter, txtDayAfter, forecasts.get(2));
        }
    }

    private void updateWeatherView(ImageView weatherIcon, TextView weatherText, String forecast) {
        int resourceIcon = getWeatherIconResource(forecast);
        weatherIcon.setImageResource(resourceIcon);
        weatherText.setText(forecast);
    }

    private int getWeatherIconResource(String forecast) {
        switch (forecast.toLowerCase()) {
            case "sunny":
                case "clear":
                return R.drawable.sunny_24;
            case "cloudy":
                return R.drawable.cloud_24;
            case "rainy":
                return R.drawable.water_drop_24;
            case "snow":
                return R.drawable.snow_24;
            default:
                return R.drawable.baseline_question_mark_24;
        }
    }
}


