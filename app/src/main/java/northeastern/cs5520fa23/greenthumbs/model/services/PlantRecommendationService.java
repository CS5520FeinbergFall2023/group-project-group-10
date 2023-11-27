package northeastern.cs5520fa23.greenthumbs.model.services;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import northeastern.cs5520fa23.greenthumbs.model.PlantInfo;

public class PlantRecommendationService extends Service {
    public static final String latitude = "42.3458";
    public static final String longitude = "-71.0947";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        float lat = intent.getFloatExtra(latitude, 42.3458F);
        float lon = intent.getFloatExtra(longitude, -71.0947F);

        new Thread(() -> {
            String zipCode = getZipCode(lat, lon);
            if (zipCode != null) {
                String hardinessZone = getHardiness(zipCode);
                List<String> plants = PlantInfo.getPlantsForZone(hardinessZone);
            }
        }).start();

        return START_NOT_STICKY;
    }

    private String getZipCode(float latitude, float longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getPostalCode();
            }
        } catch (IOException e) {
            // Handle exception
        }
        return null;
    }

    private String getHardiness(String zipCode) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String urlString = "https://s3.amazonaws.com/phzmapi.org/" + zipCode + ".json";
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + connection.getResponseCode());
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return handleApiResponse(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions
        } finally {
            // Clean up
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }

    private String handleApiResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("zone");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
