package northeastern.cs5520fa23.greenthumbs.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthHelper {
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";

    // Call this method after the user logs in successfully
    public static void storeUserDetails(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String userEmail = user.getEmail();

            // Access the app's SharedPreferences
            SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putString(KEY_USER_ID, userId);
            editor.putString(KEY_USER_EMAIL, userEmail);

            editor.apply();
        }
    }

    // Method to retrieve the stored user ID
    public static String getUserId(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_USER_ID, null);
    }

    // Method to retrieve the stored user email
    public static String getUserEmail(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPrefs.getString(KEY_USER_EMAIL, "");
    }

}

