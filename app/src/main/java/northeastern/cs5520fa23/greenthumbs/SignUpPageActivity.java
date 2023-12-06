package northeastern.cs5520fa23.greenthumbs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import northeastern.cs5520fa23.greenthumbs.model.UserCreationCallback;

public class SignUpPageActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.editTextUserName);
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextemail);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        Button signUpButton = findViewById(R.id.SignUpbutton);

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if(validateInput(username, firstName, lastName, email, password)) {
                createUser(email, password, username, firstName, lastName, new UserCreationCallback() {
                    @Override
                    public void onSuccess(FirebaseUser fbUser) {
                        Toast.makeText(SignUpPageActivity.this, "Successfully created the user!", Toast.LENGTH_SHORT).show();
                        moveToLogin();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure in user creation
                        Toast.makeText(SignUpPageActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void createUser(String email, String password, String username, String firstName, String lastName, UserCreationCallback callback){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("firstName", firstName);
                        user.put("lastName", lastName);
                        user.put("email", email);

                        if (fbUser != null) {
                            user.put("user_id", fbUser.getUid());
                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
                            dbref.child(fbUser.getUid()).setValue(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignUpPageActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                                        callback.onSuccess(fbUser);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignUpPageActivity.this, "Failure to Create New User", Toast.LENGTH_SHORT).show();
                                        callback.onFailure(e);
                                    });
                        } else {
                            callback.onFailure(new Exception("FirebaseUser is null"));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    private void moveToLogin(){
        Intent i = new Intent(SignUpPageActivity.this, LogInPageActivity.class);
        startActivity(i);
        finish();
    }

    private boolean validateInput(String username, String firstName, String lastName, String email, String password) {
        if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all empty fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
