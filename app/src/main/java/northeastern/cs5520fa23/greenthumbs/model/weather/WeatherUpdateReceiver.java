package northeastern.cs5520fa23.greenthumbs.model.weather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import northeastern.cs5520fa23.greenthumbs.R;

public class WeatherUpdateReceiver extends BroadcastReceiver {

    private final Activity activity;

    // Constructor to pass the Activity
    public WeatherUpdateReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> forecasts = intent.getStringArrayListExtra("WEATHER_FORECASTS");
        if (forecasts != null && forecasts.size() >= 3) {
            // Get references to the UI components
            ImageView imgToday = activity.findViewById(R.id.img_weather_today);
            TextView txtToday = activity.findViewById(R.id.txt_weather_today);
            ImageView imgTomorrow = activity.findViewById(R.id.img_weather_tomorrow);
            TextView txtTomorrow = activity.findViewById(R.id.txt_weather_tomorrow);
            ImageView imgDayAfter = activity.findViewById(R.id.img_weather_day_after);
            TextView txtDayAfter = activity.findViewById(R.id.txt_weather_day_after);

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
                return R.drawable.sunny_24;
            case "cloudy":
                return R.drawable.cloud_24;
            case "rainy":
                return R.drawable.water_drop_24;
            default:
                return R.drawable.baseline_question_mark_24;
        }
    }
}


