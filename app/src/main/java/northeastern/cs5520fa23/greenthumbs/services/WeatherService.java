package northeastern.cs5520fa23.greenthumbs.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WeatherService extends Service {
    public static final String EXTRA_BOX_X = "EXTRA_BOX_X";
    public static final String EXTRA_BOX_Y = "EXTRA_BOX_Y";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int boxX = intent.getIntExtra(EXTRA_BOX_X, 70);
        final int boxY = intent.getIntExtra(EXTRA_BOX_Y, 89);

        new Thread(new Runnable() {
            @Override
            public void run() {
                fetchWeatherData(boxX, boxY);
            }
        }).start();

        return START_NOT_STICKY;
    }

    private void fetchWeatherData(int boxX, int boxY) {
        try {
            String urlString = String.format("https://api.weather.gov/gridpoints/BOX/%d,%d/forecast", boxX, boxY);
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            Log.e("WeatherService", "Error fetching weather data", e);
        }
    }
}


