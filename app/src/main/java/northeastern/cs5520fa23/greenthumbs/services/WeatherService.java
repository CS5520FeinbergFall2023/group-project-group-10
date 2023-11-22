package northeastern.cs5520fa23.greenthumbs.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import northeastern.cs5520fa23.greenthumbs.model.WeatherForecast;
import northeastern.cs5520fa23.greenthumbs.model.WeatherResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;


public class WeatherService extends Service {
    public static final String latitude = "42.3458";
    public static final String longitude = "-71.0947";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final double boxX = intent.getDoubleExtra(latitude, 42.3458);
        final double boxY = intent.getDoubleExtra(longitude, -71.0947);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlString = getForecastUrl(boxX, boxY);
                fetchTemperatureData(urlString);
            }
        }).start();

        return START_NOT_STICKY;
    }

    public static String getForecastUrl(double latitude, double longitude) {
        String url = String.format("https://api.weather.gov/points/%f,%f", latitude, longitude);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            WeatherResponse weatherResponse = gson.fromJson(response.body().string(), WeatherResponse.class);
            return weatherResponse.getForecastUrl();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<WeatherForecast.Period> fetchTemperatureData(String url) {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            WeatherForecast forecast = gson.fromJson(response.body().string(), WeatherForecast.class);
            return forecast.getPeriods();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


