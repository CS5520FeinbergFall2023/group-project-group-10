package northeastern.cs5520fa23.greenthumbs.model.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class WeatherUpdateReceiver extends BroadcastReceiver {

    private final ImageView weatherTodayIcon;
    private final TextView weatherTodayText;
    private final Gson gson;

    // Constructor to initialize the UI components and Gson instance
    public WeatherUpdateReceiver(ImageView weatherTodayIcon, TextView weatherTodayText) {
        this.weatherTodayIcon = weatherTodayIcon;
        this.weatherTodayText = weatherTodayText;
        this.gson = new Gson();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*if ("ACTION_WEATHER_UPDATE".equals(intent.getAction())) {
            // Extract the JSON string from the intent
            String weatherJson = intent.getStringExtra("WEATHER_DATA");
            if (weatherJson != null && !weatherJson.isEmpty()) {
                // Parse the JSON string to WeatherForecast object
                WeatherForecast forecast = gson.fromJson(weatherJson, WeatherForecast.class);

                // Update the UI with the first period of weather data (today's weather)
                if (forecast != null && forecast.getPeriods() != null && !forecast.getPeriods().isEmpty()) {
                    WeatherForecast.Period todayWeather = forecast.getPeriods().get(0);

                    // Update the weather icon and text on the main thread
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> {
                        weatherTodayIcon.setImageResource(getWeatherIconResource(todayWeather.getIcon()));
                        weatherTodayText.setText(todayWeather.getShortForecast());
                    });
                }
            }
        }*/
    }

    // Method to map the weather condition to an image resource
    private int getWeatherIconResource(String iconName) {
        // This method should map the icon name to a drawable resource.
        // For example:
        /*
        if ("clear-day".equals(iconName)) {
            return R.drawable.ic_weather_clear_day;
        } else if ("rain".equals(iconName)) {
            return R.drawable.ic_weather_rain;
        }
        // Add more conditions for different weather icons.
        return R.drawable.ic_weather_default; // Default icon
         */
        return 0;
    }
}

