package northeastern.cs5520fa23.greenthumbs.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import northeastern.cs5520fa23.greenthumbs.R;

public class SetLocationFragment extends Fragment {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private Button btnSaveLocation;
    private Button btnFetchLocation;

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_location, container, false);

        editTextLatitude = view.findViewById(R.id.editTextLatitude);
        editTextLongitude = view.findViewById(R.id.editTextLongitude);
        btnSaveLocation = view.findViewById(R.id.btnSaveLocation);
        btnFetchLocation = view.findViewById(R.id.btnFetchLocation);

        btnSaveLocation.setOnClickListener(v -> saveHomeLocation(
                Double.parseDouble(editTextLatitude.getText().toString()),
                Double.parseDouble(editTextLongitude.getText().toString())
        ));

        btnFetchLocation.setOnClickListener(v -> requestLocationUpdates());

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        prepopulateLocationIfSet();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                editTextLatitude.setText(String.valueOf(location.getLatitude()));
                editTextLongitude.setText(String.valueOf(location.getLongitude()));
                locationManager.removeUpdates(this); // Stop updates to save battery
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}

        };

        return view;
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                // Handle the case where permission is denied
            }
        }
    }

    private void saveHomeLocation(double latitude, double longitude) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("HomeLatitude", (float) latitude);
        editor.putFloat("HomeLongitude", (float) longitude);
        editor.apply();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(SetLocationFragment.this);
        transaction.commit();
    }

    private void prepopulateLocationIfSet() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("HomeLatitude") && sharedPreferences.contains("HomeLongitude")) {
            float latitude = sharedPreferences.getFloat("HomeLatitude", 0);
            float longitude = sharedPreferences.getFloat("HomeLongitude", 0);
            editTextLatitude.setText(String.valueOf(latitude));
            editTextLongitude.setText(String.valueOf(longitude));
        }
    }

}