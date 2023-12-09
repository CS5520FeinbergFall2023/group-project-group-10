package northeastern.cs5520fa23.greenthumbs.viewmodel.Settings;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class UserSettingsFragment extends Fragment {
    private EditText usernameSettingsET;
    private EditText firstNameSettingsET;
    private EditText lastNameSettingsET;
    private EditText emailSettingsET;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    FirebaseDatabase db;
    private FirebaseUser currentUser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState) {
        View view = inflater.inflate(R.layout.accountsettings, container, false);
        usernameSettingsET = view.findViewById(R.id.editUsernameSetting);
        firstNameSettingsET = view.findViewById(R.id.editFirstNameSetting);
        lastNameSettingsET = view.findViewById(R.id.editLastNameSetting);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        loadUserData();

        Button saveChangesBtn= view.findViewById(R.id.save_changes_settings);


        saveChangesBtn.setOnClickListener(v -> saveUserChanges());


        return view;
    }

    private void deleteUserAccount() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();
            dbRef = db.getReference("users").child(userID);
            dbRef.removeValue().addOnSuccessListener(aVoid -> {
                user.delete().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(getContext(), "Account deleted!", Toast.LENGTH_SHORT).show();
                        redirectToLogin();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to delete user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Please log in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getActivity(), LogInActivity.class);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }
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
                        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "No user logged in", Toast.LENGTH_SHORT).show();
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

                        if (user != null){
                            usernameSettingsET.setText(user.getUsername());
                            firstNameSettingsET.setText(user.getFirstName());
                            lastNameSettingsET.setText(user.getLastName());
                        }
                    } else {
                        Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Please Log In", Toast.LENGTH_SHORT).show();
        }
    }
}
