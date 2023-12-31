package northeastern.cs5520fa23.greenthumbs;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import android.os.Bundle;
import android.widget.Toast;

import android.content.Context;
import android.Manifest;

import northeastern.cs5520fa23.greenthumbs.model.services.BackgroundService;
import northeastern.cs5520fa23.greenthumbs.model.services.PlantRecommendationService;
import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;
import northeastern.cs5520fa23.greenthumbs.model.weather.WeatherCheckWorker;

import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.MessageHomeFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SetLocationFragment;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.DashboardFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Settings.SettingsFragment;
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
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private CreatePostFragment createPostFragment = new CreatePostFragment();
    private MessageHomeFragment messageHomeFragment = new MessageHomeFragment();
    private String username;
    private String goUsername;
    private String goUid;
    private String uid;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Fragment profileFragment = new ProfileFragment();
    private AppBarConfiguration appBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
            finish();
            return;
        }
        uid = mAuth.getCurrentUser().getUid();
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        serviceIntent.putExtra("userIdKey", uid);

        /*
        FirebaseDatabase.getInstance().getReference("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Unable to fetch username", Toast.LENGTH_LONG).show();
                } else {
                    User currUser = task.getResult().getValue(User.class);
                    assert currUser != null;
                    username = currUser.getUsername();
                    serviceIntent.putExtra("username", username);
                    startService(serviceIntent);
                }
            }

         */
        FirebaseDatabase.getInstance().getReference("users").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Unable to fetch username", Toast.LENGTH_LONG).show();
            } else {
                User currUser = task.getResult().getValue(User.class);
                if (currUser != null) {
                    username = currUser.getUsername();
                    serviceIntent.putExtra("username", username);
                    startService(serviceIntent);
                } else {
                    Intent i = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        if (!isHomeLocationSet()) {
            requestLocationPermissions();
        }

        startPlantRecommendationService();

        PeriodicWorkRequest weatherCheckRequest = new PeriodicWorkRequest.Builder(WeatherCheckWorker.class,
                1, TimeUnit.HOURS) // Run every 1 hour, for example
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build();

        WorkManager.getInstance(MainActivity.this).enqueueUniquePeriodicWork(
                "weatherCheck",
                ExistingPeriodicWorkPolicy.KEEP, weatherCheckRequest);

        //ImageButton btnShowSetLocation = findViewById(R.id.btnShowSetLocation);
        //btnShowSetLocation.setOnClickListener(view -> showSetLocationFragment());
        // #####
        // ### Nav bar and toolbar ###
        navBar = findViewById(R.id.bottom_nav_menu);
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.appbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.appbar_profile) {
                //Fragment profileFragment = ProfileFragment.newInstance(username, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                Bundle extras = new Bundle();
                ArrayList<String> userInfo = new ArrayList<>();
                userInfo.add(username);
                userInfo.add(uid);
                extras.putStringArrayList("user_info", userInfo);
                i.putExtra("profile_info", extras);
                startActivity(i);
                return true;
            }
            return false;
        });

        // Set Up Navigation Component by giving bottom navbar and fragment container to the controller.
        // Menu item and fragments are linked because they share same id
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_menu);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Lock orientation for garden fragment
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.garden_menu_item) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void startPlantRecommendationService() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("latitude", 0);
        float longitude = sharedPreferences.getFloat("longitude", 0);

        if (latitude != 0 && longitude != 0) {
            Intent serviceIntent = new Intent(MainActivity.this, PlantRecommendationService.class);
            serviceIntent.putExtra(PlantRecommendationService.latitude, latitude);
            serviceIntent.putExtra(PlantRecommendationService.longitude, longitude);
            startService(serviceIntent);
        } else {
            /*Toast.makeText(this, "Location Undetermined. Please update your location " +
                    "in the settings menu.", Toast.LENGTH_LONG).show();*/
        }
    }

    private boolean isHomeLocationSet() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        return sharedPreferences.contains("latitude") && sharedPreferences.contains("longitude");
    }

    private void getUserLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;

        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation != null) {
                saveLocationInPreferences(lastKnownLocation);
                startWeatherService();
                startPlantRecommendationService();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void saveLocationInPreferences(Location location) {
        SharedPreferences sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("latitude", (float) location.getLatitude());
        editor.putFloat("longitude", (float) location.getLongitude());
        editor.apply();
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MainActivity.super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation();
            } else {
                Toast.makeText(MainActivity.this, "Location permissions not provided, " +
                        "please provide the permissions to enable location based features" +
                        "", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startWeatherService() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        float latitude = sharedPreferences.getFloat("latitude", 0);
        float longitude = sharedPreferences.getFloat("longitude", 0);
        if (latitude != 0 && longitude != 0) {
            Intent serviceIntent = new Intent(MainActivity.this, WeatherService.class);
            serviceIntent.putExtra(WeatherService.latitude, latitude);
            serviceIntent.putExtra(WeatherService.longitude, longitude);
            startService(serviceIntent);
        } else {
            Toast.makeText(MainActivity.this, "Location Undetermined. Please update your location " +
                    "in the settings menu.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUserLastActiveTimestamp();
    }

    private void updateUserLastActiveTimestamp() {
        if (mAuth.getCurrentUser() != null) {
            String currentUserId = mAuth.getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
            userRef.child("lastActiveTime").setValue(System.currentTimeMillis());
        }
    }


}