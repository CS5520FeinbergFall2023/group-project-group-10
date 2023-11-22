package northeastern.cs5520fa23.greenthumbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import northeastern.cs5520fa23.greenthumbs.Dashboard.DashboardFragment;
import northeastern.cs5520fa23.greenthumbs.Garden.GardenFragment;
import northeastern.cs5520fa23.greenthumbs.SocialFeed.SocialFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navBar;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private SocialFragment socialFragment = new SocialFragment();
    private GardenFragment gardenFragment = new GardenFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}