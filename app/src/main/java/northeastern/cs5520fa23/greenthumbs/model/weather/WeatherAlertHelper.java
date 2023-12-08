package northeastern.cs5520fa23.greenthumbs.model.weather;

import android.content.Context;

import java.util.List;

import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.model.NotificationHelper;
import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;
import northeastern.cs5520fa23.greenthumbs.model.weather.WeatherForecast;

public class WeatherAlertHelper {
    private WeatherService weatherService;
    private Context context;

    public WeatherAlertHelper(WeatherService service, Context context) {
        this.weatherService = service;
        this.context = context;
    }

    public void checkWeatherAndNotify() {
        List<String> periods = weatherService.getForecast();

        if (shouldNotify(periods.get(0))) {
            NotificationHelper.showNotification(context, "Weather Alert for Today", generateMessage(periods.get(0)), R.drawable.weather_warning);
        }

        // Check tomorrow's weather and send a notification
        if (shouldNotify(periods.get(1))) {
            NotificationHelper.showNotification(context, "Weather Alert for Tomorrow", generateMessage(periods.get(1)), R.drawable.weather_warning);
        }

        if (shouldNotify(periods.get(2))) {
            NotificationHelper.showNotification(context,"Weather Alert for the Day After Tomorrow", generateMessage(periods.get(2)), R.drawable.weather_warning);
        }
    }

    private boolean shouldNotify(String weather) {
        return weather.contains("rain") || weather.contains("snow")|| weather.contains("sun");
    }

    private String generateMessage(String weather) {
        // Generate a message based on the weather conditions
        if (weather.contains("rain")) {
            return "Expected rain. Remember to protect your plants!";
        } else if (weather.contains("snow")) {
            return "Snow forecasted. Time to winterize your garden!";
        } else if (weather.contains("sun")) {
            return "Expected rain. Remember to protect your plants!";
        }
        return "";
    }
}

