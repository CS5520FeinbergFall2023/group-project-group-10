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
        serviceIntent.putExtra(WeatherService.EXTRA_BOX_X, 70);
        serviceIntent.putExtra(WeatherService.EXTRA_BOX_Y, 89);
        startService(serviceIntent);
    }
}