package northeastern.cs5520fa23.greenthumbs;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import northeastern.cs5520fa23.greenthumbs.model.services.WeatherService;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.Chat.ChatActivity;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Messages.MessageHomeFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Profile.ProfileFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SetLocationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Dashboard.DashboardFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Garden.GardenFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.Settings.SettingsFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.SocialFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.SocialFeed.CreatePostFragment;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class MainActivity extends AppCompatActivity {
    private final DashboardFragment dashboardFragment = new DashboardFragment();
    private final SocialFragment socialFragment = new SocialFragment();
    private final GardenFragment gardenFragment = new GardenFragment();
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final CreatePostFragment createPostFragment = new CreatePostFragment();
    private final MessageHomeFragment messageHomeFragment = new MessageHomeFragment();
    private String username;
    private String goUsername;
    private String goUid;
    private String uid;
    private Fragment profileFragment = new ProfileFragment();

    /*
    @Override
    protected void onStart() {
        super.onStart();}
     */

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar = findViewById(R.id.toolbar);
        MenuItem notifications = menu.findItem(R.id.appbar_notifications);
        // use a layerdrawable with the number of notifications
        return true;}
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(i);
            finish();
            return;
        }
        String uid = mAuth.getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("users").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Unable to fetch username", Toast.LENGTH_LONG).show();
            } else {
                User currUser = task.getResult().getValue(User.class);
                assert currUser != null;
                username = currUser.getUsername();
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
        BottomNavigationView navBar = findViewById(R.id.bottom_nav_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.appbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.appbar_profile) {
                Fragment profileFragment = ProfileFragment.newInstance(username, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).commit();
                return true;
            }
            return false;
        });
        // When app is opened go to dashboard
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment).commit();
        navBar.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.dash_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment, "DASH").addToBackStack("DASH").commit();
                return true;
            } else if (item.getItemId() == R.id.social_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, socialFragment, "SOCIAL").addToBackStack("SOCIAL").commit();
                return true;
            } else if (item.getItemId() == R.id.garden_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, gardenFragment, "GARDEN").addToBackStack("GARDEN").commit();
                return true;
            } else if (item.getItemId() == R.id.settings_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, settingsFragment).commit();
                return true;
            } else if (item.getItemId() == R.id.messages_menu_item) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, messageHomeFragment, "MESSAGE").addToBackStack("MESSAGE").commit();
                return true;
            }
            return false;
        });

        /*
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int i = getSupportFragmentManager().getBackStackEntryCount() - 1;
                if (i >= 0) {
                    FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
                    String fTag = entry.getName();
                    if (fTag == "SOCIAL") {
                        navBar.getMenu().getItem(1).setChecked(true);
                    }
                    getSupportFragmentManager().popBackStack(i, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }



            }
        };
        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);

         */

        if (getIntent().getExtras() != null) {
            boolean goChat  = getIntent().getBooleanExtra("to_chat", false);
            if (goChat) {
                navBar.setSelectedItemId(R.id.messages_menu_item);
                //getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, messageHomeFragment).commit();
            }
            boolean goPosts  = getIntent().getBooleanExtra("to_posts", false);
            if (goPosts) {
                navBar.setSelectedItemId(R.id.social_menu_item);
            }
            if (getIntent().getBundleExtra("profile_info") != null) {
                ArrayList<String> profileInfo = getIntent().getBundleExtra("profile_info").getStringArrayList("user_info");
                if (profileInfo != null) {
                    String goUsername = profileInfo.get(0);
                    String goUid = profileInfo.get(1);
                    profileFragment = ProfileFragment.newInstance(goUsername, goUid);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment).addToBackStack(null).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, profileFragment).addToBackStack(null).commit();
                    //toolbar.action(R.id.appbar_profile);
                }
            }



        } else {
            // When app is opened go to dashboard
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, dashboardFragment, "DASH").commit();
        }
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
            Toast.makeText(this, "Location Undetermined. Please update your location " +
                    "in the settings menu.", Toast.LENGTH_LONG).show();
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