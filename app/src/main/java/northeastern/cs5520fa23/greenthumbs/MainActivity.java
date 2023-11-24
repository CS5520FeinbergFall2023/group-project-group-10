package northeastern.cs5520fa23.greenthumbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SetLocationFragment;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.DashboardFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private SocialFragment socialFragment = new SocialFragment();
    private GardenFragment gardenFragment = new GardenFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isHomeLocationSet()) {
            showSetLocationFragment();
        } else {
            startWeatherService();
        }

        ImageButton btnShowSetLocation = findViewById(R.id.btnShowSetLocation);
        btnShowSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetLocationFragment();
            }

        });
        navBar = findViewById(R.id.bottom_nav_menu);

        // When app is opened go to dashboard
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment).commit();

        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.dash_menu_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.social_menu_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, socialFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.garden_menu_item) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, gardenFragment).commit();
                    return true;
                }

                return false;
            }
        });

    }

    private void startWeatherService() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("HomeLatitude", 0);
        float longitude = sharedPreferences.getFloat("HomeLongitude", 0);

        if (latitude != 0 && longitude != 0) {
            Intent serviceIntent = new Intent(this, WeatherService.class);
            serviceIntent.putExtra(WeatherService.latitude, latitude);
            serviceIntent.putExtra(WeatherService.longitude, longitude);
            startService(serviceIntent);
        } else {
            // Handle the case where location is not set at this point
        }
    }

    private boolean isHomeLocationSet() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.contains("HomeLatitude") && sharedPreferences.contains("HomeLongitude");
    }

    private void showSetLocationFragment() {
        SetLocationFragment setLocationFragment = new SetLocationFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, setLocationFragment)
                .addToBackStack(null)
                .commit();
    }

}