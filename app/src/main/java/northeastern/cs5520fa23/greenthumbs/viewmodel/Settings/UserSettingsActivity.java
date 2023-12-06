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

import northeastern.cs5520fa23.greenthumbs.LogInPageActivity;
import northeastern.cs5520fa23.greenthumbs.R;
import northeastern.cs5520fa23.greenthumbs.viewmodel.User;

public class UserSettingsActivity extends AppCompatActivity {
    private EditText usernameSettingsET;
    private EditText firstNameSettingsET;
    private EditText lastNameSettingsET;
    private EditText emailSettingsET;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);
        usernameSettingsET = findViewById(R.id.editUsernameSetting);
        firstNameSettingsET = findViewById(R.id.editFirstNameSetting);
        lastNameSettingsET = findViewById(R.id.editLastNameSetting);
        emailSettingsET = findViewById(R.id.editEmailSetting);
        EditText passwordSettingsET = findViewById(R.id.editPasswordSetting);
        Button saveChangesbtn = findViewById(R.id.save_changes_settings);
        Button deleteAccountbtn = findViewById(R.id.delete_account);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        dbRef = fdb.getReference("users");

        loadUserData();

        saveChangesbtn.setOnClickListener(v -> saveUserChanges());
        deleteAccountbtn.setOnClickListener(v -> deleteUserAccount());

    }

    private void deleteUserAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            String userID = user.getUid();
            dbRef.child("users").child(userID).removeValue()
                    .addOnSuccessListener(aVoid -> user.delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserSettingsActivity.this, "Account deleted!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserSettingsActivity.this, LogInPageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UserSettingsActivity.this, "Account Deletion Failure", Toast.LENGTH_SHORT).show();
                        }
                    })).addOnFailureListener(e -> Toast.makeText(UserSettingsActivity.this, "User data could not be deleted", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No logged in user", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserChanges() {
        String userId = currentUser.getUid();

        String updatedUsername = usernameSettingsET.getText().toString().trim();
        String updatedFirstName = firstNameSettingsET.getText().toString().trim();
        String updatedLastName = lastNameSettingsET.getText().toString().trim();
        String updatedEmail = emailSettingsET.getText().toString().trim();


        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put("username", updatedUsername);
        updatedUserData.put("firstName", updatedFirstName);
        updatedUserData.put("lastName", updatedLastName);
        updatedUserData.put("email", updatedEmail);

        dbRef.child("users").child(userId).updateChildren(updatedUserData)
                .addOnSuccessListener(aVoid -> Toast.makeText(UserSettingsActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(UserSettingsActivity.this, "Failed to update", Toast.LENGTH_SHORT).show());
    }

    private void loadUserData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            dbRef.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user != null) {
                            usernameSettingsET.setText(user.getUsername());
                            firstNameSettingsET.setText(user.getFirstName());
                            lastNameSettingsET.setText(user.getLastName());
                            emailSettingsET.setText(user.getEmail());
                        }
                    } else {
                        Toast.makeText(UserSettingsActivity.this, "User data unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(UserSettingsActivity.this, "Save Cancelled", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please log in.", Toast.LENGTH_SHORT).show();
        }
    }
}
