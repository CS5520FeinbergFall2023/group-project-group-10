package northeastern.cs5520fa23.greenthumbs;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpPageActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;

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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if(validateInput(username, firstName, lastName, email, password)) {
                    createUser(email, password, username, firstName, lastName);
                    Toast.makeText(SignUpPageActivity.this, "Sign Up Success!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String username, String firstName, String lastName){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser fbUser = mAuth.getCurrentUser();
                            Map<String, Object> user = new HashMap<>();
                            user.put("username", username);
                            user.put("firstName", firstName);
                            user.put("lastName", lastName);
                            user.put("email", email);

                            FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                            DatabaseReference dbref = fdb.getReference("users");
                            dbref.child(fbUser.getUid()).setValue(user);

                            db.collection("users").document(fbUser.getUid()).set(user).addOnSuccessListener(aVoid ->
                            {Toast.makeText(SignUpPageActivity.this, "User Created!", Toast.LENGTH_SHORT).show();
                            moveToLogin();
                            }).addOnFailureListener(e -> Toast.makeText(SignUpPageActivity.this, "Failure to Create New User", Toast.LENGTH_SHORT).show());
                        }else{
                            Toast.makeText(SignUpPageActivity.this, "Sign Up Failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
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
