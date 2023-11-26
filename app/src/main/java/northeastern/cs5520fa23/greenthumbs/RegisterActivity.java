package northeastern.cs5520fa23.greenthumbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.xml.transform.Result;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private Button toLoginButton;
    EditText username;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButton = findViewById(R.id.register_button_register_activity);
        toLoginButton = findViewById(R.id.login_button_register_activity);
        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        mAuth = FirebaseAuth.getInstance();
        toLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(i);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String regUsername = username.getText().toString();
                String regEmail = email.getText().toString();
                String regPassword = password.getText().toString();
                registerUser(regUsername, regEmail, regPassword);


            }
        });


    }

    public void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currUser = mAuth.getCurrentUser();
                } else {
                    Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_LONG);
                }
            }
        });
    }
}