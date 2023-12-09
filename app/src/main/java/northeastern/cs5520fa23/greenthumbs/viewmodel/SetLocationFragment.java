package northeastern.cs5520fa23.greenthumbs.viewmodel;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

import northeastern.cs5520fa23.greenthumbs.R;

public class SetLocationFragment extends DialogFragment {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_location, container, false);
        editTextLatitude = view.findViewById(R.id.editTextLatitude);
        editTextLongitude = view.findViewById(R.id.editTextLongitude);
        Button btnSaveLocation = view.findViewById(R.id.btnSaveLocation);
        Button btnFetchLocation = view.findViewById(R.id.btnFetchLocation);
        btnSaveLocation.setOnClickListener(v -> {
            //showLoadingDialog();
            String lat = editTextLatitude.getText().toString();
            String lon = editTextLongitude.getText().toString();
            if (lat.isEmpty() || lon.isEmpty()) {
                Toast.makeText(getContext(), "Please set a location", Toast.LENGTH_SHORT).show();
            } else {
                saveHomeLocation(
                        Float.parseFloat(lat),
                        Float.parseFloat(lon)
                );
            }
            //hideLoadingDialog();
        });

        btnFetchLocation.setOnClickListener(v -> requestLocationUpdates());
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        prepopulateLocationIfSet();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //showLoadingDialog();
                //showLoadingDialog();
                double lat = location.getLatitude();
                editTextLatitude.setText(String.valueOf(lat));
                editTextLongitude.setText(String.valueOf(location.getLongitude()));
                locationManager.removeUpdates(this); // Stop updates to save battery
                //hideLoadingDialog();
                //hideLoadingDialog();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}

        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenHeight = displayMetrics.heightPixels;
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 3);
                window.setGravity(Gravity.TOP);
            }
        }
    }

    private void requestLocationUpdates() {
        //showLoadingDialog();
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        //hideLoadingDialog();
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

    private void saveHomeLocation(float latitude, float longitude) {
        //showLoadingDialog();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("latitude", latitude);
        editor.putFloat("longitude", longitude);
        editor.apply();

        FragmentManager fragmentManager;

        if (isAdded()) {
            Fragment parentFragment = getParentFragment();

            if (parentFragment != null) {
                 fragmentManager = parentFragment.getChildFragmentManager();
            } else {
                fragmentManager = requireActivity().getSupportFragmentManager();
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(this);
            transaction.commitAllowingStateLoss();
            //hideLoadingDialog();
        }

    }

    private void prepopulateLocationIfSet() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("latitude") && sharedPreferences.contains("longitude")) {
            float latitude = sharedPreferences.getFloat("latitude", 0);
            float longitude = sharedPreferences.getFloat("longitude", 0);
            editTextLatitude.setText(String.valueOf(latitude));
            editTextLongitude.setText(String.valueOf(longitude));
        }
    }

    private Dialog loadingDialog;

    private void showLoadingDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Fetching location...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideLoadingDialog() {

            progressDialog.dismiss();

    }

}