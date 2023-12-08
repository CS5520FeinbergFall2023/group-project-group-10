package northeastern.cs5520fa23.greenthumbs.viewmodel.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import northeastern.cs5520fa23.greenthumbs.LogInActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class UserSettingsActivity extends AppCompatActivity {
    private EditText usernameSettingsET;
    private EditText firstNameSettingsET;
    private EditText lastNameSettingsET;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    FirebaseDatabase db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);

        usernameSettingsET = findViewById(R.id.editUsernameSetting);
        firstNameSettingsET = findViewById(R.id.editFirstNameSetting);
        lastNameSettingsET = findViewById(R.id.editLastNameSetting);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        loadUserData();

        Button saveChangesBtn = findViewById(R.id.save_changes_settings);
        saveChangesBtn.setOnClickListener(v -> saveUserChanges());
    }

    private void redirectToLogin() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUserChanges() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String updatedUsername = usernameSettingsET.getText().toString().trim();
            String updatedFirstName = firstNameSettingsET.getText().toString().trim();
            String updatedLastName = lastNameSettingsET.getText().toString().trim();

            Map<String, Object> updatedUserData = new HashMap<>();
            updatedUserData.put("username", updatedUsername);
            updatedUserData.put("firstName", updatedFirstName);
            updatedUserData.put("lastName", updatedLastName);

            dbRef = db.getReference("users").child(userId);
            dbRef.updateChildren(updatedUserData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UserSettingsActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UserSettingsActivity.this, "Failed to update" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(UserSettingsActivity.this, "No user", Toast.LENGTH_SHORT).show();
        }
    }



    private void loadUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            dbRef = db.getReference("users").child(userId);
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        User user = snapshot.getValue(User.class);

                        if (user != null) {
                            usernameSettingsET.setText(user.getUsername());
                            firstNameSettingsET.setText(user.getFirstName());
                            lastNameSettingsET.setText(user.getLastName());
                        }
                    } else {
                        Toast.makeText(UserSettingsActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserSettingsActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UserSettingsActivity.this, "Please Log In", Toast.LENGTH_SHORT).show();
        }
    }
}
