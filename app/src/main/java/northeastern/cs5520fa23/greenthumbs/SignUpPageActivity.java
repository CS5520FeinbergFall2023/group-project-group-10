package northeastern.cs5520fa23.greenthumbs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private Button signUpButton;

    private ProgressDialog progressDialog;

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.editTextUserName);
        firstNameEditText = findViewById(R.id.editTextFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        emailEditText = findViewById(R.id.editTextemail);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        signUpButton = findViewById(R.id.SignUpbutton);

        if (savedInstanceState != null) {
            usernameEditText.setText(savedInstanceState.getString("username", ""));
            firstNameEditText.setText(savedInstanceState.getString("firstName", ""));
            lastNameEditText.setText(savedInstanceState.getString("lastName", ""));
            emailEditText.setText(savedInstanceState.getString("email", ""));
            passwordEditText.setText(savedInstanceState.getString("password", ""));
        }

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!isNetworkConnected()) {
                    Toast.makeText(SignUpPageActivity.this, "No internet connection available", Toast.LENGTH_SHORT).show();
                    return;
                }

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
            }
        });
    }

    private void createUser(String email, String password, String username, String firstName, String lastName, UserCreationCallback callback){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account...");
        progressDialog.setCancelable(false); // Optional: make the dialog not cancelable
        progressDialog.show();

        startSignupTimer();

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
        dbref.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    timerHandler.removeCallbacks(timerRunnable);
                    Toast.makeText(SignUpPageActivity.this, "Username already taken. Please choose another.", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    createFirebaseUser(email, password, username, firstName, lastName, callback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                timerHandler.removeCallbacks(timerRunnable);
                Toast.makeText(SignUpPageActivity.this, "Error checking username uniqueness", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createFirebaseUser(String email, String password, String username, String firstName, String lastName, UserCreationCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    dismissProgressDialog();
                    if (task.isSuccessful()) {
                        FirebaseUser fbUser = mAuth.getCurrentUser();
                        if (fbUser != null) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("firstName", firstName);
                            user.put("lastName", lastName);
                            user.put("email", email);
                            user.put("user_id", fbUser.getUid());

                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
                            dbref.child(fbUser.getUid()).setValue(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignUpPageActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                                        callback.onSuccess(fbUser);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignUpPageActivity.this, "Failure to Save User Data", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", usernameEditText.getText().toString());
        outState.putString("firstName", firstNameEditText.getText().toString());
        outState.putString("lastName", lastNameEditText.getText().toString());
        outState.putString("email", emailEditText.getText().toString());
        outState.putString("password", passwordEditText.getText().toString());
    }


    private boolean validateInput(String username, String firstName, String lastName, String email, String password) {
        if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all empty fields!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(username.length() < 5) {
            Toast.makeText(this, "Username must be at least 5 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = () -> Toast.makeText(SignUpPageActivity.this, "Signup is taking longer than expected", Toast.LENGTH_SHORT).show();

    private void startSignupTimer() {
        timerHandler.postDelayed(timerRunnable, 10000);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        timerHandler.removeCallbacks(timerRunnable);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

}
