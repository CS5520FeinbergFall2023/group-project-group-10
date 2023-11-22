package northeastern.cs5520fa23.greenthumbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import northeastern.cs5520fa23.greenthumbs.services.WeatherService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startWeatherService();
    }

    private void startWeatherService() {
        Intent serviceIntent = new Intent(this, WeatherService.class);
        // Update with home location fetched from user
        serviceIntent.putExtra(WeatherService.latitude, 42.3458);
        serviceIntent.putExtra(WeatherService.longitude, -71.0947);
        startService(serviceIntent);
    }
}