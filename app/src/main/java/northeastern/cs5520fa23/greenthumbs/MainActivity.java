package northeastern.cs5520fa23.greenthumbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.MessageHomeFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SetLocationFragment;

import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.DashboardFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.CreatePostFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView navBar;
    private Toolbar toolbar;
    private DashboardFragment dashboardFragment = new DashboardFragment();
    private SocialFragment socialFragment = new SocialFragment();
    private GardenFragment gardenFragment = new GardenFragment();
    private CreatePostFragment createPostFragment = new CreatePostFragment();
    private MessageHomeFragment messageHomeFragment = new MessageHomeFragment();
    private String username;
    private String uid;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar = findViewById(R.id.toolbar);
        MenuItem notifications = menu.findItem(R.id.appbar_notifications);
        // use a layerdrawable with the number of notifications

        return true;
    }

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
        }
        uid = mAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Unable to fetch username", Toast.LENGTH_LONG).show();
                } else {
                    User currUser = task.getResult().getValue(User.class);
                    username = currUser.getUsername();
                }
            }
        });

        // ### Home Location ###
        //if (!isHomeLocationSet()) {
            //showSetLocationFragment();
        //} else {
            //startWeatherService();
        //}
        //ImageButton btnShowSetLocation = findViewById(R.id.btnShowSetLocation);
        //btnShowSetLocation.setOnClickListener(view -> showSetLocationFragment());
        // #####
        // ### Nav bar and toolbar ###
        navBar = findViewById(R.id.bottom_nav_menu);
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.appbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.appbar_profile) {
                    Fragment profileFragment = ProfileFragment.newInstance(username, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
                    return true;
                }
                return false;
            }
        });

        // When app is opened go to dashboard
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment).commit();

        navBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.dash_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.social_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, socialFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.garden_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, gardenFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.settings_menu_item) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, createPostFragment).commit();
                //return true;
            } else if (item.getItemId() == R.id.messages_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, messageHomeFragment).commit();
                return true;
            }

            return false;
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
        if (findViewById(R.id.overlay_frame).getVisibility() == View.GONE) {
            findViewById(R.id.overlay_frame).setVisibility(View.VISIBLE);
            SetLocationFragment setLocationFragment = new SetLocationFragment();
            setLocationFragment.show(getSupportFragmentManager(), "setLocationFragment");
            findViewById(R.id.overlay_frame).setVisibility(View.GONE);
        }
    }
}