package northeastern.cs5520fa23.greenthumbs.model.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.model.weather.WeatherForecast;
import northeastern.cs5520fa23.greenthumbs.model.weather.WeatherResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.gson.Gson;

public class WeatherService extends Service {
    public static final String latitude = "42.3458";
    public static final String longitude = "-71.0947";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    public static final String ACTION_WEATHER_UPDATE = "com.example.weatherapp.ACTION_WEATHER_UPDATE";
    public static final String EXTRA_WEATHER_DATA = "com.example.weatherapp.EXTRA_WEATHER_DATA";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        float boxX = intent.getFloatExtra(latitude, 42.3458F);
        float boxY = intent.getFloatExtra(longitude, -71.0947F);

        new Thread(() -> {
            String urlString = getForecastUrl(boxX, boxY);
            List<String> requiredPeriods = fetchPeriodData(urlString);
            broadcastMessage(requiredPeriods, this);
        }).start();

        return START_NOT_STICKY;
    }

    public static String getForecastUrl(double latitude, double longitude) {
        @SuppressLint("DefaultLocale") String url = String.format("https://api.weather.gov/points/%f,%f", latitude, longitude);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            WeatherResponse weatherResponse = gson.fromJson(response.body().string(), WeatherResponse.class);
            return weatherResponse.getForecastUrl();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Returns a list of periods with each of them an hour different from the previous.
     * The period 0 marks the current weather conditions.
     * @param url URL to fetch temperature from
     */
    public static List<String> fetchPeriodData(String url) {
        if (url==null) {
            Log.e("WeatherService", "URL is null. Cannot fetch weather data.");
            return new ArrayList<>();
        }

        Request request = new Request.Builder().url(url).build();


        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            assert response.body() != null;
            WeatherForecast forecast = gson.fromJson(response.body().string(), WeatherForecast.class);
            List<String> requiredPeriods = new ArrayList<>();
            List<WeatherForecast.Period> periods = forecast.getPeriods();
            requiredPeriods.add(checkWeatherCondition(periods.get(0).getShortForecast()));
            requiredPeriods.add(checkWeatherCondition(periods.get(24).getShortForecast()));
            requiredPeriods.add(checkWeatherCondition(periods.get(48).getShortForecast()));
            return requiredPeriods;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static String checkWeatherCondition(String inputString) {
        List<String> conditions = Arrays.asList("sun", "cloud", "rain", "snow", "clear", "fog", "frost");

        for (String condition : conditions) {
            if (inputString.toLowerCase().contains(condition)) {
                return condition;
            }
        }
        return "";
    }

    public List<String> getForecast() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("HomeLatitude", 0);
        float longitude = sharedPreferences.getFloat("HomeLongitude", 0);
        String urlString = getForecastUrl(latitude, longitude);
        return fetchPeriodData(urlString);
    }

    public void broadcastMessage(List<String> requiredPeriods, Context context) {
        Intent intent = new Intent(ACTION_WEATHER_UPDATE);
        intent.putStringArrayListExtra(EXTRA_WEATHER_DATA, new ArrayList<>(requiredPeriods));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}


