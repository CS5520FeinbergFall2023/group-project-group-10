package northeastern.cs5520fa23.greenthumbs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button registerButton = findViewById(R.id.register_button_register_activity);
        Button toLoginButton = findViewById(R.id.login_button_register_activity);
        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        mAuth = FirebaseAuth.getInstance();
        toLoginButton.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
            startActivity(i);
        });
        registerButton.setOnClickListener(v -> {
            String regUsername = username.getText().toString();
            String regEmail = email.getText().toString();
            String regPassword = password.getText().toString();
            registerUser(regUsername, regEmail, regPassword);


        });


    }

    public void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currUser = mAuth.getCurrentUser();
            } else {
                Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_LONG).show();
            }
        });
    }
}